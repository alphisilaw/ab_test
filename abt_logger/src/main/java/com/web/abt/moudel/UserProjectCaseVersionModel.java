package com.web.abt.moudel;
import java.io.Serializable;
import java.util.Date;

public class UserProjectCaseVersionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer versionId = 0;
    private String versionName;
    private Integer versionType = 0;
    private Integer versionStatus = 0;
    private Integer caseId = 0;
    private Integer projectId = 0;
    private String jsCode;
    private Date createTime;
    private Date updateTime;
    private Double percent;
    private String forwardUrl;
    
    public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getVersionType() {
		return versionType;
	}

	public void setVersionType(Integer versionType) {
		this.versionType = versionType;
	}

	public Integer getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(Integer versionStatus) {
		this.versionStatus = versionStatus;
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

	public String getJsCode() {
		return jsCode;
	}

	public void setJsCode(String jsCode) {
		this.jsCode = jsCode;
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

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
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
        if (!(other instanceof UserProjectCaseVersionModel)) {
            return false;
        }

        final UserProjectCaseVersionModel otherModel = (UserProjectCaseVersionModel) other;
        if (getVersionId() != null && !getVersionId().equals(otherModel.getVersionId())) {
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
        buffer.append('|').append(versionId);
        return buffer.toString().hashCode();
    }
    
    public Integer getPercentInt() {
		return this.percent.intValue();
	}
}

