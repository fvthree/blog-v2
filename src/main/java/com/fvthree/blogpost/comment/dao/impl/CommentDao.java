package com.fvthree.blogpost.comment.dao.impl;

import java.util.List;

import com.fvthree.blogpost.base.dao.BaseDao;
import com.fvthree.blogpost.comment.entity.Comment;

public interface CommentDao extends BaseDao<Comment> {
	void insertPostComments(long posId, long commentId);
	List<Comment> getAllCommentsByPostId(long postId);
	void updateComment(long id, String comment);
}
