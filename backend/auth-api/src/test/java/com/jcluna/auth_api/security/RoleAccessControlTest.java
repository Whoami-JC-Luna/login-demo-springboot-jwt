package com.jcluna.auth_api.security;


import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.model.enums.Role;
import com.jcluna.auth_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoleAccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userToken;
    private String adminToken;
    private String guestToken;

    @BeforeEach
    void setUp() {

            userRepository.deleteAll();

            User userUser = new User();
            userUser.setEmail("user@test.com");
            userUser.setUserName("user");
            userUser.setPassword(passwordEncoder.encode("password"));
            userUser.setRole(Role.ROLE_USER);
            userRepository.save(userUser);

            User adminUser = new User();
            adminUser.setEmail("admin@test.com");
            adminUser.setUserName("admin");
            adminUser.setPassword(passwordEncoder.encode("password"));
            adminUser.setRole(Role.ROLE_ADMIN);
            userRepository.save(adminUser);

            User guestUser = new User();
            guestUser.setEmail("guest@test.com");
            guestUser.setUserName("guest");
            guestUser.setPassword(passwordEncoder.encode("password"));
            guestUser.setRole(Role.ROLE_GUEST);
            userRepository.save(guestUser);

            userToken = jwtService.generateToken(userUser);
            adminToken = jwtService.generateToken(adminUser);
            guestToken = jwtService.generateToken(guestUser);
    }


    // Test 1
    @Test
    void guest_canAccess_publicEndpoints() throws Exception {
        mockMvc.perform(get("/quotes")
                        .header("Authorization", "Bearer " + guestToken))
                .andExpect(status().isOk());
    }


    // Test 2
    @Test
    void guest_cannotAccess_protectedEndpoints() throws Exception {
        mockMvc.perform(get("/quotes/me")
                        .header("Authorization", "Bearer " + guestToken))
                .andExpect(status().isForbidden());
    }

    // Test 3
    @Test
    void user_canAccess_protectedEndpoints() throws Exception {
        mockMvc.perform(get("/quotes/me")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    // Test 4
    @Test
    void admin_canAccess_protectedEndpoints() throws Exception {
        mockMvc.perform(get("/quotes/me")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    // Test 5
    @Test
    void noToken_cannotAccess_protectedEndpoints() throws Exception {
        mockMvc.perform(get("/quotes/me"))
                .andExpect(status().isUnauthorized());
    }
}
