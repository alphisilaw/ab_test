package com.web.abt.m.common;

public class SQLCondition {
	
	private String conName;
	private String operation;
	private ConditionTypes conType;
	private Object conValue;
	
	public SQLCondition(String conName, String opr, ConditionTypes conType, Object conValue){
		this.conName = conName;
		this.operation = opr;
		this.conType = conType;
		this.conValue = conValue;
	}
	
	public String getConName() {
		return conName;
	}
	public void setConName(String conName) {
		this.conName = conName;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public ConditionTypes getConType() {
		return conType;
	}
	public void setConType(ConditionTypes conType) {
		this.conType = conType;
	}
	public Object getConValue() {
		return conValue;
	}
	public void setConValue(Object conValue) {
		this.conValue = conValue;
	}
	
	public static enum ConditionTypes {
	    STRING,
	    INT,
	    DATE,
	    EVAL
	}
}
