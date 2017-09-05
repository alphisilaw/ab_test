package com.web.abt.m.common;

import java.io.Serializable;

public class ResultBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isSuccess;
	private String errCode;
	private String errMsg;
	private Object bean;
	
	public ResultBean(){
		this.isSuccess = true;
	}
	
	public ResultBean(Object bean){
		this.isSuccess = true;
		this.bean = bean;
	}
	
	public ResultBean(String errCode){
		this.isSuccess = false;
		this.errCode = errCode;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}
}
