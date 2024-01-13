package com.fvthree.blogpost.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fvthree.blogpost.user.dao.impl.UserDao;
import com.fvthree.blogpost.user.dao.impl.UserDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.role.entity.Role;
import com.fvthree.blogpost.user.entity.User;

@JdbcTest(properties = {
		"spring.test.database.replace=none",
		"spring.datasource.url=jdbc:tc:postgresql:15.4-alpine:///db"
})
@ImportAutoConfiguration(JdbcClientAutoConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoImplTest {
	
	private static final Logger log = LogManager.getLogger(UserDaoImplTest.class);

	private UserDaoImpl userdao;
	
	@Autowired
	private JdbcClient jdbcClient;
	
	private GeneratedKeyHolder gkh;


	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private RowMapper<User> profileRowMapper = (resultSet, mapper) -> {
		return new User(
				resultSet.getLong("user_id"),
				resultSet.getString("username"),
				resultSet.getString("email"),
				resultSet.getString("password"),
				resultSet.getInt("role"),
				LocalDateTime.parse(resultSet.getString("last_login"), formatter),
				resultSet.getBoolean("is_verified"),
				resultSet.getString("account_level"),
				resultSet.getString("profile_picture"),
				resultSet.getString("cover_image"),
				resultSet.getString("notification_preferences"),
				resultSet.getString("gender"),
				resultSet.getString("bio"),
				resultSet.getString("location"),
				LocalDateTime.parse(resultSet.getString("date_created"), formatter),
				LocalDateTime.parse(resultSet.getString("date_updated"), formatter));
	};

	@BeforeEach
	public void setup() {
		userdao = new UserDaoImpl(jdbcClient);
		User user = User.builder().id(1L).username("username").email("123@gmail.com").password("123321").role(1).build();
		long id = userdao.insert(user);
		log.info(id);
	}

	@Test
	@Order(1)
	void testInsertUser() {
		User user = User.builder().id(1L).username("username123").email("123321@gmail.com").password("123321").role(1).build();
		
		long inserted = userdao.insert(user);
		
		assertEquals(inserted, 2);
	}
	
	@Test
	@Order(2)
	void testInsertShouldThrowException() {
		String username = "user", email = "user@gmail.com", password = "123456";
		User user = User.builder().username(username).email(email).password(password).build();
		assertThrows(DBException.class, () -> userdao.insert(user));
	}
	
	@Test
	@Order(3)
	void testFindById() {
		User registered = userdao.findById(5L);
		
		assertEquals(registered.username(), "username");
	}

	@Test
	@Order(4)
	void testFindByEmail() {
		Optional<User> registered = userdao.findByEmail("123@gmail.com");
		
		assertEquals(registered.get().email(), "123@gmail.com");
	}
	
	@Test
	@Order(5)
	void testFindByEmailAndPass() {

		Optional<User> registered = userdao.findByEmailPassword("123@gmail.com", "123321");
		
		User registrd = registered.get();
		
		assertEquals(registrd.email(), "123@gmail.com");
		assertEquals(registrd.password(), "123321");
	}
	
	@Test
	@Order(6)
	void testInsertUserRoles() {
		userdao.insertUserRoles(8, 1);
	}
	
	@Test
	@Order(7)
	void testGetUserRolesByEmail() {
		List<Role> roles = new ArrayList<>();
		
		Role role = new Role();
		role.setId(1L);
		role.setName("ROLE_USER");
		role.setCreatedDate(LocalDateTime.now());
		role.setLastUpdated(LocalDateTime.now());
		
		roles.add(role);
		
		List<Role> registered = userdao.getUserRolesByEmail("123@gmail.com");
		
		log.info(registered.toString());
	}
	
	@Test
	@Order(8)
	void testFindByUsernamePassword() {
		Optional<User> registered = userdao.findByUsernamePassword("username", "123321");
		
		User registrd = registered.get();
		
		assertEquals(registrd.email(), "123@gmail.com");
		assertEquals(registrd.password(), "123321");
	}
	
	@Test
	@Order(9)
	void testFindByEmailOrUserAndPassword() {
		Optional<User> registered = userdao.findByEmailOrUserAndPassword("username", "123321");
		
		User registrd = registered.get();
		
		assertEquals(registrd.email(), "123@gmail.com");
		assertEquals(registrd.password(), "123321");
	}

	@Test
	@Order(10)
	void testFindByEmailOrUser() {
		Optional<User> registered = userdao.findByEmailOrUser("username");
		
		User registrd = registered.get();
		
		assertEquals(registrd.email(), "123@gmail.com");
		assertEquals(registrd.password(), "123321");
	}
	
	@Test
	@Order(11)
	void testFindByUsername() {
		Optional<User> registered = userdao.findByUsername("username");
		
		User registrd = registered.get();
		
		assertEquals(registrd.email(), "123@gmail.com");
		assertEquals(registrd.password(), "123321");
	}
	
	@Test
	@Order(12)
	void testupdateLastLogin() {
		userdao.updateLastLogin("username");
	}
	
	@Test
	@Order(13)
	void testinsertBlockUser() {
		userdao.insertBlockUser(15L, 15L);
	}

	@Test
	@Order(14)
	void testunBlockUser() {
		userdao.unBlockUser(16L, 16L);
	}

	@Test
	@Order(15)
	void testviewedUser() {
		userdao.viewedUser(17L, 17L);
	}

	@Test
	@Order(16)
	void testfollowUser() {
		userdao.followUser(18L, 18L);
	}

	@Test
	@Order(17)
	void testunfollowUser() {
		userdao.unfollowUser(19L, 19L);
	}
	
	@Test
	@Order(18)
	void testReturnEmptyList() {
		List<User> users = userdao.findAll(0,0);
		
		assertEquals(users.size(), 0);
	}
}
