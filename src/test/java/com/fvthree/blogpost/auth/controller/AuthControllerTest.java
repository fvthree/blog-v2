package com.fvthree.blogpost.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fvthree.blogpost.auth.dto.LoginRequest;
import com.fvthree.blogpost.auth.service.AuthService;
import com.fvthree.blogpost.security.JwtTokenProvider;
import com.fvthree.blogpost.user.dao.impl.UserDao;
import com.fvthree.blogpost.user.entity.User;
import com.fvthree.blogpost.util.PasswordUtil;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
	
	@MockBean
	private UserDao userDao;
	
	@MockBean
	private AuthService authService;
	
	@MockBean
	private PasswordUtil passUtil;
	
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objMapper = new ObjectMapper()
			.findAndRegisterModules()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Test
	void testLogin() throws Exception {
		User created = createUser();
		
		when(authService.login(new LoginRequest(created.username(), created.password())))
			.thenReturn("tokenstring");
		
		String request = objMapper.writeValueAsString(created);
			
		this.mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").value("tokenstring"))
				.andExpect(jsonPath("$.tokenType").value("Bearer"));
	}
	
	@Test
	void testRegister() throws Exception {
		User created = createUser();
		
		when(authService.register(created.username(), created.email(), created.password()))
			.thenReturn(created);
		
		String request = objMapper.writeValueAsString(created);
			
		this.mockMvc.perform(post("/api/v1/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value("username"))
				.andExpect(jsonPath("$.email").value("email@email.com"));
	}
	
	@Test
	void testGetProfile() throws Exception {
		User created = createUser();
		
		when(authService.getProfile(1L)).thenReturn(created);
		
		when(jwtTokenProvider.getUsername(any(String.class))).thenReturn(created.username());
		when(userDao.findById(any(Long.class))).thenReturn(created);
		
		String profileId = "1";
		
		this.mockMvc.perform(get("/api/v1/users/profile/" + profileId)
				.header("Authorization",  "Bearer " + created.username()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("username"))
				.andExpect(jsonPath("$.email").value("email@email.com"));
	}
	
	@Test
	@WithMockUser(
			username = "fvthree",
			password = "123456",
			roles = {"DEV"})
	void testBlockUser() throws Exception {
		
		Map<String, String> request = new HashMap<>();
		request.put("userId", "1");
		request.put("blockedUserId", "1");
		
		when(authService.blockUser(any(Long.class), any(Long.class)))
			.thenReturn("Successfully blocked a user.");
		
		String rqst = objMapper.writeValueAsString(request);
			
		this.mockMvc.perform(post("/api/v1/users/block-user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(rqst))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("Successfully blocked a user."));
	}
	
	@Test
	@WithMockUser(
			username = "fvthree",
			password = "123456",
			roles = {"DEV"})
	void testUnBlockUser() throws Exception {
		
		Map<String, String> request = new HashMap<>();
		request.put("userId", "1");
		request.put("unBlockedUserId", "1");
		
		when(authService.unblockUser(any(Long.class), any(Long.class)))
			.thenReturn("Unblocked successfully!");
		
		String rqst = objMapper.writeValueAsString(request);
			
		this.mockMvc.perform(post("/api/v1/users/unblock-user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(rqst))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("Unblocked successfully!"));
	}
	
	@Test
	@WithMockUser(
			username = "fvthree",
			password = "123456",
			roles = {"DEV"})
	void testViewedUser() throws Exception {
		
		Map<String, String> request = new HashMap<>();
		request.put("userId", "1");
		request.put("viewedUser", "1");
		
		when(authService.profileViewed(any(Long.class), any(Long.class)))
			.thenReturn("Viewed a user!");
		
		String rqst = objMapper.writeValueAsString(request);
			
		this.mockMvc.perform(post("/api/v1/users/view-user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(rqst))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("Viewed a user!"));
	}
	
	private User createUser() {
		return new User(1L,"username", "email@email.com", "123456", null,
				null, false, null, null, null,
				null, null,null,null, LocalDateTime.now(), LocalDateTime.now());
	}

}
