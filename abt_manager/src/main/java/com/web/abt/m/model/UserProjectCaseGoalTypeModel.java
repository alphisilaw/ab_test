package com.web.abt.m.model;
import java.sql.ResultSet;
import java.util.Date;
import com.web.abt.m.model.BaseModel;

public class UserProjectCaseGoalTypeModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer id = 0;
    private String goalTypeCode;
    private String goalTypeName;
    private Integer goalBuizType = 0;
    private Date createTime;
    private Date updateTime;
    /**
     * 构造函数
     * 
     * @param rs
     *            数据库查询结果集
     */
    public UserProjectCaseGoalTypeModel getModelByRs(ResultSet rs) {
        try {
			this.id = rs.getInt("id");
			this.goalTypeCode = rs.getString("goalTypeCode");
			this.goalTypeName = rs.getString("goalTypeName");
			this.goalBuizType = rs.getInt("goalBuizType");
			this.createTime = rs.getDate("createTime");
			this.updateTime = rs.getDate("updateTime");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
 
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGoalTypeCode() {
		return goalTypeCode;
	}

	public void setGoalTypeCode(String goalTypeCode) {
		this.goalTypeCode = goalTypeCode;
	}

	public String getGoalTypeName() {
		return goalTypeName;
	}

	public void setGoalTypeName(String goalTypeName) {
		this.goalTypeName = goalTypeName;
	}

	public Integer getGoalBuizType() {
		return goalBuizType;
	}

	public void setGoalBuizType(Integer goalBuizType) {
		this.goalBuizType = goalBuizType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
     * 比较两个对象在逻辑上是否相等
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserProjectCaseGoalTypeModel)) {
            return false;
        }

        final UserProjectCaseGoalTypeModel otherModel = (UserProjectCaseGoalTypeModel) other;
        if (getId() != null && !getId().equals(otherModel.getId())) {
            return false;
        }
        return true;
    }

    /**
     * 根据主键生成HashCode
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('|').append(id);
        return buffer.toString().hashCode();
    }
}

