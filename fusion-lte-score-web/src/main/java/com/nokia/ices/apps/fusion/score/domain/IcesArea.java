/*package com.nokia.ices.apps.fusion.score.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="ices_area")
public class IcesArea  implements Serializable  {

	private Integer areaCode; //地市ID
	@Id
	@GeneratedValue 
	private Integer cityCode; //区县ID
	private String areaEn;  //地区英文名'
	private String areaCn;  //地区中文名称;(地市)
	private String cityCn;  //区县中文名称
    private Integer araeOrder; //地区排序 
    private Integer  countiesOrder; //区县排序 
 
	public String getAreaEn() {
		return areaEn;
	}
	public void setAreaEn(String areaEn) {
		this.areaEn = areaEn;
	}
	public String getAreaCn() {
		return areaCn;
	}
	public void setAreaCn(String areaCn) {
		this.areaCn = areaCn;
	}
	public String getCityCn() {
		return cityCn;
	}
	public void setCityCn(String cityCn) {
		this.cityCn = cityCn;
	}
	public Integer getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}
	public Integer getCityCode() {
		return cityCode;
	}
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}
	public Integer getAraeOrder() {
		return araeOrder;
	}
	public void setAraeOrder(Integer araeOrder) {
		this.araeOrder = araeOrder;
	}
	public Integer getCountiesOrder() {
		return countiesOrder;
	}
	public void setCountiesOrder(Integer countiesOrder) {
		this.countiesOrder = countiesOrder;
	}

} */