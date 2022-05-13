package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.TicketDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.TicketForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.FatalErrorException;
import com.ecinema.app.exceptions.InvalidAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.TicketService;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * The type Ticket service.
 */
@Service
@Transactional
public class TicketServiceImpl extends AbstractServiceImpl<Ticket, TicketRepository>
        implements TicketService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    /**
     * Instantiates a new Ticket service.
     *
     * @param repository the repository
     */
    public TicketServiceImpl(TicketRepository repository,
                             UserRepository userRepository,
                             CouponRepository couponRepository,
                             ScreeningSeatRepository screeningSeatRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.screeningSeatRepository = screeningSeatRepository;
    }

    @Override
    protected void onDelete(Ticket ticket) {
        logger.debug("Ticket on delete");
        // detach ScreeningSeat
        ScreeningSeat screeningSeat = ticket.getScreeningSeat();
        logger.debug("Detaching " + screeningSeat + " from " + ticket);
        if (screeningSeat != null) {
            screeningSeat.setTicket(null);
            ticket.setScreeningSeat(null);
        }
        // detach CustomerAuthority
        CustomerAuthority customerAuthority = ticket.getTicketOwner();
        logger.debug("Detaching " + customerAuthority + " from " + ticket);
        if (customerAuthority != null) {
            customerAuthority.getTickets().remove(ticket);
            ticket.setTicketOwner(null);
        }
        // detach Coupons
        Iterator<Coupon> couponIterator = ticket.getCoupons().iterator();
        while (couponIterator.hasNext()) {
            Coupon coupon = couponIterator.next();
            logger.debug("Detaching " + coupon + " from " + ticket);
            coupon.setTicket(null);
            couponIterator.remove();
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        Ticket ticket = findById(id).orElseThrow(
                () -> new NoEntityFoundException("ticket","id", id));
        onDeleteInfo(ticket, info);
    }

    @Override
    public void onDeleteInfo(Ticket ticket, Collection<String> info) {
        String username = ticket.getTicketOwner().getUser().getUsername();
        info.add("Ticket purchased by " + username + " for screening seat " +
                         ticket.getScreeningSeat().seatDesignation() + " will be deleted");
        ticket.getCoupons().forEach(
                coupon -> info.add(coupon.getCouponType().toString() + " will be detached from ticket" +
                                           " but will not deleted; customer can still use coupon for another ticket"));
    }

    @Override
    public void submitTicketForm(TicketForm ticketForm)
            throws NoEntityFoundException, ClashException, InvalidAssociationException, FatalErrorException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Submit Ticket Form");
        ScreeningSeat screeningSeat = screeningSeatRepository
                .findById(ticketForm.getScreeningSeatId()).orElseThrow(
                        () -> new NoEntityFoundException(
                                "screening seat", "id", ticketForm.getScreeningSeatId()));
        logger.debug("Screening seat found by id");
        if (screeningSeat.getTicket() != null) {
            throw new ClashException("Ticket for screening seat already exists");
        }
        logger.debug("Screening seat is not already associated with a ticket");
        User user = userRepository.findById(ticketForm.getUserId()).orElseThrow(
                () -> new NoEntityFoundException("user", "id", ticketForm.getUserId()));
        logger.debug("User found by id");
        AbstractUserAuthority AbstractUserAuthority = user.getUserAuthorities().get(UserAuthority.CUSTOMER);
        if (AbstractUserAuthority == null) {
            throw new InvalidAssociationException("User does not have customer authority and as " +
                                                          "a result cannot book tickets");
        }
        logger.debug("User has user role def mapped to CUSTOMER");
        if (!(AbstractUserAuthority instanceof CustomerAuthority)) {
            throw new FatalErrorException("Fatal error: There is a user role def mapped to CUSTOMER key but " +
                                                  "the user role def is not an instance of CUSTOMER class");
        }
        logger.debug("User role def mapped to CUSTOMER is instance of customer role def");
        Ticket ticket = new Ticket();
        ticket.setCreationDateTime(LocalDateTime.now());
        ticket.setTicketOwner((CustomerAuthority) AbstractUserAuthority);
        ((CustomerAuthority) AbstractUserAuthority).getTickets().add(ticket);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        ticket.setTicketStatus(TicketStatus.VALID);
        ticket.setTicketType(ticketForm.getTicketType());
        List<Coupon> coupons = couponRepository.findAllById(ticketForm.getCouponIds());
        for (Coupon coupon : coupons) {
            ticket.getCoupons().add(coupon);
            coupon.setTicket(ticket);
        }
        save(ticket);
        logger.debug("Instantiated and saved new ticket: " + ticket);
    }

    @Override
    public List<TicketDto> findAllByCreationDateTimeLessThanEqual(LocalDateTime localDateTime) {
        return convertToDto(repository.findAllByCreationDateTimeLessThanEqual(localDateTime));
    }

    @Override
    public List<TicketDto> findAllByCreationDateTimeGreaterThanEqual(LocalDateTime localDateTime) {
        return convertToDto(repository.findAllByCreationDateTimeGreaterThanEqual(localDateTime));
    }

    @Override
    public List<TicketDto> findAllByTicketStatus(TicketStatus ticketStatus) {
        return convertToDto(repository.findAllByTicketStatus(ticketStatus));
    }

    @Override
    public TicketDto convertIdToDto(Long id)
            throws NoEntityFoundException {
        Ticket ticket = findById(id).orElseThrow(
                () -> new NoEntityFoundException("ticket", "id", id));
        String username = ticket.getTicketOwner().getUser().getUsername();
        String movieTitle = ticket.getScreeningSeat().getScreening().getMovie().getTitle();
        Letter showroomLetter = ticket.getScreeningSeat().getScreening().getShowroom().getShowroomLetter();
        LocalDateTime showtime = ticket.getScreeningSeat().getScreening().getShowDateTime();
        ShowroomSeat showroomSeat = ticket.getScreeningSeat().getShowroomSeat();
        TicketDto ticketDto = new TicketDto();
        ticketDto.setUsername(username);
        ticketDto.setShowtime(showtime);
        ticketDto.setMovieTitle(movieTitle);
        ticketDto.setShowroomLetter(showroomLetter);
        ticketDto.setTicketType(ticket.getTicketType());
        ticketDto.setTicketStatus(ticket.getTicketStatus());
        ticketDto.setRowLetter(showroomSeat.getRowLetter());
        ticketDto.setSeatNumber(showroomSeat.getSeatNumber());
        ticketDto.setCreationDateTime(ticket.getCreationDateTime());
        logger.debug("Converted ticket to DTO: " + ticketDto);
        logger.debug("Ticket: " + ticket);
        return ticketDto;
    }

}
