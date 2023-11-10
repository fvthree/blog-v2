package com.fvthree.blogpost.user.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fvthree.blogpost.base.dao.BaseDao;
import com.fvthree.blogpost.role.entity.Role;
import com.fvthree.blogpost.user.entity.User;

@Component
public interface UserDao extends BaseDao<User> {
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernamePassword(String username, String password);
	Optional<User> findByEmailPassword(String email, String password);
	Optional<User> findByEmailOrUserAndPassword(String userid, String password);
	Optional<User> findByEmailOrUser(String userid);

	void insertUserRoles(int userId, int rolesId);
	List<Role> getUserRolesByEmail(String email);
	
	void updateLastLogin(String username);
	
	void insertBlockUser(long userId, Long blockedId);
	void unBlockUser(long userId, Long blockedId);
	void viewedUser(long userId, Long userViewed);
	void followUser(long userId, Long followedId);
	void unfollowUser(long userId, Long unfollowedId);
}
