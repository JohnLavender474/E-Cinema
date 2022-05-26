package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.TicketDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.objects.SeatDesignation;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.exceptions.NoFieldFoundException;
import com.ecinema.app.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService extends AbstractEntityService<Ticket, TicketRepository, TicketDto> {

    public TicketService(TicketRepository repository) {
        super(repository);
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
        // detach Customer
        Customer customer = ticket.getTicketOwner();
        logger.debug("Detaching " + customer + " from " + ticket);
        if (customer != null) {
            customer.getTickets().remove(ticket);
            ticket.setTicketOwner(null);
        }
    }

    @Override
    public TicketDto convertToDto(Ticket ticket)
            throws NoFieldFoundException {
        logger.debug("Convert ticket to DTO");
        logger.debug("Ticket: " + ticket);
        TicketDto ticketDto = new TicketDto();
        String username = findUsernameOfTicketUserOwner(ticket.getId())
                .orElseThrow(() -> new NoFieldFoundException("username", "ticket"));
        ticketDto.setUsername(username);
        String movieTitle = findMovieTitleAssociatedWithTicket(ticket.getId())
                .orElseThrow(() -> new NoFieldFoundException("movie title", "ticket"));
        ticketDto.setMovieTitle(movieTitle);
        Letter showroomLetter = findShowroomLetterAssociatedWithTicket(ticket.getId())
                .orElseThrow(() -> new NoFieldFoundException("showroom letter", "ticket"));
        ticketDto.setShowroomLetter(showroomLetter);
        LocalDateTime showtime = findShowtimeOfScreeningAssociatedWithTicket(ticket.getId())
                .orElseThrow(() -> new NoFieldFoundException("showtime", "ticket"));
        ticketDto.setShowtime(showtime);
        boolean refundable = Duration.between(LocalDateTime.now(), showtime)
                                     .compareTo(Duration.ofDays(3)) > 0;
        ticketDto.setIsRefundable(refundable);
        LocalDateTime endtime = findEndtimeOfScreeningAssociatedWithTicket(ticket.getId())
                .orElseThrow(() -> new NoFieldFoundException("endtime", "ticket"));
        ticketDto.setEndtime(endtime);
        SeatDesignation seatDesignation = findSeatDesignationOfTicket(ticket.getId())
                .orElseThrow(() -> new NoFieldFoundException("seat designation", "ticket"));
        ticketDto.setSeatDesignation(seatDesignation);
        ticketDto.setTicketType(ticket.getTicketType());
        ticketDto.setTicketStatus(ticket.getTicketStatus());
        ticketDto.setCreationDateTime(ticket.getCreationDateTime());
        logger.debug("Ticket DTO: " + ticketDto);
        return ticketDto;
    }

    public void requestRefund(Long ticketId)
            throws NoEntityFoundException {

    }

    public Optional<String> findUsernameOfTicketUserOwner(Long ticketId) {
        return repository.findUsernameOfTicketUserOwner(ticketId);
    }

    public Optional<String> findMovieTitleAssociatedWithTicket(Long ticketId) {
        return repository.findMovieTitleAssociatedWithTicket(ticketId);
    }

    public Optional<Letter> findShowroomLetterAssociatedWithTicket(Long ticketId) {
        return repository.findShowroomLetterAssociatedWithTicket(ticketId);
    }

    public Optional<LocalDateTime> findShowtimeOfScreeningAssociatedWithTicket(Long ticketId) {
        return repository.findShowtimeOfScreeningAssociatedWithTicket(ticketId);
    }

    public Optional<LocalDateTime> findEndtimeOfScreeningAssociatedWithTicket(Long ticketId) {
        return repository.findEndtimeOfScreeningAssociatedWithTicket(ticketId);
    }

    public Optional<SeatDesignation> findSeatDesignationOfTicket(Long ticketId)
            throws NoEntityFoundException {
        ShowroomSeat showroomSeat = repository
                .findShowroomSeatAssociatedWithTicket(ticketId).orElse(null);
        return showroomSeat != null ? Optional.of(new SeatDesignation(
                showroomSeat.getRowLetter(), showroomSeat.getSeatNumber())) : Optional.empty();
    }

    public List<TicketDto> findAllByUserWithId(Long userId) {
        return convertToDto(repository.findAllByUserWithId(userId));
    }

    public List<TicketDto> findAllByCreationDateTimeLessThanEqual(LocalDateTime localDateTime) {
        return convertToDto(
                repository.findAllByCreationDateTimeLessThanEqual(localDateTime));
    }

    public List<TicketDto> findAllByCreationDateTimeGreaterThanEqual(LocalDateTime localDateTime) {
        return convertToDto(
                repository.findAllByCreationDateTimeGreaterThanEqual(localDateTime));
    }

    public List<TicketDto> findAllByTicketStatus(TicketStatus ticketStatus) {
        return convertToDto(
                repository.findAllByTicketStatus(ticketStatus));
    }

}
