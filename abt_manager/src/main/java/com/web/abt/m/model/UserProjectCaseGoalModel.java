package com.web.abt.m.model;
import java.sql.ResultSet;
import java.util.Date;
import com.web.abt.m.model.BaseModel;

public class UserProjectCaseGoalModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer goalId = 0;
    private String goalName;
    private Integer goalTypeId = 0;
    private String goalSelector;
    private Integer seq = 0;
    private Integer caseId = 0;
    private Integer projectId = 0;
    private Integer goalBuizType = 0;
    private Date createTime;
    private Date updateTime;
    private Integer isMaster = 0;
    /**
     * 构造函数
     * 
     * @param rs
     *            数据库查询结果集
     */
    public UserProjectCaseGoalModel getModelByRs(ResultSet rs) {
        try {
			this.goalId = rs.getInt("goalId");
			this.goalName = rs.getString("goalName");
			this.goalTypeId = rs.getInt("goalTypeId");
			this.goalSelector = rs.getString("goalSelector");
			this.seq = rs.getInt("seq");
			this.caseId = rs.getInt("caseId");
			this.projectId = rs.getInt("projectId");
			this.goalBuizType = rs.getInt("goalBuizType");
			this.createTime = rs.getDate("createTime");
			this.updateTime = rs.getDate("updateTime");
			this.isMaster = rs.getInt("isMaster");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
 
    public UserProjectCaseGoalModel copyModel(UserProjectCaseGoalModel model, int newCaseId, int newProjectId) {
        try {
			this.goalName = model.getGoalName();
			this.goalTypeId = model.getGoalTypeId();
			this.goalSelector = model.getGoalSelector();
			this.seq = model.getSeq();
			this.caseId = newCaseId;
			this.projectId = newProjectId;
			this.goalBuizType = model.getGoalBuizType();
			this.createTime = new Date();
			this.updateTime = new Date();
			this.isMaster = model.getIsMaster();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public Integer getGoalId() {
		return goalId;
	}

	public void setGoalId(Integer goalId) {
		this.goalId = goalId;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public Integer getGoalTypeId() {
		return goalTypeId;
	}

	public void setGoalTypeId(Integer goalTypeId) {
		this.goalTypeId = goalTypeId;
	}

	public String getGoalSelector() {
		return goalSelector;
	}

	public void setGoalSelector(String goalSelector) {
		this.goalSelector = goalSelector;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
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

	public Integer getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(Integer isMaster) {
		this.isMaster = isMaster;
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
        if (!(other instanceof UserProjectCaseGoalModel)) {
            return false;
        }

        final UserProjectCaseGoalModel otherModel = (UserProjectCaseGoalModel) other;
        if (getGoalId() != null && !getGoalId().equals(otherModel.getGoalId())) {
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
        buffer.append('|').append(goalId);
        return buffer.toString().hashCode();
    }
}

