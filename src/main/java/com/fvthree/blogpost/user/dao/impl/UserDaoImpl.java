package com.fvthree.blogpost.user.dao.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fvthree.blogpost.rowmappers.RoleRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.role.entity.Role;
import com.fvthree.blogpost.user.entity.User;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class UserDaoImpl implements UserDao {

	private static final Logger log = LogManager.getLogger(UserDaoImpl.class);
	private static final String USER_ID = "user_id";
	private static final String USERNAME = "username";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	
	@Autowired
	private final JdbcClient jdbcClient;
	
	public UserDaoImpl(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	@Override
	public List<User> findAll(int size, int page) {
		return new ArrayList<>();
	}

	@Override
	public void delete(long id) {
	}

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final RowMapper<User> profileRowMapper = (resultSet, mapper) -> new User(
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
			LocalDateTime.parse(resultSet.getString("last_updated"), formatter));

	private static final String FIND_BY_ID = "select user_id, username, email, password, role, is_verified, account_level, profile_picture, cover_image, notification_preferences, gender, bio, location, last_login, date_created, last_updated from users where user_id =:user_id";

	@Override
	public User findById(long id) {
		return jdbcClient.sql(FIND_BY_ID).param("user_id",id).query(profileRowMapper).single();
	}

	private static final String INSERT_USER = "insert into users(username, email, password, role, date_created, last_updated, last_login) values (:username, :email, :password, :role, DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp))";

	@Override
	public long insert(User user) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcClient.sql(INSERT_USER)
					.param("username", user.username())
					.param("email", user.email())
					.param("role", user.role())
					.param("password", user.password())
					.update(keyHolder);
		} catch (Exception e) {
			log.info("insert exception = {} ", e.getMessage());
			throw new DBException(e.getMessage());
		}
		return Long.parseLong(String.valueOf(keyHolder.getKeys().get(USER_ID).toString()));
	}

	@Override
	public void update(long id, User entity) {
	}
	
	private static final String FIND_BY_EMAIL = "SELECT user_id, username, email, password, role, is_verified, account_level, profile_picture, cover_image, notification_preferences, gender, bio, location, last_login, date_created, last_updated FROM users WHERE email = :email";

	@Override
	public Optional<User> findByEmail(String email) {
		return jdbcClient.sql(FIND_BY_EMAIL).param(EMAIL, email).query(profileRowMapper).optional();
	}

	private static final String FIND_BY_EMAIL_PASSWORD = "select user_id, username, email, password, role, is_verified, account_level, profile_picture, cover_image, notification_preferences, gender, bio, location, last_login, date_created, last_updated  from users where email =:email and password =:password";

	@Override
	public Optional<User> findByEmailPassword(String email, String password) {
		return jdbcClient.sql(FIND_BY_EMAIL_PASSWORD).param(EMAIL, email).param(PASSWORD, password).query(profileRowMapper).optional();
	}

	private static final String INSERT_USER_ROLES = "insert into user_roles(user_id, role_id, date_created, last_updated) values (:user_id, :role_id, DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp))";

	@Override
	public void insertUserRoles(int userId, int rolesId) {
		jdbcClient.sql(INSERT_USER_ROLES).param("user_id",userId).param("role_id", rolesId).update();
	}

	private static final String FIND_ALL_USER_ROLES_BY_USER_ID = "select r.role_id, r.name, r.date_created, r.last_updated from user_roles ur, roles r, users u where ur.role_id = r.role_id and ur.user_id = u.user_id and u.email =:email";

	@Override
	public List<Role> getUserRolesByEmail(String email) {
		return jdbcClient.sql(FIND_ALL_USER_ROLES_BY_USER_ID).param(EMAIL, email).query(new RoleRowMapper()).list();
	}

	private static final String FIND_BY_USER_PASSWORD = "select user_id, username, email, password, role, is_verified, account_level, profile_picture, cover_image, notification_preferences, gender, bio, location, last_login, date_created, last_updated  from users where username =:username and password =:password";

	@Override
	public Optional<User> findByUsernamePassword(String username, String password) {
		return jdbcClient.sql(FIND_BY_USER_PASSWORD).param(USERNAME, username).param(PASSWORD, password).query(profileRowMapper).optional();
	}

	private static final String FIND_BY_USEREMAIL_PASSWORD = "select user_id, username, email, password, role, is_verified, account_level, profile_picture, cover_image, notification_preferences, gender, bio, location, last_login, date_created, last_updated  from users where (username =:login or email =:login) and password =:password";

	@Override
	public Optional<User> findByEmailOrUserAndPassword(String userid, String password) {
		return jdbcClient.sql(FIND_BY_USEREMAIL_PASSWORD).param("login",userid).param(PASSWORD, password).query(profileRowMapper).optional();
	}

	private static final String FIND_BY_USER_EMAIL = "select user_id, username, email, password, role, is_verified, account_level, profile_picture, cover_image, notification_preferences, gender, bio, location, last_login, date_created, last_updated  from users where (username =:userid  or email =:userid)";

	@Override
	public Optional<User> findByEmailOrUser(String userid) {
		return jdbcClient.sql(FIND_BY_USER_EMAIL).param("userid", userid).query(profileRowMapper).optional();
	}

	private static final String FIND_BY_USERNAME = "select user_id, username, email, password, role, is_verified, account_level, profile_picture, cover_image, notification_preferences, gender, bio, location, last_login, date_created, last_updated  from users where username =:username";

	@Override
	public Optional<User> findByUsername(String username) {
		return jdbcClient.sql(FIND_BY_USERNAME).param(USERNAME,username).query(profileRowMapper).optional();
	}

	private static final String UPDATE_LAST_LOGIN = "update users set last_login = DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp) where username =:username";

	@Override
	public void updateLastLogin(String username) {
		jdbcClient.sql(UPDATE_LAST_LOGIN).param(USERNAME, username).update();
	}
	
	private static final String INSERT_BLOCK_USER = "INSERT INTO blocked_users(user_id, blocked_user_id) VALUES (:user_id, :blocked_user_id)";

	@Override
	public void insertBlockUser(long userId, Long blockedId) {
		jdbcClient.sql(INSERT_BLOCK_USER).param(USER_ID, userId).param("blocked_user_id", blockedId).update();
	}
	
	private static final String DELETE_BLOCK_USER = "DELETE FROM blocked_users WHERE user_id =:user_id AND blocked_user_id =:blocked_user_id";

	@Override
	public void unBlockUser(long userId, Long blockedId) {
		jdbcClient.sql(DELETE_BLOCK_USER).param(USER_ID, userId).param("blocked_user_id", blockedId).update();
	}
	
	private static final String PROFILE_VIEWED = "INSERT INTO profile_views(user_id, viewed_user) VALUES (:user_id, :viewed_user)";

	@Override
	public void viewedUser(long userId, Long userViewed) {
		jdbcClient.sql(PROFILE_VIEWED).param(USER_ID, userId).param("viewed_user", userViewed).update();
	}
	
	private static final String FOLLOW_USER = "INSERT INTO followers(user_id, followed_user) VALUES (:user_id, :followed_user)";

	@Override
	public void followUser(long userId, Long followedId) {
		jdbcClient.sql(FOLLOW_USER).param(USER_ID, userId).param("followed_user", followedId).update();
	}
	
	private static final String UNFOLLOW_USER = "DELETE FROM followers WHERE user_id =:user_id AND followed_user =:followed_user";

	@Override
	public void unfollowUser(long userId, Long unfollowedId) {
		jdbcClient.sql(UNFOLLOW_USER).param("user_id", userId).param("followed_user", unfollowedId).update();
	}
}
