package com.nokia.ices.apps.fusion.log.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class HistoryData{
	
	@Id
	@GeneratedValue
	private Long id;
	private String userName; //姓名
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, optional = true)
	private Product product;  //产品名称
	private String modular;  //模块
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, optional = true)
	private Project projectName; //项目名称
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, optional = true)
	private Stage stage; //产品所处阶段
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, optional = true)
	private WorkType workPackageType; //工作包类型：工作阶段所处类型
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, optional = true)
	private WorkPackage workPackage; //工作包
	private String jobOperator;  //工作描述--备注
	private Double time; //工时数
	@JsonFormat(pattern="yyyy-MM-dd", locale="zh", timezone="GMT+8")
	private Date logDate; //日志时间
	private Boolean status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getModular() {
		return modular;
	}
	public void setModular(String modular) {
		this.modular = modular;
	}
	public Project getProjectName() {
		return projectName;
	}
	public void setProjectName(Project projectName) {
		this.projectName = projectName;
	}
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public WorkType getWorkPackageType() {
		return workPackageType;
	}
	public void setWorkPackageType(WorkType workPackageType) {
		this.workPackageType = workPackageType;
	}
	public WorkPackage getWorkPackage() {
		return workPackage;
	}
	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}
	public String getJobOperator() {
		return jobOperator;
	}
	public void setJobOperator(String jobOperator) {
		this.jobOperator = jobOperator;
	}
	public Double getTime() {
		return time;
	}
	public void setTime(Double time) {
		this.time = time;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
