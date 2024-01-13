package com.fvthree.blogpost.comment.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.comment.dao.impl.CommentDao;
import com.fvthree.blogpost.comment.entity.Comment;
import com.fvthree.blogpost.exceptions.DBException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	
	private static final Logger logger = LogManager.getLogger(CommentServiceImpl.class);
	
	@Autowired
	private CommentDao commentDao;

	@Override
	public String insertComment(long postId, String comment, long author) {
		try {
			Comment cmt = new Comment();
			cmt.setBody(comment);
			cmt.setAuthor(author);
			long commentId = commentDao.insert(cmt);
			commentDao.insertPostComments(postId, commentId);
		} catch(Exception e) {
			logger.error("exception at comment service = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
		return "Successfully posted a comment.";
	}

	@Override
	public ListTypedResponse<Comment> findAllComments(long postId) {
		ListTypedResponse<Comment> comments = new ListTypedResponse<>();
		comments.setStatus("Success");
		comments.setMessage("Successfully fetched all comments.");
		comments.setData(commentDao.getAllCommentsByPostId(postId));
		return comments;
	}

	@Override
	public String updateComment(long id, String comment) {
		try {
			commentDao.updateComment(id, comment);
		} catch(Exception e) {
			logger.error("exception at updateComment method = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
		return "Comment updated successfully!";
	}
	
	@Override
	public String deleteComment(long id) {
		try {
			commentDao.delete(id);
		} catch(Exception e) {
			logger.error("exception at updateComment method = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
		return "Comment deleted successfully!";
	}
}
