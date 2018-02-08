package com.nokia.ices.apps.fusion.score.domain;

import java.math.BigDecimal;
import java.util.List;

public class BtsProxy {
	private Integer areaCode;
	private String areaName;
	private Integer cityCode;
	private String cityName;
	private Integer bsNo;
	private String locateName;
	private Integer star;
	private BigDecimal proxyX;
	private BigDecimal proxyY;
	private List<BigDecimal> latlng;
	private BigDecimal score;

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public BigDecimal getProxyX() {
		return proxyX;
	}

	public void setProxyX(BigDecimal proxyX) {
		this.proxyX = proxyX;
	}

	public BigDecimal getProxyY() {
		return proxyY;
	}

	public void setProxyY(BigDecimal proxyY) {
		this.proxyY = proxyY;
	}

	public Integer getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getBsNo() {
		return bsNo;
	}

	public void setBsNo(Integer bsNo) {
		this.bsNo = bsNo;
	}

	public String getLocateName() {
		return locateName;
	}

	public void setLocateName(String locateName) {
		this.locateName = locateName;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public List<BigDecimal> getLatlng() {
		return latlng;
	}

	public void setLatlng(List<BigDecimal> latlng) {
		this.latlng = latlng;
	}

}
