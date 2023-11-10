package com.fvthree.blogpost.auth.service;

import org.springframework.stereotype.Service;

import com.fvthree.blogpost.auth.dto.LoginRequest;
import com.fvthree.blogpost.user.entity.User;


@Service
public interface AuthService {
	String login(LoginRequest loginRequest);
	User register(String username, String email, String password);
	User getProfile(Long profileId);
	String blockUser(Long userId, Long blockuserId);
	String unblockUser(Long userId, Long blockuserId);
	String profileViewed(Long userId, Long viewedUser);
	String follow(Long userId, Long followedId);
	String unfollow(Long userId, Long unfollowedId);
}
