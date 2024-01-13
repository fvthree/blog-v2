package com.fvthree.blogpost.role.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import com.fvthree.blogpost.role.entity.Role;

import jakarta.transaction.Transactional;

@Transactional
@Component
public class RoleDaoImpl implements RoleDao {
	
	@Autowired
	private final JdbcClient jdbcClient;

	public RoleDaoImpl(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	@Override
	public Role findById(long id) {
		return null;
	}

	@Override
	public List<Role> findAll(int page, int size) {
		return new ArrayList<>();
	}

	@Override
	public long insert(Role entity) {
		return 0;
	}

	@Override
	public void update(long id, Role entity) {

	}

	@Override
	public void delete(long id) {

	}

	private static final String FIND_BY_NAME = "SELECT role_id, name, date_created, last_updated FROM roles WHERE name = :name";

	@Override
	public Optional<Role> findByName(String name) {
		return jdbcClient.sql(FIND_BY_NAME).param("name", name).query(new RoleRowMapper()).optional();
	}
}
