package com.fvthree.blogpost.role.dao.impl;

import java.util.Optional;

import com.fvthree.blogpost.base.dao.BaseDao;
import com.fvthree.blogpost.role.entity.Role;

public interface RoleDao extends BaseDao<Role> {
	Optional<Role> findByName(String name);
}
