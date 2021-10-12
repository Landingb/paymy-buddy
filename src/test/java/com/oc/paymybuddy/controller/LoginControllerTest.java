package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.service.interfaces.SecurityService;
import com.oc.paymybuddy.service.interfaces.UserService;

import com.oc.paymybuddy.testconfig.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoginController.class)
@Import(SpringWebTestConfig.class)

class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userServiceMock;
    @MockBean
    private SecurityService securityServiceMock;

    @Test
    void givenAnonymous_shouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isFound()); //TODO: WHY 302 ????
    }

    @WithMockUser
    //annotation to test spring security with mock user : here we have default values "user"/"password"/"USER_ROLE"
    @Test
    void givenMockUser_shouldSucceedWith200() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @WithUserDetails("user@company.com") //user from SpringSecurityWebTestConfig.class
    @Test
    void givenWithUserDetails_shouldSucceedWith200() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

}