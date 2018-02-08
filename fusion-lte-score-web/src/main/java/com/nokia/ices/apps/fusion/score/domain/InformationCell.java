package com.nokia.ices.apps.fusion.score.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * 基站基础信息
 * */
@SuppressWarnings("serial")
@Entity
public class InformationCell  implements Serializable {

	   @Id
	   @GeneratedValue  
	   private Long id;
	   private int areaCode;    //地市ID
	   private int cityCode;    //所属区县ID' / 区县CODE
	   private String cellNameCn;  //小区中文名称
	   private String cellNameEn;    //英文 页面不显示
	   private String neNameCn;    //基站中文名称
	   private String neNameEn;		//英文 页面不显示
	   private int neCode;	   //基站ID
	   private int cellId;      //小区ID
	   private int sectorId;    //扇区
	   private double proxyX;	   //经度
	   private double proxyY;  	   //纬度
	   //  is_update tinyint(4) DEFAULT '2' COMMENT '自动添加的基站，状态是1，否则是2，在界面上更新之后，变成2',
	   private int isUpdate;
	   
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public int getAreaCode() {
			return areaCode;
		}
		public void setAreaCode(int areaCode) {
			this.areaCode = areaCode;
		}
		public int getCityCode() {
			return cityCode;
		}
		public void setCityCode(int cityCode) {
			this.cityCode = cityCode;
		}
		public String getCellNameCn() {
			return cellNameCn;
		}
		public void setCellNameCn(String cellNameCn) {
			this.cellNameCn = cellNameCn;
		}
		public String getCellNameEn() {
			return cellNameEn;
		}
		public void setCellNameEn(String cellNameEn) {
			this.cellNameEn = cellNameEn;
		}
		public String getNeNameCn() {
			return neNameCn;
		}
		public void setNeNameCn(String neNameCn) {
			this.neNameCn = neNameCn;
		}
		public String getNeNameEn() {
			return neNameEn;
		}
		public void setNeNameEn(String neNameEn) {
			this.neNameEn = neNameEn;
		}
		public int getNeCode() {
			return neCode;
		}
		public void setNeCode(int neCode) {
			this.neCode = neCode;
		}
		public int getCellId() {
			return cellId;
		}
		public void setCellId(int cellId) {
			this.cellId = cellId;
		}
		public int getSectorId() {
			return sectorId;
		}
		public void setSectorId(int sectorId) {
			this.sectorId = sectorId;
		}
		public double getProxyX() {
			return proxyX;
		}
		public void setProxyX(double proxyX) {
			this.proxyX = proxyX;
		}
		public double getProxyY() {
			return proxyY;
		}
		public void setProxyY(double proxyY) {
			this.proxyY = proxyY;
		}
		public int getIsUpdate() {
			return isUpdate;
		}
		public void setIsUpdate(int isUpdate) {
			this.isUpdate = isUpdate;
		}

	   
}
