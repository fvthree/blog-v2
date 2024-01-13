package com.fvthree.blogpost.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.RowMapper;

import com.fvthree.blogpost.user.entity.UserRoles;

public class UserRolesRowMapper implements RowMapper<UserRoles> {

	@Override
	public UserRoles mapRow(ResultSet rs, int rowNum) throws SQLException {
		var user = new UserRoles();
		user.setId(rs.getLong("user_roles_id"));
		user.setRoleId(rs.getLong("role_id"));
		user.setUserId(rs.getLong("user_id"));
		DateTimeFormatter formatter = null;
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		user.setCreatedDate(LocalDateTime.parse(rs.getString("date_created"), formatter));
		user.setLastUpdated(LocalDateTime.parse(rs.getString("last_updated"), formatter));
		return user;
	}
}
