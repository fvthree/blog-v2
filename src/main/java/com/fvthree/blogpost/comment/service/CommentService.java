package com.fvthree.blogpost.comment.service;

import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.comment.entity.Comment;

public interface CommentService {
	String insertComment(long postId, String comment, long author);
	ListTypedResponse<Comment> findAllComments(long postId);
	String updateComment(long id, String comment);
	String deleteComment(long id);
}
