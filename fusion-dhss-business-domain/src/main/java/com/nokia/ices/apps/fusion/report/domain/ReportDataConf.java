package com.nokia.ices.apps.fusion.report.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class ReportDataConf {
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * sheet页名称
	 */
	private String sheetName;
	
	/**
	 * 存放表头字段名,以逗号隔开
	 */
	@Column(length=4096)
	private String headerName;
	
	
	/**
	 * 提取原始数据所用的SQL
	 */
	@Column(length=4096)
	private String sqlForOriginalData;
	
	/**
	 * 开始填写数据的位置
	 */
	private String cellStartAddress;
	
	/**
	 * 备注信息
	 */
	@Column(length=4096)
	private String comment;
	/**
	 * 表格数据写入方式 landscape 表示数据横向循环写入 portrait表示数据纵向循环写入
	 */
	private String gridType;
	
	@ManyToMany(mappedBy = "reportDataConf")
	private Set<ReportTemplate> reportTemplate;

	public Set<ReportTemplate> getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(Set<ReportTemplate> reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public String getGridType() {
		return gridType;
	}

	public void setGridType(String gridType) {
		this.gridType = gridType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getSqlForOriginalData() {
		return sqlForOriginalData;
	}

	public void setSqlForOriginalData(String sqlForOriginalData) {
		this.sqlForOriginalData = sqlForOriginalData;
	}

	public String getCellStartAddress() {
		return cellStartAddress;
	}

	public void setCellStartAddress(String cellStartAddress) {
		this.cellStartAddress = cellStartAddress;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
