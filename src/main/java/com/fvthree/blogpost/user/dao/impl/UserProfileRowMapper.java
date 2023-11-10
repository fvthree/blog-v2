package com.fvthree.blogpost.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;
import com.fvthree.blogpost.user.entity.User;

public class UserProfileRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		var user = new User();
		user.setId(rs.getLong("user_id"));
		user.setUsername(rs.getString("username"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setRole(rs.getInt("role"));
		user.setVerified(rs.getBoolean("is_verified"));
		user.setAccountLevel(rs.getString("account_level"));
		user.setProfilePicture(rs.getString("profile_picture"));
		user.setCoverImage(rs.getString("cover_image"));
		user.setNotificationPreferences(rs.getString("notification_preferences"));
		user.setGender(rs.getString("gender"));
		user.setBio(rs.getString("bio"));
		user.setLocation(rs.getString("location"));
		DateTimeFormatter formatter = null;
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		user.setDateCreated(LocalDateTime.parse(rs.getString("date_created"), formatter));
		user.setLastUpdated(LocalDateTime.parse(rs.getString("last_updated"), formatter));
		user.setLastLogin(LocalDateTime.parse(rs.getString("last_login"), formatter));
		return user;
	}
}
