package com.fvthree.blogpost.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.ActiveProfiles;

import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.role.entity.Role;
import com.fvthree.blogpost.rowmappers.RoleRowMapper;
import com.fvthree.blogpost.rowmappers.UserRowMapper;
import com.fvthree.blogpost.user.dao.impl.UserDaoImpl;
import com.fvthree.blogpost.user.dao.impl.UserProfileRowMapper;
import com.fvthree.blogpost.user.entity.User;


@ActiveProfiles("test")
@SpringBootTest
class UserDaoImplTest {
	
	private static final Logger log = LogManager.getLogger(UserDaoImplTest.class);
	
	@InjectMocks
	private UserDaoImpl userdao;
	
	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Mock
	private GeneratedKeyHolder gkh;
	
	@Test
	void testInsertUser() {
		User user = User.builder().id(1L).username("username").email("123@gmail.com").password("123321").build();

		Mockito.when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class), Mockito.any(GeneratedKeyHolder.class)))
		.thenAnswer(new Answer() {
		    public Object answer(InvocationOnMock invocation) {
		        var args = invocation.getArguments();
		        var keyMap = new HashMap<String, Object>();
		        keyMap.put("user_id", 1);
		        var keyList = ((GeneratedKeyHolder)args[2]).getKeyList();
		        keyList.add(keyMap);
		        log.info(keyList);
		        return 1;
		    }
		}).thenReturn(1);
		
		long inserted = userdao.insert(user);
		
		assertEquals(inserted, 1);
	}
	
	@Test
	void testInsertShouldThrowException() {
		String username = "user", email = "user@gmail.com", password = "123456";
		User user = User.builder().username(username).email(email).password(password).build();
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class), Mockito.any(GeneratedKeyHolder.class)))
			.thenThrow(new DBException());
		assertThrows(DBException.class, () -> userdao.insert(user));
	}
	
	@Test
	void testFindById() {
		User user = User.builder().id(1L).username("username").build();
		
		when(jdbcTemplate.queryForObject(
				Mockito.anyString(), 
				Mockito.any(MapSqlParameterSource.class),
                Mockito.any(UserProfileRowMapper.class)))
		.thenReturn(user);
		
		User registered = userdao.findById(1L);
		
		assertEquals(registered.getUsername(), "username");
	}

	@Test
	void testFindByEmail() {
		User user = User.builder().id(1L).username("username").email("123@gmail.com").build();
		
		when(jdbcTemplate.queryForObject(
				Mockito.anyString(), 
				Mockito.any(MapSqlParameterSource.class),
                Mockito.any(UserRowMapper.class)))
		.thenReturn(user);
		
		Optional<User> registered = userdao.findByEmail(user.getEmail());
		
		assertEquals(registered.get().getEmail(), "123@gmail.com");
	}
	
	@Test
	void testFindByEmailAndPass() {
		User user = User.builder().id(1L).username("username").email("123@gmail.com").password("123123").build();
		
		when(jdbcTemplate.queryForObject(
				Mockito.anyString(), 
				Mockito.any(MapSqlParameterSource.class),
                Mockito.any(UserRowMapper.class)))
		.thenReturn(user);
		
		Optional<User> registered = userdao.findByEmailPassword(user.getEmail(), user.getPassword());
		
		User registrd = registered.get();
		
		assertEquals(registrd.getEmail(), "123@gmail.com");
		assertEquals(registrd.getPassword(), "123123");
	}
	
	@Test
	void testInsertUserRoles() {
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class), Mockito.any(GeneratedKeyHolder.class)))
			.thenReturn(1);
		userdao.insertUserRoles(1, 1);
	}
	
	@Test
	void testGetUserRolesByEmail() {
		List<Role> roles = new ArrayList<>();
		
		Role role = new Role();
		role.setId(1L);
		role.setName("ROLE_USER");
		role.setCreatedDate(LocalDateTime.now());
		role.setLastUpdated(LocalDateTime.now());
		
		roles.add(role);
		
		when(jdbcTemplate.query(
				Mockito.anyString(), 
				Mockito.any(MapSqlParameterSource.class),
                Mockito.any(RoleRowMapper.class)))
		.thenReturn(roles);
		
		List<Role> registered = userdao.getUserRolesByEmail("123@gmail.com");
		
		log.info(registered.toString());
	}
	
	@Test
	void testFindByUsernamePassword() {
		User user = User.builder().id(1L).username("username").email("123@gmail.com").password("123123").build();
		
		when(jdbcTemplate.queryForObject(
				Mockito.anyString(), 
				Mockito.any(MapSqlParameterSource.class),
                Mockito.any(UserRowMapper.class)))
		.thenReturn(user);
		
		Optional<User> registered = userdao.findByUsernamePassword(user.getUsername(), user.getPassword());
		
		User registrd = registered.get();
		
		assertEquals(registrd.getEmail(), "123@gmail.com");
		assertEquals(registrd.getPassword(), "123123");
	}
	
	@Test
	void testFindByEmailOrUserAndPassword() {
		User user = User.builder().id(1L).username("username").email("123@gmail.com").password("123123").build();
		
		when(jdbcTemplate.queryForObject(
				Mockito.anyString(), 
				Mockito.any(MapSqlParameterSource.class),
                Mockito.any(UserRowMapper.class)))
		.thenReturn(user);
		
		Optional<User> registered = userdao.findByEmailOrUserAndPassword(user.getUsername(), user.getPassword());
		
		User registrd = registered.get();
		
		assertEquals(registrd.getEmail(), "123@gmail.com");
		assertEquals(registrd.getPassword(), "123123");
	}

	@Test
	void testFindByEmailOrUser() {
		User user = User.builder().id(1L).username("username").email("123@gmail.com").password("123123").build();
		
		when(jdbcTemplate.queryForObject(
				Mockito.anyString(), 
				Mockito.any(MapSqlParameterSource.class),
                Mockito.any(UserRowMapper.class)))
		.thenReturn(user);
		
		Optional<User> registered = userdao.findByEmailOrUser(user.getUsername());
		
		User registrd = registered.get();
		
		assertEquals(registrd.getEmail(), "123@gmail.com");
		assertEquals(registrd.getPassword(), "123123");
	}
	
	@Test
	void testFindByUsername() {
		User user = User.builder().id(1L).username("username").email("123@gmail.com").password("123123").build();
		
		when(jdbcTemplate.queryForObject(
				Mockito.anyString(), 
				Mockito.any(MapSqlParameterSource.class),
                Mockito.any(UserRowMapper.class)))
		.thenReturn(user);
		
		Optional<User> registered = userdao.findByUsername(user.getUsername());
		
		User registrd = registered.get();
		
		assertEquals(registrd.getEmail(), "123@gmail.com");
		assertEquals(registrd.getPassword(), "123123");
	}
	
	@Test
	void testupdateLastLogin() {
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
			.thenReturn(1);
		userdao.updateLastLogin("username");
	}
	
	@Test
	void testinsertBlockUser() {
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
			.thenReturn(1);
		userdao.insertBlockUser(1L, 1L);
	}

	@Test
	void testunBlockUser() {
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
			.thenReturn(1);
		userdao.unBlockUser(1L, 1L);
	}

	@Test
	void testviewedUser() {
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
			.thenReturn(1);
		userdao.viewedUser(1L, 1L);
	}

	@Test
	void testfollowUser() {
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
			.thenReturn(1);
		userdao.followUser(1L, 1L);
	}

	@Test
	void testunfollowUser() {
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
			.thenReturn(1);
		userdao.unfollowUser(1L, 1L);
	}
	
	@Test
	void testReturnEmptyList() {
		List<User> users = userdao.findAll(0,0);
		
		assertEquals(users.size(), 0);
	}
}
