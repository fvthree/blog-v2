package com.fvthree.blogpost.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.exceptions.RestAPIException;
import com.fvthree.blogpost.user.dao.impl.UserDao;
import com.fvthree.blogpost.user.entity.User;
import com.fvthree.blogpost.util.PasswordUtil;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {
	
	private static final Logger logger  =  LogManager.getLogger(AuthServiceTest.class);

	@InjectMocks
	private AuthServiceImpl authService;
	
	@Mock
	private PasswordUtil passwordUtil;
	
	@Mock
	private UserDao userDao;
	
	@Test
	void testRegisterShouldSuccess() {
		String username = "user", email = "user@gmail.com", password = "123456";
		doNothing().when(userDao).insertUserRoles(any(Integer.class), any(Integer.class));
		when(userDao.insert(any(User.class))).thenReturn(1L);
		User registered  =  authService.register(username, email, password);
		assertEquals(registered.username(), username);
		assertEquals(registered.email(), email);
	}
	
	@Test
	void testRegisterShouldThrowDBException() {
		String username = "user", email = "user@gmail.com", password = "123456";
		doNothing().when(userDao).insertUserRoles(any(Integer.class), any(Integer.class));
		when(userDao.insert(any(User.class))).thenThrow(new DBException("duplicate value"));
		assertThrows(DBException.class, () -> authService.register(username, email, password));
	}
	
	@Test
	void testGetProfileShouldSuccess() {
		String username = "user", email = "user@gmail.com", password = "123456";
		User user = User.builder().username(username).email(email).password(password).build();
		when(userDao.findById(any(Long.class))).thenReturn(user);
		User registered  =  authService.getProfile(1L);
		assertEquals(registered.username(), username);
		assertEquals(registered.email(), email);
	}
	
	@Test
	void testGetProfileRestAPIException() {
		when(userDao.findById(any(Long.class))).thenThrow(new DBException("Cannot find"));
		assertThrows(RestAPIException.class, () -> authService.getProfile(any(Long.class)));
	}
	
	@Test
	void testBlockUserShouldSuccess() {
		String msg = "Successfully blocked a user.";
		doNothing().when(userDao).insertBlockUser(any(Long.class), any(Long.class));
		String successMsg  =  authService.blockUser(any(Long.class), any(Long.class));
		assertEquals(successMsg, msg);
	}
	
	@Test
	void testUnBlockUserShouldThrowDBException() {
		when(authService.blockUser(any(Long.class), any(Long.class))).thenThrow(new DBException("Invalid constraints"));
		assertThrows(DBException.class, () -> authService.blockUser(any(Long.class), any(Long.class)));
	}
	
	@Test
	void testUnblockUserShouldSuccess() {
		String msg = "Unblocked successfully!";
		doNothing().when(userDao).unBlockUser(any(Long.class), any(Long.class));
		String successMsg  =  authService.unblockUser(any(Long.class), any(Long.class));
		assertEquals(successMsg, msg);
	}
	
	@Test
	void testUnblockwUserShouldThrowDBException() {
		when(authService.unblockUser(any(Long.class), any(Long.class))).thenThrow(new DBException("Invalid constraints"));
		assertThrows(DBException.class, () -> authService.unblockUser(any(Long.class), any(Long.class)));
	}
	
	@Test
	void testFollowUserShouldSuccess() {
		String msg = "Successfully followed a user";
		doNothing().when(userDao).followUser(any(Long.class), any(Long.class));
		String successMsg = authService.follow(any(Long.class), any(Long.class));
		assertEquals(successMsg, msg);
	}
	
	@Test
	void testFollowkUserShouldThrowDBException() {
		when(authService.follow(any(Long.class), any(Long.class))).thenThrow(new DBException("Invalid constraints"));
		assertThrows(DBException.class, () -> authService.follow(any(Long.class), any(Long.class)));
	}
	
	@Test
	void testUnFollowUserShouldSuccess() {
		String msg = "Successfully unfollowed a user";
		doNothing().when(userDao).unfollowUser(any(Long.class), any(Long.class));
		String successMsg = authService.unfollow(any(Long.class), any(Long.class));
		assertEquals(successMsg, msg);
	}
	
	@Test
	void testUnFollowkUserShouldThrowDBException() {
		when(authService.unfollow(any(Long.class), any(Long.class))).thenThrow(new DBException("Invalid constraints"));
		assertThrows(DBException.class, () -> authService.unfollow(any(Long.class), any(Long.class)));
	}
	
	@Test
	void testProfileViewedShouldSuccess() {
		String msg = "Viewed a user";
		doNothing().when(userDao).viewedUser(any(Long.class), any(Long.class));
		String successMsg = authService.profileViewed(any(Long.class), any(Long.class));
		assertEquals(successMsg, msg);
	}
	
	@Test
	void testProfileViewedShouldThrowDBException() {
		when(authService.profileViewed(any(Long.class), any(Long.class))).thenThrow(new DBException("Invalid constraints"));
		assertThrows(DBException.class, () -> authService.profileViewed(any(Long.class), any(Long.class)));
	}
}
