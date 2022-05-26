package com.ecinema.app.services;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.domain.dtos.CustomerDto;
import com.ecinema.app.domain.dtos.TicketDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.forms.SeatBookingsForm;
import com.ecinema.app.domain.objects.SeatBooking;
import com.ecinema.app.exceptions.*;
import com.ecinema.app.repositories.CustomerRepository;
import com.ecinema.app.repositories.ScreeningSeatRepository;
import com.ecinema.app.util.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for {@link Customer}
 */
@Service
@Transactional
public class CustomerService extends UserAuthorityService<Customer, CustomerRepository, CustomerDto> {

    private final EmailService emailService;
    private final ReviewService reviewService;
    private final TicketService ticketService;
    private final SecurityContext securityContext;
    private final ReviewVoteService reviewVoteService;
    private final PaymentCardService paymentCardService;
    private final ScreeningSeatRepository screeningSeatRepository;

    /**
     * Instantiates a new Customer service.
     *
     * @param repository              See {@link CustomerRepository}
     * @param screeningSeatRepository See {@link ScreeningSeatRepository}
     * @param emailService            See {@link EmailService}
     * @param reviewService           See {@link ReviewService}
     * @param ticketService           See {@link TicketService}
     * @param paymentCardService      See {@link PaymentCardService}
     * @param securityContext         See {@link SecurityContext}
     */
    @Autowired
    public CustomerService(CustomerRepository repository, ScreeningSeatRepository screeningSeatRepository,
                           EmailService emailService, ReviewService reviewService, TicketService ticketService,
                           PaymentCardService paymentCardService, ReviewVoteService reviewVoteService,
                           SecurityContext securityContext) {
        super(repository);
        this.emailService = emailService;
        this.ticketService = ticketService;
        this.reviewService = reviewService;
        this.securityContext = securityContext;
        this.reviewVoteService = reviewVoteService;
        this.paymentCardService = paymentCardService;
        this.screeningSeatRepository = screeningSeatRepository;
    }

    @Override
    protected void onDelete(Customer customer) {
        logger.debug("Customer on delete");
        // detach User
        super.onDelete(customer);
        // cascade delete Reviews
        logger.debug("Deleting all associated reviews");
        reviewService.deleteAll(customer.getReviews());
        // cascade delete Review Votes
        logger.debug("Deleting all associated review votes");
        reviewVoteService.deleteAll(customer.getReviewVotes());
        // cascade delete Tickets
        logger.debug("Deleting all associated tickets");
        ticketService.deleteAll(customer.getTickets());
        // cascade delete PaymentCards
        logger.debug("Deleting all associated payment cards");
        paymentCardService.deleteAll(customer.getPaymentCards());
        // detach Moderator censors
        Moderator moderator = customer.getCensoredBy();
        logger.debug("Detaching moderator role def: " + moderator);
        if (moderator != null) {
            customer.setCensoredBy(null);
            moderator.getCensoredCustomers().remove(customer);
        }
    }

