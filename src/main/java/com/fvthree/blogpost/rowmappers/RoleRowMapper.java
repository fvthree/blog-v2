package com.fvthree.blogpost.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.RowMapper;

import com.fvthree.blogpost.role.entity.Role;

public class RoleRowMapper implements RowMapper<Role> {

	@Override
	public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
		var role = new Role();
		role.setId(rs.getLong("role_id"));
		role.setName(rs.getString("name"));
		DateTimeFormatter formatter = null;
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		role.setCreatedDate(LocalDateTime.parse(rs.getString("date_created"), formatter));
		role.setLastUpdated(LocalDateTime.parse(rs.getString("last_updated"), formatter));
		return role;
	}
}
