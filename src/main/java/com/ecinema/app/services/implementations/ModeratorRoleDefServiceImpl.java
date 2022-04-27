package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.ModeratorRoleDef;
import com.ecinema.app.repositories.ModeratorRoleDefRepository;
import com.ecinema.app.services.ModeratorRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModeratorRoleDefServiceImpl extends UserRoleDefServiceImpl<ModeratorRoleDef,
        ModeratorRoleDefRepository> implements ModeratorRoleDefService {

    public ModeratorRoleDefServiceImpl(ModeratorRoleDefRepository repository) {
        super(repository);
    }

}
