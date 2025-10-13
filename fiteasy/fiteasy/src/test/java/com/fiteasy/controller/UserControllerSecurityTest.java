package com.fiteasy.controller;

import com.fiteasy.model.User;
import com.fiteasy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "user1")
    public void testGetUserById_Success_WhenAccessingOwnData() throws Exception {
        // Arrange: Create a user and mock the service layer
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        given(userService.findByUsername("user1")).willReturn(Optional.of(user1));
        given(userService.getUserById(1L)).willReturn(Optional.of(user1));

        // Act & Assert: Attempt to access own profile should be successful (200 OK)
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1")
    public void testGetUserById_Forbidden_WhenAccessingOtherUserData() throws Exception {
        // Arrange: Create two users
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        // Mock the service to return the correct user based on username
        given(userService.findByUsername("user1")).willReturn(Optional.of(user1));
        // Mock getUserById to return user2 (so it exists, but security should block access)
        given(userService.getUserById(2L)).willReturn(Optional.of(user2));

        // Act & Assert: Attempt to access another user's profile should be forbidden (403)
        mockMvc.perform(get("/api/users/2"))
                .andExpect(status().isForbidden());
    }
}
