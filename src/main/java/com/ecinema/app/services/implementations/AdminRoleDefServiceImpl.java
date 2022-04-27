package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.entities.Theater;
import com.ecinema.app.entities.User;
import com.ecinema.app.repositories.AdminRoleDefRepository;
import com.ecinema.app.services.AdminRoleDefService;
import com.ecinema.app.services.AdminTraineeRoleDefService;
import com.ecinema.app.services.TheaterService;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.aspectj.lang.NoAspectBoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Admin role def service.
 */
@Service
@Transactional
public class AdminRoleDefServiceImpl extends UserRoleDefServiceImpl<AdminRoleDef,
        AdminRoleDefRepository> implements AdminRoleDefService {

    private final TheaterService theaterService;
    private final AdminTraineeRoleDefService adminTraineeRoleDefService;

    /**
     * Instantiates a new Admin role def service.
     *
     * @param repository                 the repository
     * @param theaterService             the theater service
     * @param adminTraineeRoleDefService the admin trainee role def service
     */
    public AdminRoleDefServiceImpl(AdminRoleDefRepository repository, TheaterService theaterService,
                                   AdminTraineeRoleDefService adminTraineeRoleDefService) {
        super(repository);
        this.theaterService = theaterService;
        this.adminTraineeRoleDefService = adminTraineeRoleDefService;
    }

    @Override
    protected void onDelete(AdminRoleDef adminRoleDef) {
        // detach User
        super.onDelete(adminRoleDef);
        // detach Theaters
        for (Theater theater : adminRoleDef.getTheatersBeingManaged()) {
            theater.getAdmins().remove(adminRoleDef);
            adminRoleDef.getTheatersBeingManaged().remove(theater);
        }
        // detach Trainees
        for (AdminTraineeRoleDef adminTraineeRoleDef : adminRoleDef.getTrainees()) {
            adminTraineeRoleDef.setMentor(null);
            adminRoleDef.getTrainees().remove(adminTraineeRoleDef);
        }
    }

    @Override
    public void addTheaterToAdminRoleDef(Long theaterId, Long adminRoleDefId)
            throws NoEntityFoundException {
        Theater theater = theaterService.findById(theaterId)
                .orElseThrow(() -> new NoEntityFoundException("Theater", "id", theaterId));
        AdminRoleDef adminRoleDef = findById(adminRoleDefId)
                .orElseThrow(() -> new NoEntityFoundException("Admin","id", adminRoleDefId));
        theater.getAdmins().add(adminRoleDef);
        adminRoleDef.getTheatersBeingManaged().add(theater);
    }

    @Override
    public void removeTheaterFromAdminRoleDef(Long theaterId, Long adminRoleDefId)
            throws NoEntityFoundException {
        Theater theater = theaterService.findById(theaterId).orElseThrow(
                () -> new NoEntityFoundException("Theater", "id", theaterId));
        AdminRoleDef adminRoleDef = findById(adminRoleDefId).orElseThrow(
                () -> new NoEntityFoundException("Admin", "id", adminRoleDefId));
        theater.getAdmins().remove(adminRoleDef);
        adminRoleDef.getTheatersBeingManaged().remove(theater);
    }

    @Override
    public void addTraineeToAdminRoleDef(Long adminTraineeRoleDefId, Long adminRoleDefId)
            throws NoEntityFoundException {
        AdminTraineeRoleDef adminTraineeRoleDef = adminTraineeRoleDefService.findById(adminTraineeRoleDefId)
                .orElseThrow(() -> new NoEntityFoundException("Admin Trainee", "id", adminTraineeRoleDefId));
        AdminRoleDef adminRoleDef = findById(adminRoleDefId).orElseThrow(
                () -> new NoEntityFoundException("Admin", "id", adminRoleDefId));
        adminTraineeRoleDef.setMentor(adminRoleDef);
        adminRoleDef.getTrainees().add(adminTraineeRoleDef);
    }

    @Override
    public void removeTraineeFromAdminRoleDef(Long adminTraineeRoleDefId, Long adminRoleDefId)
            throws NoEntityFoundException {
        AdminTraineeRoleDef adminTraineeRoleDef = adminTraineeRoleDefService.findById(adminTraineeRoleDefId)
                .orElseThrow(() -> new NoEntityFoundException("Admin Trainee", "id", adminTraineeRoleDefId));
        AdminRoleDef adminRoleDef = findById(adminRoleDefId).orElseThrow(
                () -> new NoEntityFoundException("Admin", "id", adminRoleDefId));
        adminTraineeRoleDef.setMentor(null);
        adminRoleDef.getTrainees().remove(adminTraineeRoleDef);
    }

}
