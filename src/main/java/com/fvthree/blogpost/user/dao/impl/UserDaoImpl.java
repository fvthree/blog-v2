package com.fvthree.blogpost.user.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.role.dao.impl.RoleRowMapper;
import com.fvthree.blogpost.role.entity.Role;
import com.fvthree.blogpost.rowmappers.UserRowMapper;
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
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public UserDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final String FIND_BY_ID = "select user_id, username, email, password, role, is_verified, account_level, profile_picture, cover_image, notification_preferences, gender, bio, location, last_login, date_created, last_updated from users where user_id = :user_id";
	private static final String FIND_BY_USERNAME = "select user_id, username, email, password, date_created, last_updated from users where username = :username";

	private static final String FIND_BY_EMAIL_PASSWORD = "select user_id, email, password, date_created, last_updated from users where email = :email and password = :password";
	private static final String FIND_BY_USER_PASSWORD = "select user_id, email, password, date_created, last_updated from users where username = :username and password = :password";
	private static final String FIND_BY_USEREMAIL_PASSWORD = "select user_id, username, email, password, date_created, last_updated from users where (username = :userid  or email = :userid) and password = :password";
	private static final String FIND_BY_USER_EMAIL = "select user_id, username, email, password, date_created, last_updated from users where (username = :userid  or email = :userid)";
	private static final String FIND_ALL_USER_ROLES_BY_USER_ID = "select r.role_id, r.name, r.date_created, r.last_updated from user_roles ur, roles r, users u where ur.role_id = r.role_id and ur.user_id = u.user_id and u.email = :email";

	private static final String INSERT_USER = "insert into users(username, email, password, role, date_created, last_updated) values (:username, :email, :password, :role, DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp))";
	private static final String INSERT_USER_ROLES = "insert into user_roles(user_id, role_id, date_created, last_updated) values (:user_id, :role_id, DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp))";
	
	private static final String UPDATE_LAST_LOGIN = "update users set last_login = DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp) where username = :username";
	
	@Override
	public List<User> findAll(int size, int page) {
		return new ArrayList<>();
	}

	@Override
	public void delete(long id) {
	}

	@Override
	public User findById(long id) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue(USER_ID, id);
		return jdbcTemplate.queryForObject(FIND_BY_ID, sqlParameterSource, new UserProfileRowMapper());
	}

	@Override
	public long insert(User user) {
		GeneratedKeyHolder gkh = new GeneratedKeyHolder();
		var paramMap = new MapSqlParameterSource();
		paramMap.addValue(USERNAME, user.getUsername());
		paramMap.addValue(EMAIL, user.getEmail());
		paramMap.addValue("role", user.getRole());
		paramMap.addValue(PASSWORD, user.getPassword());
		try {
			jdbcTemplate.update(INSERT_USER, paramMap, gkh);
		} catch (Exception e) {
			log.info("insert exception = {} ", e.getMessage());
			throw new DBException(e.getMessage());
		}
		return Long.parseLong(String.valueOf(gkh.getKeys().get(USER_ID))); 
	}

	@Override
	public void update(long id, User entity) {
	}
	
	private static final String FIND_BY_EMAIL = "select user_id, username, email, password, date_created, last_updated from users where email = :email";

	@Override
	public Optional<User> findByEmail(String email) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue(EMAIL, email);
		var user = jdbcTemplate.queryForObject(FIND_BY_EMAIL, sqlParameterSource, new UserRowMapper());
		return Optional.ofNullable(user);
	}
	
	
	
	@Override
	public Optional<User> findByEmailPassword(String email, String password) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue(EMAIL, email);
		sqlParameterSource.addValue(PASSWORD, password);
		var user = jdbcTemplate.queryForObject(FIND_BY_EMAIL_PASSWORD, sqlParameterSource, new UserRowMapper());
		return Optional.ofNullable(user);
	}
	

	@Override
	public void insertUserRoles(int userId, int rolesId) {
		var mapPs = new MapSqlParameterSource(USER_ID, userId);
		mapPs.addValue("role_id", rolesId);
		jdbcTemplate.update(INSERT_USER_ROLES, mapPs);
	}

	@Override
	public List<Role> getUserRolesByEmail(String email) {
		var paramSource = new MapSqlParameterSource();
		paramSource.addValue(EMAIL, email);
		return jdbcTemplate.query(FIND_ALL_USER_ROLES_BY_USER_ID, paramSource, new RoleRowMapper());
	}

	@Override
	public Optional<User> findByUsernamePassword(String username, String password) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue(USERNAME, username);
		sqlParameterSource.addValue(PASSWORD, password);
		var user = jdbcTemplate.queryForObject(FIND_BY_USER_PASSWORD, sqlParameterSource, new UserRowMapper());
		return Optional.ofNullable(user);
	}

	@Override
	public Optional<User> findByEmailOrUserAndPassword(String userid, String password) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userid", userid);
		sqlParameterSource.addValue(PASSWORD, password);
		var user = jdbcTemplate.queryForObject(FIND_BY_USEREMAIL_PASSWORD, sqlParameterSource, new UserRowMapper());
		return Optional.ofNullable(user);
	}

	@Override
	public Optional<User> findByEmailOrUser(String userid) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("userid", userid);
		var user = jdbcTemplate.queryForObject(FIND_BY_USER_EMAIL, sqlParameterSource, new UserRowMapper());
		return Optional.ofNullable(user);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue(USERNAME, username);
		var user = jdbcTemplate.queryForObject(FIND_BY_USERNAME, sqlParameterSource, new UserRowMapper());
		return Optional.ofNullable(user);
	}

	@Override
	public void updateLastLogin(String username) {
		var mapParamSource = new MapSqlParameterSource();
		mapParamSource.addValue(USERNAME, username);
		jdbcTemplate.update(UPDATE_LAST_LOGIN, mapParamSource);
	}
	
	private static final String INSERT_BLOCK_USER = ""
			+ "insert into blocked_users(user_id, blocked_user_id) values (:user_id, :blocked_user_id)";

	@Override
	public void insertBlockUser(long userId, Long blockedId) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue(USER_ID, userId);
		mapps.addValue("blocked_user_id", blockedId);
		jdbcTemplate.update(INSERT_BLOCK_USER, mapps);
	}
	
	private static final String DELETE_BLOCK_USER = ""
			+ "delete from blocked_users where user_id = :user_id AND blocked_user_id = :blocked_user_id)";

	@Override
	public void unBlockUser(long userId, Long blockedId) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue(USER_ID, userId);
		mapps.addValue("blocked_user_id", blockedId);
		jdbcTemplate.update(DELETE_BLOCK_USER, mapps);
	}
	
	private static final String PROFILE_VIEWED = ""
			+ "insert into profile_views(user_id, viewed_user) values (:user_id, :viewed_user)";

	@Override
	public void viewedUser(long userId, Long userViewed) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue(USER_ID, userId);
		mapps.addValue("viewed_users", userViewed);
		jdbcTemplate.update(PROFILE_VIEWED, mapps);
	}
	
	private static final String FOLLOW_USER = ""
			+ "INSERT INTO followers(user_id, followed_user) VALUES (:user_id, :followed_user)";

	@Override
	public void followUser(long userId, Long followedId) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue(USER_ID, userId);
		mapps.addValue("followed_user", followedId);
		jdbcTemplate.update(FOLLOW_USER, mapps);
	}
	
	private static final String UNFOLLOW_USER = ""
			+ "DELETE FROM followers where user_id = :user_id AND followed_user = :followed_user";

	@Override
	public void unfollowUser(long userId, Long unfollowedId) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue("userId", userId);
		mapps.addValue("unfollowedI", unfollowedId);
		jdbcTemplate.update(UNFOLLOW_USER, mapps);
	}
}
