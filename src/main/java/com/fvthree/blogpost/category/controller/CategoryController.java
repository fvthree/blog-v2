package com.fvthree.blogpost.category.controller;

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

import com.fvthree.blogpost.category.controller.request.CreateCategoryRequest;
import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.category.dao.impl.Category;
import com.fvthree.blogpost.category.service.CategoryService;
import com.fvthree.blogpost.post.entity.Post;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends CategoryAbstractController {
    
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CreateCategoryRequest request){
        return ResponseEntity.ok(categoryService.createCategory(request));
    }
    
    @GetMapping
    public ResponseEntity<ListTypedResponse<Category>> findAll() {
    	return ResponseEntity.ok(categoryService.findAll());
    }
    
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable("categoryId") Long categoryId,
    		@RequestBody CreateCategoryRequest request) {
    	return new ResponseEntity<>(
    			categoryService.updateCategory(categoryId, request), HttpStatus.OK);
    }
    
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
    	return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
    
    @GetMapping("/posts/{categoryId}")
    public ResponseEntity<ListTypedResponse<Post>> findPostByCategoryId(@PathVariable("categoryId") Long id) {
    	return ResponseEntity.ok(categoryService.findAllPostByCategory(id));
    }
}
