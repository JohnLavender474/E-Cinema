package com.ecinema.app.services;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.repositories.AdminRoleDefRepository;
import com.ecinema.app.repositories.AdminTraineeRoleDefRepository;
import com.ecinema.app.services.implementations.AdminRoleDefServiceImpl;
import com.ecinema.app.services.implementations.AdminTraineeRoleDefServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminTraineeRoleDefServiceTest {

    @InjectMocks
    private AdminTraineeRoleDefServiceImpl adminTraineeRoleDefService;

    @Mock
    private AdminTraineeRoleDefRepository adminTraineeRoleDefRepository;

    @InjectMocks
    private AdminRoleDefServiceImpl adminRoleDefService;

    @Mock
    private AdminRoleDefRepository adminRoleDefRepository;

    @Test
    void testFindAllByMentor() {
        // given
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDefService.save(adminRoleDef);
        AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
        adminTraineeRoleDef.setMentor(adminRoleDef);
        adminRoleDef.getTrainees().add(adminTraineeRoleDef);
        adminTraineeRoleDefService.save(adminTraineeRoleDef);
        List<AdminTraineeRoleDef> trainees = new ArrayList<>();
        trainees.add(adminTraineeRoleDef);
        given(adminTraineeRoleDefRepository.findAllByMentor(adminRoleDef))
                .willReturn(trainees);
        // when
        List<AdminTraineeRoleDef> testTrainees = adminTraineeRoleDefService
                .findAllByMentor(adminRoleDef);
        // then
        assertEquals(trainees, testTrainees);
        verify(adminRoleDefRepository, times(1))
                .save(adminRoleDef);
        verify(adminTraineeRoleDefRepository, times(1))
                .save(adminTraineeRoleDef);
    }

    @Test
    void testFindAllByPercentageTrainingModulesCompletedLessThanEqual() {
        // given
        List<AdminTraineeRoleDef> trainees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
            adminTraineeRoleDef.setPercentageTrainingModulesCompleted(i * 10);
            adminTraineeRoleDefService.save(adminTraineeRoleDef);
            trainees.add(adminTraineeRoleDef);
        }
        List<AdminTraineeRoleDef> control = trainees
                .stream().filter(trainee -> trainee.getPercentageTrainingModulesCompleted() >= 50)
                .collect(Collectors.toList());
        given(adminTraineeRoleDefRepository.findAllByPercentageTrainingModulesCompletedGreaterThanEqual(50))
                .willReturn(control);
        // when
        List<AdminTraineeRoleDef> test = adminTraineeRoleDefService
                .findAllByPercentageTrainingModulesCompletedGreaterThanEqual(50);
        // then
        assertEquals(control, test);
    }

    @Test
    void testFindAllByPercentageTrainingModulesCompletedGreaterThanEqual() {
        // given
        List<AdminTraineeRoleDef> trainees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
            adminTraineeRoleDef.setPercentageTrainingModulesCompleted(i * 10);
            adminTraineeRoleDefService.save(adminTraineeRoleDef);
            trainees.add(adminTraineeRoleDef);
        }
        List<AdminTraineeRoleDef> control = trainees
                .stream().filter(trainee -> trainee.getPercentageTrainingModulesCompleted() < 70)
                .collect(Collectors.toList());
        given(adminTraineeRoleDefRepository.findAllByPercentageTrainingModulesCompletedLessThanEqual(60))
                .willReturn(control);
        // when
        List<AdminTraineeRoleDef> test = adminTraineeRoleDefService
                .findAllByPercentageTrainingModulesCompletedLessThanEqual(60);
        // then
        assertEquals(control, test);
    }

}