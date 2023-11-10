package com.fvthree.blogpost.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;
import com.fvthree.blogpost.comment.entity.Comment;

public class CommentRowMapper implements RowMapper<Comment> {

	@Override
	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
		var comment = new Comment();
		comment.setCommentId(rs.getLong("comment_id"));
		comment.setBody(rs.getString("comment"));
		comment.setAuthor(rs.getLong("author"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		comment.setDateCreated(LocalDateTime.parse(rs.getString("date_created"), formatter));
		return comment;
	}
}
