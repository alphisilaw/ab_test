package com.web.abt.dao;

import org.springframework.stereotype.Repository;

import com.web.abt.moudel.UserProjectModel;

@Repository
public class UserProjectDao extends BaseDao<UserProjectModel> {

	@Override
	protected String getKey() {
		return "projectId";
	}

}
