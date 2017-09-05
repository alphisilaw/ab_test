package com.web.abt.m.model;
import java.sql.ResultSet;
import java.util.Date;
import com.web.abt.m.model.BaseModel;

public class UserProjectModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer projectId = 0;
    private String projectName;
    private String description;
    private String platform;
    private Integer ownerId = 0;
    private Date createTime;
    private Date updateTime;
    /**
     * 构造函数
     * 
     * @param rs
     *            数据库查询结果集
     */
    public UserProjectModel getModelByRs(ResultSet rs) {
        try {
			this.projectId = rs.getInt("projectId");
			//System.out.println("ProjectID:"+rs.getInt("projectId"));/////////
			this.projectName = rs.getString("projectName");
			//System.out.println("ProjectName:"+rs.getString("projectName"));/////////
			this.description = rs.getString("description");
			this.platform = rs.getString("platform");
			this.ownerId = rs.getInt("ownerId");
			this.createTime = rs.getDate("createTime");
			this.updateTime = rs.getDate("updateTime");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
 
    public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
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
        if (!(other instanceof UserProjectModel)) {
            return false;
        }

        final UserProjectModel otherModel = (UserProjectModel) other;
        if (getProjectId() != null && !getProjectId().equals(otherModel.getProjectId())) {
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
        buffer.append('|').append(projectId);
        return buffer.toString().hashCode();
    }
}

