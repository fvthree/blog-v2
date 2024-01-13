package com.fvthree.blogpost.auth.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fvthree.blogpost.auth.dto.LoginRequest;
import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.exceptions.RestAPIException;
import com.fvthree.blogpost.security.JwtTokenProvider;
import com.fvthree.blogpost.user.dao.impl.UserDao;
import com.fvthree.blogpost.user.entity.User;
import com.fvthree.blogpost.util.PasswordUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
	
	private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private PasswordUtil passwordUtil;
	

	public AuthServiceImpl(UserDao userDao, JwtTokenProvider jwtTokenProvider, PasswordUtil passwordUtil) {
		this.userDao = userDao;
		this.jwtTokenProvider = jwtTokenProvider;
		this.passwordUtil = passwordUtil;
	}

	@Override
	public String login(LoginRequest loginRequest) {
		logger.info("login email = {} ", loginRequest.username());
		try {
			String encodedPassword = passwordUtil.hashPassword(loginRequest.password());
			logger.info("password = {}", encodedPassword);
			User user = userDao.findByEmailOrUserAndPassword(loginRequest.username(), encodedPassword)
					.orElseThrow(()-> new RestAPIException("invalid credentials"));
			var userDetails = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
			userDao.updateLastLogin(user.username());
			SecurityContextHolder.getContext().setAuthentication(userDetails);
			return jwtTokenProvider.generateToken(userDetails);
		} catch (Exception e) {
			logger.error(" exception at login = {}", e.getMessage());
			throw new RestAPIException(e.getMessage());
		}
	}

	@Override
	public User register(String username, String email, String password) {
		User user = null;
		try {
			String hashPass = passwordUtil.hashPassword(password);
			user = User.builder()
				.username(username)
				.email(email)
				.role(1)
				.password(hashPass)
				.build();
			long insertedId = userDao.insert(user);
			logger.info("inserted Id = {}", insertedId);
			userDao.insertUserRoles((int) insertedId, 1);
			return user;
		} catch (Exception e) {
			logger.warn("exception at register = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
	}
	
	@Override
	public User getProfile(Long profileId) {
		try {
			return userDao.findById(profileId);
		} catch (Exception e) {
			logger.warn("exception at register = {}", e.getMessage());
			throw new RestAPIException(e.getMessage());
		}
	}

	@Override
	public String blockUser(Long userId, Long blockuserId) {
		try {
			userDao.insertBlockUser(userId, blockuserId);
		} catch(Exception e) {
			throw new DBException(e.getMessage());
		}
		return "Successfully blocked a user.";
	}

	@Override
	public String unblockUser(Long userId, Long blockuserId) {
		try {
			userDao.unBlockUser(userId, blockuserId);
		} catch (Exception e) {
			logger.error("exception in auth service layer = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
		return "Unblocked successfully!";
	}

	@Override
	public String profileViewed(Long userId, Long viewedUser) {
		try {
			userDao.viewedUser(userId, viewedUser);
		} catch(Exception e) {
			logger.error("error at authservice = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
		return "Viewed a user";
	}

	@Override
	public String follow(Long userId, Long followedId) {
		try {
			userDao.followUser(userId, followedId);
		} catch (Exception e) { 
			throw new DBException(e.getMessage());
		}
		return "Successfully followed a user";
	}

	@Override
	public String unfollow(Long userId, Long unfollowedId) {
		try {
			userDao.unfollowUser(userId, unfollowedId);
		} catch (Exception e) { 
			throw new DBException(e.getMessage());
		}
		return "Successfully unfollowed a user";
	}
}
