package com.zhenai.channel_manager.moudel;

public class User {

	private String username;
	private String realname;
	private Integer employeeid;
	private String email;
	private Integer phone;
	
	/**组织架构信息**/
	private String departmentLevel1;
	private String departmentLevel2;
	private String departmentLevel3;
	private String departmentLevel4;
	private String departmentLevel5;
	private String departmentLevel6;
	private String departmentLevel7;
	private Integer approvalLevel;//人事级别
	private String company;
	private String salaryArea;
	
	
	public Integer getPhone() {
		return phone;
	}
	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public Integer getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(Integer employeeid) {
		this.employeeid = employeeid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDepartmentLevel1() {
		return departmentLevel1;
	}
	public String getDepartmentLevel2() {
		return departmentLevel2;
	}
	public void setDepartmentLevel2(String departmentLevel2) {
		this.departmentLevel2 = departmentLevel2;
	}
	public String getDepartmentLevel3() {
		return departmentLevel3;
	}
	public void setDepartmentLevel3(String departmentLevel3) {
		this.departmentLevel3 = departmentLevel3;
	}
	public String getDepartmentLevel4() {
		return departmentLevel4;
	}
	public void setDepartmentLevel4(String departmentLevel4) {
		this.departmentLevel4 = departmentLevel4;
	}
	public String getDepartmentLevel5() {
		return departmentLevel5;
	}
	public void setDepartmentLevel5(String departmentLevel5) {
		this.departmentLevel5 = departmentLevel5;
	}
	public String getDepartmentLevel6() {
		return departmentLevel6;
	}
	public void setDepartmentLevel6(String departmentLevel6) {
		this.departmentLevel6 = departmentLevel6;
	}
	public String getDepartmentLevel7() {
		return departmentLevel7;
	}
	public void setDepartmentLevel7(String departmentLevel7) {
		this.departmentLevel7 = departmentLevel7;
	}
	public Integer getApprovalLevel() {
		return approvalLevel;
	}
	public void setApprovalLevel(Integer approvalLevel) {
		this.approvalLevel = approvalLevel;
	}
	public void setDepartmentLevel1(String departmentLevel1) {
		this.departmentLevel1 = departmentLevel1;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSalaryArea() {
		return salaryArea;
	}
	public void setSalaryArea(String salaryArea) {
		this.salaryArea = salaryArea;
	}
	
}
