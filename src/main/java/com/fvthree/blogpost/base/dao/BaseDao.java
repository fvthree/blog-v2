package com.fvthree.blogpost.base.dao;

import java.util.List;

public interface BaseDao<T> {
	
	T findById(long id);
	
	List<T> findAll(int page, int size);
	
	long insert(T entity);
	
	void update(long id ,T entity);
	
	void delete(long id);
}
