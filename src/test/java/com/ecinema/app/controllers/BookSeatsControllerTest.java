package com.ecinema.app.controllers;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.configs.InitializationConfig;
import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.TicketType;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.GenericListForm;
import com.ecinema.app.domain.forms.SeatBookingsForm;
import com.ecinema.app.domain.objects.SeatBooking;
import com.ecinema.app.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Tests {@link BookSeatsController} */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookSeatsControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ScreeningService screeningService;

    @MockBean
    private ScreeningSeatService screeningSeatService;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private InitializationConfig config;

    private MockMvc mockMvc;

    /** Sets up the {@link MockMvc} */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER"})
    void showChooseSeatsToBookPage()
        throws Exception {
        setUpMockUser();
        ScreeningDto screeningDto = new ScreeningDto();
        given(screeningService.findById(anyLong())).willReturn(screeningDto);
        Map<Letter, Set<ScreeningSeatDto>> mapOfScreeningSeats = new EnumMap<>(Letter.class);
        given(screeningSeatService.findScreeningSeatMapByScreeningWithId(anyLong()))
                .willReturn(mapOfScreeningSeats);
        mockMvc.perform(get("/choose-seats-to-book")
                      .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(result -> model().attribute("screening", screeningDto))
                .andExpect(result -> model().attribute("mapOfScreeningSeats", mapOfScreeningSeats))
                .andExpect(result -> model().attributeExists("seatIdsForm"));
    }

    @Test
    @WithAnonymousUser
    void failToShowChooseSeatsToBookPage()
            throws Exception {
        mockMvc.perform(get("/choose-seats-to-book")
                                .param("id", String.valueOf(1L)))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER"})
    void showBookSeatsPage()
            throws Exception {
        setUpMockUser();
        ScreeningDto screeningDto = new ScreeningDto();
        given(screeningService.findById(1L)).willReturn(screeningDto);
        GenericListForm<Long> seatIdsForm = new GenericListForm<>();
        seatIdsForm.setList(List.of(2L));
        mockMvc.perform(get("/book-seats")
                                .param("id", String.valueOf(1L))
                                .flashAttr("seatIdsForm", seatIdsForm))
                .andExpect(status().isOk())
                .andExpect(result -> model().attribute("screening", screeningDto))
                .andExpect(result -> model().attribute(
                        "seatBookingsForm",
                        hasProperty("seatBookings",
                                    equalTo(List.of(
                                            new SeatBooking(2L, TicketType.ADULT))))));
    }

    @Test
    @WithAnonymousUser
    void failToShowBookSeatsPage()
            throws Exception {
        mockMvc.perform(get("/book-seats")
                                .param("id", String.valueOf(2L))
                                .flashAttr("seatIdsForm", new GenericListForm<Long>()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER"})
    void bookSeats()
        throws Exception {
        setUpMockUser();
        doNothing().when(customerService).bookTickets(
                ArgumentMatchers.any(SeatBookingsForm.class));
        mockMvc.perform(post("/book-seats")
                                .param("id", String.valueOf(1L))
                                .flashAttr("seatBookingsForm", new SeatBookingsForm()))
                .andExpect(redirectedUrlPattern("/message-page**"));
    }

    void setUpMockUser() {
        given(securityContext.findIdOfLoggedInUser()).willReturn(1L);
        UserDto userDto = new UserDto();
        userDto.getUserAuthorities().add(UserAuthority.CUSTOMER);
        given(userService.findById(1L)).willReturn(userDto);
    }

}