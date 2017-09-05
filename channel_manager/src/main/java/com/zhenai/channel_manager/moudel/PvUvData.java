package com.zhenai.channel_manager.moudel;

import java.io.Serializable;

public class PvUvData  implements Serializable {

	private static final long serialVersionUID = -5928085937942344190L;

	private Integer testid;
	
	private Integer testversionid;
	
	private String clickid;
	
	private Integer pv;
	
	private Integer uv;
	
	private Integer data_date;
	
	private String etl_stamp_reserved;

	public Integer getTestid() {
		return testid;
	}

	public void setTestid(Integer testid) {
		this.testid = testid;
	}

	public Integer getTestversionid() {
		return testversionid;
	}

	public void setTestversionid(Integer testversionid) {
		this.testversionid = testversionid;
	}

	public String getClickid() {
		return clickid;
	}

	public void setClickid(String clickid) {
		this.clickid = clickid;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public Integer getUv() {
		return uv;
	}

	public void setUv(Integer uv) {
		this.uv = uv;
	}

	public Integer getData_date() {
		return data_date;
	}

	public void setData_date(Integer data_date) {
		this.data_date = data_date;
	}

	public String getEtl_stamp_reserved() {
		return etl_stamp_reserved;
	}

	public void setEtl_stamp_reserved(String etl_stamp_reserved) {
		this.etl_stamp_reserved = etl_stamp_reserved;
	}
	
	
}