    @Override
    public CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        User user = customer.getUser();
        if (user != null) {
            customerDto.setUserId(user.getId());
            customerDto.setEmail(user.getEmail());
            customerDto.setUsername(user.getUsername());
        }
        Moderator censor = customer.getCensoredBy();
        if (censor != null) {
            customerDto.setIsCensored(censor != null);
            customerDto.setCensorId(censor != null ? censor.getId() : null);
        }
        logger.debug("Converted " + customer + " to DTO: " + customerDto);
        return customerDto;
    }

    /**
     * Book tickets.
     *
     * @param seatBookingsForm the seat bookings form
     * @throws NoEntityFoundException      thrown if a requested {@link AbstractEntity} cannot be found
     * @throws ClashException              thrown if {@link ScreeningSeat#isBooked()} returns true
     * @throws PermissionDeniedException   thrown if the requesting client does not have {@link Customer} authority
     * @throws InvalidAssociationException thrown if the {@link PaymentCard} fetched via
     * {@link SeatBookingsForm#getPaymentCardId()} is not associated with the {@link Customer}
     * @throws ExpirationException         thrown if {@link PaymentCard#getExpirationDate()} is surpassed
     */
    public void bookTickets(SeatBookingsForm seatBookingsForm)
            throws NoEntityFoundException, ClashException, PermissionDeniedException,
            InvalidAssociationException, ExpirationException {
        logger.debug("Customer service: book tickets");
        // validate that user is logged into customer account and get user id
        Long userId = securityContext.findIdOfLoggedInUser();
        logger.debug("Logged-in user id: " + userId);
        if (userId == null || !repository.existsByUserWithId(userId)) {
            throw new PermissionDeniedException("Cannot book tickets if not logged into a valid customer account");
        }
        // find customer by user id
        Customer customer = repository.findByUserWithId(userId).orElseThrow(
                () -> new NoEntityFoundException("customer", "id", seatBookingsForm.getCustomerId()));
        logger.debug("Found customer by user id: " + customer);
        // validate each seat booking contains a valid screening seat id
        // and that each screening seat is not already booked
        seatBookingsForm.getSeatBookings().forEach(seatBooking -> {
            if (!screeningSeatRepository.existsById(seatBooking.getSeatId()))  {
                throw new NoEntityFoundException("screening seat", "id", seatBooking.getSeatId());
            }
            if (screeningSeatRepository.screeningSeatIsBooked(seatBooking.getSeatId())) {
                throw new ClashException("Screening seat with id " + seatBooking.getSeatId() + " is already booked");
            }
        });
        logger.debug("Seat bookings form passes validation checks");
        // find payment card by payment card id
        PaymentCard paymentCard = paymentCardService.repository.findById(seatBookingsForm.getPaymentCardId())
                .orElseThrow(() -> new NoEntityFoundException(
                        "payment card", "id", seatBookingsForm.getPaymentCardId()));
        logger.debug("Payment card is associated with logged-in user");
        // validate payment card is associated with logged-in customer account
        if (!paymentCardService.isPaymentCardOwnedByUser(paymentCard.getId(), userId)) {
            throw new InvalidAssociationException("Payment card is not owned by user with id " + userId);
        }
        // validate payment card is not expired
        if (paymentCard.getExpirationDate().isBefore(LocalDate.now())) {
            throw new ExpirationException("Payment card cannot be used for this booking because it " +
                    "expired on " + UtilMethods.localDateFormatted(paymentCard.getExpirationDate()));
        }
        logger.debug("Payment card passes validation checks");
        // instantiate new tickets and collect ticket dtos for email
        List<TicketDto> ticketDtos = new ArrayList<>();
        logger.debug("Finding each screening seat by id and instantiating new ticket");
        for (SeatBooking seatBooking : seatBookingsForm.getSeatBookings()) {
            ScreeningSeat screeningSeat = screeningSeatRepository.findById(seatBooking.getSeatId()).orElseThrow(
                    () -> new NoEntityFoundException("screening seat", "id", seatBooking.getSeatId()));
            logger.debug("Found screening seat " + screeningSeat + " by id " + seatBooking.getSeatId());
            Ticket ticket = new Ticket();
            ticket.setTicketOwner(customer);
            customer.getTickets().add(ticket);
            ticket.setScreeningSeat(screeningSeat);
            screeningSeat.setTicket(ticket);
            ticket.setTicketType(seatBooking.getTicketType());
            ticket.setTicketStatus(TicketStatus.VALID);
            ticket.setCreationDateTime(LocalDateTime.now());
            ticketService.save(ticket);
            logger.debug("Instantiated and saved new ticket: " + ticket);
            TicketDto ticketDto = ticketService.convertToDto(ticket);
            logger.debug("Fetched DTO of ticket: " + ticketDto);
            ticketDtos.add(ticketDto);
        }
        String email = customer.getUser().getEmail();
        logger.debug("Email being sent to customer's email: " + email);
        String message = buildBookTicketsEmail(ticketDtos);
        logger.debug("Email message: " + message);
        // email ticket booking info to customer
        emailService.sendFromBusinessEmail(email, message, "Ticket Bookings");
    }

    private String buildBookTicketsEmail(List<TicketDto> ticketDtos) {
        StringBuilder sb = new StringBuilder();
        sb.append("THIS IS A CONFIRMATION EMAIL FOR YOUR RECENT TICKET PURCHASES\n");
        sb.append("You have purchased the following tickets:\n");
        ticketDtos.forEach(ticketDto -> {
            String message = "\tPurchased " + ticketDto.getTicketType() + " at " +
                    UtilMethods.localDateTimeFormatted(ticketDto.getCreationDateTime()) + "\n" +
                    "\tfor seat " + ticketDto.getSeatDesignation() + "\n" +
                    "\tfor " + ticketDto.getMovieTitle() + " in showroom " + ticketDto.getShowroomLetter() + "\n" +
                    "\tat " + UtilMethods.localDateTimeFormatted(ticketDto.getShowtime()) + "\n\n";
            sb.append(message);
        });
        return sb.toString();
    }

}
