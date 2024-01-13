package com.fvthree.blogpost.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;
import com.fvthree.blogpost.category.dao.impl.Category;

public class CategoryRowMapper implements RowMapper<Category> {

	@Override
	public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
		var category = new Category();
		category.setId(rs.getLong("category_id"));
		category.setName(rs.getString("name"));
		category.setShares(rs.getInt("shares"));
		category.setAuthor(rs.getString("author"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		category.setDateCreated(LocalDateTime.parse(rs.getString("date_created"), formatter));
		category.setLastUpdated(LocalDateTime.parse(rs.getString("last_updated"), formatter));
		return category;
	}
}
