package com.fvthree.blogpost.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.RowMapper;

import com.fvthree.blogpost.user.entity.User;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		var user = new User();
		user.setId(rs.getLong("user_id"));
		user.setUsername(rs.getString("username"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		DateTimeFormatter formatter = null;
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		user.setDateCreated(LocalDateTime.parse(rs.getString("date_created"), formatter));
		user.setLastUpdated(LocalDateTime.parse(rs.getString("last_updated"), formatter));
		
		return user;
	}
}
