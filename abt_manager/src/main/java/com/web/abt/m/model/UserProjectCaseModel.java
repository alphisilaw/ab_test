package com.web.abt.m.model;
import java.sql.ResultSet;
import java.util.Date;
import com.web.abt.m.model.BaseModel;

public class UserProjectCaseModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer caseId = 0;
    private String caseName;
    private Integer buizType = 0;
    private Integer isMobile = 0;
    private Integer projectId = 0;
    private Integer caseStatus = 0;
    private Integer dayCount = 0;
    private Integer verCount = 0;
    private Integer curGoalCount = 0;
    private Integer maxGoalCount = 0;
    private String url;
    private Date createTime;
    private Date updateTime;
    private Date startRunTime;
    private int copyPrototypeId=0;
    private int copySN=0;
    /**
     * 构造函数
     * 
     * @param rs
     *            数据库查询结果集
     */
    public UserProjectCaseModel getModelByRs(ResultSet rs) {
        try {
			this.caseId = rs.getInt("caseId");
			this.caseName = rs.getString("caseName");
			this.buizType = rs.getInt("buizType");
			this.isMobile = rs.getInt("isMobile");
			this.projectId = rs.getInt("projectId");
			this.caseStatus = rs.getInt("caseStatus");
			this.dayCount = rs.getInt("dayCount");
			this.verCount = rs.getInt("verCount");
			this.curGoalCount = rs.getInt("curGoalCount");
			this.maxGoalCount = rs.getInt("maxGoalCount");
			this.url = rs.getString("url");
			this.createTime = rs.getDate("createTime");
			this.updateTime = rs.getDate("updateTime");
			this.startRunTime = rs.getDate("startRunTime");
			this.copySN=rs.getInt("copySN");
			this.copyPrototypeId=rs.getInt("copyPrototypeId");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
 
    public UserProjectCaseModel copyModel(UserProjectCaseModel model,int newCopySN,int oldCaseId) {
        try {
        	this.caseName = model.getCaseName() + "-copy("+newCopySN+")";
			this.buizType = model.getBuizType();
			this.isMobile = model.getIsMobile();
			this.projectId = model.getProjectId();
			this.caseStatus = 0;
			this.dayCount = 0;
			this.verCount = model.getVerCount();
			this.curGoalCount = model.getCurGoalCount();
			this.maxGoalCount = model.getMaxGoalCount();
			this.url = model.getUrl();
			this.createTime = new Date();
			this.updateTime = new Date();
			this.copySN=newCopySN;
			this.copyPrototypeId=oldCaseId;
			
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public Integer getBuizType() {
		return buizType;
	}

	public void setBuizType(Integer buizType) {
		this.buizType = buizType;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(Integer caseStatus) {
		this.caseStatus = caseStatus;
	}

	public Integer getDayCount() {
		return dayCount;
	}

	public void setDayCount(Integer dayCount) {
		this.dayCount = dayCount;
	}

	public Integer getVerCount() {
		return verCount;
	}

	public void setVerCount(Integer verCount) {
		this.verCount = verCount;
	}

	public Integer getCurGoalCount() {
		return curGoalCount;
	}

	public void setCurGoalCount(Integer curGoalCount) {
		this.curGoalCount = curGoalCount;
	}

	public Integer getMaxGoalCount() {
		return maxGoalCount;
	}

	public void setMaxGoalCount(Integer maxGoalCount) {
		this.maxGoalCount = maxGoalCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Date getStartRunTime() {
		return startRunTime;
	}

	public void setStartRunTime(Date startRunTime) {
		this.startRunTime = startRunTime;
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
        if (!(other instanceof UserProjectCaseModel)) {
            return false;
        }

        final UserProjectCaseModel otherModel = (UserProjectCaseModel) other;
        if (getCaseId() != null && !getCaseId().equals(otherModel.getCaseId())) {
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
        buffer.append('|').append(caseId);
        return buffer.toString().hashCode();
    }

    public int getCopyPrototypeId() {
		return copyPrototypeId;
	}

    public void setCopyPrototypeId(int copyPrototypeId) {
		this.copyPrototypeId = copyPrototypeId;
	}

    public int getCopySN() {
		return copySN;
	}

    public void setCopySN(int copySN) {
		this.copySN = copySN;
	}

	public Integer getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(Integer isMobile) {
		this.isMobile = isMobile;
	}
    
}

