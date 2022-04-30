package com.ecinema.app.services.implementations;

import com.ecinema.app.dtos.AddressDto;
import com.ecinema.app.dtos.TheaterDto;
import com.ecinema.app.entities.Address;
import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.Theater;
import com.ecinema.app.repositories.TheaterRepository;
import com.ecinema.app.services.AddressService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.services.ShowroomService;
import com.ecinema.app.services.TheaterService;
import com.ecinema.app.exceptions.NoEntityFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Optional;

/**
 * The type Theater service.
 */
@Service
@Transactional
public class TheaterServiceImpl extends AbstractServiceImpl<Theater, TheaterRepository>
        implements TheaterService {

    private final AddressService addressService;
    private final ShowroomService showroomService;
    private final ScreeningService screeningService;

    /**
     * Instantiates a new Theater service.
     *
     * @param repository the repository
     */
    public TheaterServiceImpl(TheaterRepository repository, AddressService addressService,
                              ShowroomService showroomService, ScreeningService screeningService) {
        super(repository);
        this.addressService = addressService;
        this.showroomService = showroomService;
        this.screeningService = screeningService;
    }

    @Override
    protected void onDelete(Theater theater) {
        // detach AdminRoleDefs
        // iterator to avoid concurrent modification exception
        Iterator<AdminRoleDef> adminRoleDefIterator = theater.getAdmins().iterator();
        while (adminRoleDefIterator.hasNext()) {
            AdminRoleDef adminRoleDef = adminRoleDefIterator.next();
            adminRoleDef.getTheatersBeingManaged().remove(theater);
            adminRoleDefIterator.remove();
        }
        // cascade delete Address
        Address address = theater.getAddress();
        theater.setAddress(null);
        addressService.delete(address);
        // cascade delete Showrooms
        showroomService.deleteAll(theater.getShowrooms().values());
    }

    @Override
    public Optional<Theater> findByTheaterName(String theaterName) {
        return repository.findByTheaterName(theaterName);
    }

    @Override
    public Optional<Theater> findByTheaterNumber(Integer theaterNumber) {
        return repository.findByTheaterNumber(theaterNumber);
    }

    @Override
    public void addShowroomToTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException {

    }

    @Override
    public void removeShowroomFromTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException {

    }

    @Override
    public void addScreeningToTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException {

    }

    @Override
    public void removeScreeningFromTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException {

    }

    @Override
    public TheaterDto convert(Long entityId)
            throws NoEntityFoundException {
        Theater theater = findById(entityId).orElseThrow(
                () -> new NoEntityFoundException("theater", "id", entityId));
        TheaterDto theaterDTO = new TheaterDto();
        theaterDTO.setId(theater.getId());
        theaterDTO.setTheaterName(theater.getTheaterName());
        theaterDTO.setTheaterNumber(theater.getTheaterNumber());
        AddressDto addressDto = addressService
                .convert(theater.getAddress().getId());
        theaterDTO.setAddressDTO(addressDto);
        return theaterDTO;
    }

}
