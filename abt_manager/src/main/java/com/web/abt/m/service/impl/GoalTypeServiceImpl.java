package com.web.abt.m.service.impl;

import java.util.List;

import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.dao.UserProjectCaseGoalTypeDao;
import com.web.abt.m.model.UserProjectCaseGoalTypeModel;
import com.web.abt.m.service.GoalTypeService;

/**
 */
public class GoalTypeServiceImpl extends BaseServiceImpl implements GoalTypeService {

	@Override
	public ResultBean doSearchGoalTypesByBuizType(Object... buizType) {
		
		List<UserProjectCaseGoalTypeModel> list = UserProjectCaseGoalTypeDao.getInstance().findAllByBuizType(buizType);
		if(list != null){
			return Result(list);
		}else{
			return Result(ResultConstant.EMPTY_PAGE_ATTR);
		}
		
	}

}
