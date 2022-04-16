package com.ecinema.app.user;

import com.ecinema.app.abstraction.AbstractServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends AbstractServiceImpl<Admin, AdminRepository> implements AdminService {

    public AdminServiceImpl(AdminRepository repository) {
        super(repository);
    }

}
