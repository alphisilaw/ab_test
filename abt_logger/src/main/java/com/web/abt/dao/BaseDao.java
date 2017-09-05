package com.web.abt.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.web.abt.util.ReflectionUtils;
import com.web.abt.util.SqlUtil;


public abstract class BaseDao<T> {

	
	@Resource
	protected NamedParameterJdbcTemplate mainNamedTemplate;

	@SuppressWarnings("unchecked")
	public BaseDao() { 
		this.entityClass = (Class<T>) ReflectionUtils.getGenricType(getClass());
		this.rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
	}  

	private Class<T> entityClass;
	
	protected RowMapper<T> rowMapper;

	protected abstract String getKey();
	
	protected String getTableName() {
		return entityClass.getSimpleName();
	}

	public void save(T t) {
		save(getKey(), t);
	}

	public void save(String key, T t) {
		Map<String, Object> beanMap = SqlUtil.beanToMap(t);
		String sql = SqlUtil.getInsertSQL(getTableName(), beanMap, key);
		SqlParameterSource ps = new BeanPropertySqlParameterSource(t);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		mainNamedTemplate.update(sql, ps, keyHolder);
		ReflectionUtils.setFieldValue(t, key,  keyHolder.getKey().intValue());
	}

	public void save(T... ts) {
		save(getKey(), ts);
	}

	public void save(String key, T... ts) {
		
		if (ts.length == 0) {
			return;
		}
		T first = ts[0];
		Map<String, Object> beanMap = SqlUtil.beanToMap(first);
		String sql = SqlUtil.getInsertSQL(getTableName(), beanMap, key);
		
		SqlParameterSource[] batchArgs = new SqlParameterSource[ts.length];
		for (int i=0; i<ts.length; i++) {
			batchArgs[i] = new BeanPropertySqlParameterSource(ts[i]);
		}
		mainNamedTemplate.batchUpdate(sql, batchArgs);

	}

	public void deleteById(Object...ids) {
		deleteByKey(getKey(), ids);
	}

	public void deleteByKey(String key, Object...values) {
		
		if (values.length == 0) {
			return;
		}
		String sql = SqlUtil.getDeleteSQL(getTableName(), key, values);
		mainNamedTemplate.getJdbcOperations().update(sql, values);
	}

	public void update(T...ts) {
		update(getKey(), ts);
	}

	public void update(String key, T...ts) {

		if (ts.length == 0) {
			return;
		}
		T first = ts[0];
		Map<String, Object> beanMap = SqlUtil.beanToMap(first);
		String sql = SqlUtil.getUpdateSQL(getTableName(), beanMap, key);
		
		SqlParameterSource[] batchArgs = new SqlParameterSource[ts.length];
		for (int i=0; i<ts.length; i++) {
			batchArgs[i] = new BeanPropertySqlParameterSource(ts[i]);
		}
		mainNamedTemplate.batchUpdate(sql, batchArgs);
	}

	public void replace(T t) {
		replace(getKey(), t);
	}

	public void replace(String key, T t) {

		Map<String, Object> beanMap = SqlUtil.beanToMap(t);
		String sql = SqlUtil.getReplaceSQL(getTableName(), beanMap, key);
		
		SqlParameterSource ps = new BeanPropertySqlParameterSource(t);
		mainNamedTemplate.update(sql, ps);
	}

}
