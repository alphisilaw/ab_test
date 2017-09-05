package com.web.abt.m.model;
import java.sql.ResultSet;
import java.util.Date;
import com.web.abt.m.model.BaseModel;

public class UserInfoModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer uid = 0;
    private String nickName;
    private String password;
    private Integer sex = 0;
    private String mobile;
    private String pickey;
    private String email;
    private String birthday;
    private String country;
    private String province;
    private String city;
    private Date createTime;
    private String iphoneToken;
    private String baiduChannelId;
    private String baiduUserId;
    private String platform;
    private String device;
    private String softversion;
    private String channel;
    private String phonecode;
    private String locale;
    private String countrycode;
    private String accessToken;
    private Integer online = 0;
    private String qqOpenId;
    private Integer status = 0;
    private String systemVersion;
    private Integer privateFlag = 0;
    private String wallpaper;
    private String sinaOpenId;
    private String xiaomiUserId;
    private String jpushUserId;
    private Date updateAt;
    /**
     * 构造函数
     * 
     * @param rs
     *            数据库查询结果集
     */
    public UserInfoModel getModelByRs(ResultSet rs) {
        try {
			this.uid = rs.getInt("uid");
			this.nickName = rs.getString("nickName");
			this.password = rs.getString("password");
			this.sex = rs.getInt("sex");
			this.mobile = rs.getString("mobile");
			this.pickey = rs.getString("pickey");
			this.email = rs.getString("email");
			this.birthday = rs.getString("birthday");
			this.country = rs.getString("country");
			this.province = rs.getString("province");
			this.city = rs.getString("city");
			this.createTime = rs.getDate("createTime");
			this.iphoneToken = rs.getString("iphoneToken");
			this.baiduChannelId = rs.getString("baiduChannelId");
			this.baiduUserId = rs.getString("baiduUserId");
			this.platform = rs.getString("platform");
			this.device = rs.getString("device");
			this.softversion = rs.getString("softversion");
			this.channel = rs.getString("channel");
			this.phonecode = rs.getString("phonecode");
			this.locale = rs.getString("locale");
			this.countrycode = rs.getString("countrycode");
			this.accessToken = rs.getString("accessToken");
			this.online = rs.getInt("online");
			this.qqOpenId = rs.getString("qqOpenId");
			this.status = rs.getInt("status");
			this.systemVersion = rs.getString("systemVersion");
			this.privateFlag = rs.getInt("privateFlag");
			this.wallpaper = rs.getString("wallpaper");
			this.sinaOpenId = rs.getString("sinaOpenId");
			this.xiaomiUserId = rs.getString("xiaomiUserId");
			this.jpushUserId = rs.getString("jpushUserId");
			this.updateAt = rs.getDate("updateAt");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
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
        if (!(other instanceof UserInfoModel)) {
            return false;
        }

        final UserInfoModel otherModel = (UserInfoModel) other;
        if (getUid() != null && !getUid().equals(otherModel.getUid())) {
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
        buffer.append('|').append(uid);
        return buffer.toString().hashCode();
    }

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPickey() {
		return pickey;
	}

	public void setPickey(String pickey) {
		this.pickey = pickey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIphoneToken() {
		return iphoneToken;
	}

	public void setIphoneToken(String iphoneToken) {
		this.iphoneToken = iphoneToken;
	}

	public String getBaiduChannelId() {
		return baiduChannelId;
	}

	public void setBaiduChannelId(String baiduChannelId) {
		this.baiduChannelId = baiduChannelId;
	}

	public String getBaiduUserId() {
		return baiduUserId;
	}

	public void setBaiduUserId(String baiduUserId) {
		this.baiduUserId = baiduUserId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getSoftversion() {
		return softversion;
	}

	public void setSoftversion(String softversion) {
		this.softversion = softversion;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPhonecode() {
		return phonecode;
	}

	public void setPhonecode(String phonecode) {
		this.phonecode = phonecode;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}

	public String getQqOpenId() {
		return qqOpenId;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public Integer getPrivateFlag() {
		return privateFlag;
	}

	public void setPrivateFlag(Integer privateFlag) {
		this.privateFlag = privateFlag;
	}

	public String getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(String wallpaper) {
		this.wallpaper = wallpaper;
	}

	public String getSinaOpenId() {
		return sinaOpenId;
	}

	public void setSinaOpenId(String sinaOpenId) {
		this.sinaOpenId = sinaOpenId;
	}

	public String getXiaomiUserId() {
		return xiaomiUserId;
	}

	public void setXiaomiUserId(String xiaomiUserId) {
		this.xiaomiUserId = xiaomiUserId;
	}

	public String getJpushUserId() {
		return jpushUserId;
	}

	public void setJpushUserId(String jpushUserId) {
		this.jpushUserId = jpushUserId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
    
}

