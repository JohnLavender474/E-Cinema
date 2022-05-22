package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.AdminDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    private AdminService adminService;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        adminService = new AdminService(adminRepository);
    }

    @Test
    void deleteAdminAndCascade() {
        // given
        User user = new User();
        user.setId(1L);
        userRepository.save(user);
        Admin admin = new Admin();
        admin.setId(2L);
        admin.setUser(user);
        user.getUserAuthorities().put(UserAuthority.ADMIN, admin);
        adminRepository.save(admin);
        assertNotNull(admin.getUser());
        assertNotNull(user.getUserAuthorities().get(UserAuthority.ADMIN));
        // when
        adminService.delete(admin);
        // then
        assertNull(admin.getUser());
        assertNull(user.getUserAuthorities().get(UserAuthority.ADMIN));
    }

    @Test
    void adminRoleDefDto() {
        // given
        User user = new User();
        user.setId(1L);
        Admin admin = new Admin();
        admin.setId(1L);
        user.getUserAuthorities().put(UserAuthority.ADMIN, admin);
        admin.setUser(user);
        adminRepository.save(admin);
        given(adminRepository.findById(1L))
                .willReturn(Optional.of(admin));
        // when
        AdminDto adminDto = adminService.convertToDto(1L);
        // then
        assertEquals(admin.getId(), adminDto.getId());
    }

}