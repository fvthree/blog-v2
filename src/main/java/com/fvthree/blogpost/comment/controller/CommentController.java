package com.fvthree.blogpost.comment.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.comment.entity.Comment;
import com.fvthree.blogpost.comment.service.CommentService;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController extends CommentAbstractController {
	
	private static final Logger logger = LogManager.getLogger(CommentController.class);
    
    @Autowired
    private CommentService commentService;
    
    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> createComment(
    		@PathVariable("postId") Long postId, @RequestBody Map<String, String> request){
    	logger.info(request);
    	String comment = request.get("comment");
    	long author = Long.parseLong(request.get("author"));
        return new ResponseEntity<>(commentService.insertComment(postId, comment, author),
        		HttpStatus.CREATED);
    }
    
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ListTypedResponse<Comment>> findAllCommentByPostId(@PathVariable("postId") Long postId) {
    	return new ResponseEntity<>(commentService.findAllComments(postId), HttpStatus.OK);
    }
    
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<String> updatePostComment(@PathVariable("commentId") Long commentId,
    		@RequestBody Map<String, String> request) {
    	String comment = request.get("comment");
    	return new ResponseEntity<>(commentService.updateComment(commentId, comment), HttpStatus.OK);
    }
    
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
    	return new ResponseEntity<>(commentService.deleteComment(commentId), HttpStatus.OK);
    }
}
