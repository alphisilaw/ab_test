package com.zhenai.channel_manager.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class SqlUtil {
	
	private final static String  INSERT = "INSERT INTO ";
	private final static String  REPLACE = "REPLACE INTO ";
	private final static String  UPDATE = "UPDATE ";
	private final static String  DELETE = "DELETE FROM ";
	private final static String  SELECT = "SELECT ";
	
	
	public static Map<String, Object> beanToMap(Object bean) {
		if (bean == null) {
			return null;
		}
		Field[] fs = bean.getClass().getDeclaredFields();
		Class<?> superClass = bean.getClass().getSuperclass();
		while(superClass != null && superClass != Object.class){
			fs = ArrayUtils.addAll(fs, superClass.getDeclaredFields());
			superClass = superClass.getSuperclass();
		}
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (int i = 0; i < fs.length; i++) {
			String name = fs[i].getName();
			if ("serialVersionUID".equals(name))
				continue;
			Object value = null;
			try {
				Method m = bean.getClass().getMethod("get" + StringUtils.capitalize(name));
				value = m.invoke(bean);
			} catch (Exception e) {
				System.err.println(e + " : name=" + name + ",value=" + value);
			}
			if (value != null) {
				valuesMap.put(name, value);
			}
		}
		return valuesMap;
	}
	
	public static String getInsertSQL(String table,
			Map<String, Object> dataMap, String primaryKey) {
		StringBuilder sql1 = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		sql1.append(INSERT).append(table).append(" (");
		sql2.append(" ) VALUES (");
		for (Entry<String, Object> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (key.equalsIgnoreCase(primaryKey)) {
				continue;
			}
			if (value == null) {
				continue;
			} else {
				sql1.append(key).append(", ");
				sql2.append(":").append(key).append(", ");
			}
		}
		sql1 = new StringBuilder(sql1.subSequence(0, sql1.lastIndexOf(",")));
		sql2 = new StringBuilder(sql2.subSequence(0, sql2.lastIndexOf(",")));
		sql1.append(sql2).append(")");
		return sql1.toString();
	}
	
	
	public static String getReplaceSQL(String table,
			Map<String, Object> dataMap, String primaryKey) {
		StringBuilder sql1 = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		sql1.append(REPLACE).append(table).append(" (");
		sql2.append(" ) VALUES (");
		for (Entry<String, Object> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (primaryKey != null && key.equalsIgnoreCase(primaryKey)) {
				continue;
			}
			if (value == null) {
				continue;
			} else {
				sql1.append(key).append(", ");
				sql2.append(":").append(key).append(", ");
			}
		}
		sql1 = new StringBuilder(sql1.subSequence(0, sql1.lastIndexOf(",")));
		sql2 = new StringBuilder(sql2.subSequence(0, sql2.lastIndexOf(",")));
		sql1.append(sql2).append(")");
		return sql1.toString();
	}

	public static String getUpdateSQL(String table, boolean allowNull,
			Map<String, Object> dataMap, String primaryKey) {
		if (dataMap == null || dataMap.size() == 0) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(UPDATE).append(table).append(" SET ");
		for (Entry<String, Object> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (primaryKey != null && key.equalsIgnoreCase(primaryKey)) {
				continue;
			}
			if (value == null) {
				continue;
			} else {
				sql.append(key).append("=");
				sql.append(":").append(key).append(", ");
			}
		}
		sql = new StringBuilder(sql.subSequence(0, sql.lastIndexOf(",")));
		sql.append(" WHERE 1=1");
		if (primaryKey != null) {
			sql.append(" AND ").append(primaryKey).append("=");
			sql.append(":").append(primaryKey);
		}
		return sql.toString();
	}
	
	public static String getUpdateSQL(String table,
			Map<String, Object> dataMap, String primaryKey) {
		return getUpdateSQL(table, true, dataMap, primaryKey);
	}

	public static String getSelectSQL(String tableName, Map<String, Object> param) {
		StringBuffer sql = new StringBuffer(SELECT).append(" * FROM ").append(tableName)
				.append(" WHERE 1=1");
		for (Entry<String, Object> entry : param.entrySet()) {
			String key = entry.getKey();
			sql.append(" AND ").append(key).append("=:").append(key);
		}
		return sql.toString();
	}
	
	public static String getDeleteSQL(String table, String key, Object... values) {
		StringBuffer sql = new StringBuffer(DELETE).append(table)
				.append(" WHERE ").append(key).append(" IN(");
		for (int i = 0; i < values.length; i++) {
			if (i == values.length - 1) {
				sql.append("?");
			} else {
				sql.append("?,");
			}
		}
		sql.append(")");
		return sql.toString();
	}

	
	public static String getSelectSQL(String table, String key, Object... values) {
		StringBuffer sql = new StringBuffer(SELECT).append(" * FROM ").append(table)
				.append(" WHERE ").append(key).append(" IN(");
		for (int i = 0; i < values.length; i++) {
			if (i == values.length - 1) {
				sql.append("?");
			} else {
				sql.append("?,");
			}
		}
		sql.append(")");
		return sql.toString();
	}
}
