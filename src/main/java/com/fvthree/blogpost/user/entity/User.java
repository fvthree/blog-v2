package com.fvthree.blogpost.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record User(Long id, String username, String email, String password, Integer role,
		LocalDateTime lastLogin, boolean isVerified, String accountLevel, String profilePicture, String coverImage,
		String notificationPreferences, String gender, String bio, String location, LocalDateTime dateCreated, LocalDateTime lastUpdated)
{
	record UserProfile(List<Integer> profileViewers, List<Integer> followers, List<Integer> blockedUsers,
			 List<Integer> posts, List<Integer> likedPosts){}
}
