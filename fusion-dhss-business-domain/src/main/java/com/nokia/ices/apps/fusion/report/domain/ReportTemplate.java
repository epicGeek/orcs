package com.nokia.ices.apps.fusion.report.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class ReportTemplate implements Serializable {
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 5175579059724117484L;

		@Id
		@GeneratedValue
		private Long id;
		
		private String reportName;
		
		private String reportTemplatePath;
		
		private Boolean isAvailable;
		
		private Date executeTime;
		
		private String outPutPath;
		
		
		@ManyToMany
		private Set<ReportDataConf> reportDataConf; 

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getReportTemplatePath() {
			return reportTemplatePath;
		}

		public void setReportTemplatePath(String reportTemplatePath) {
			this.reportTemplatePath = reportTemplatePath;
		}

		public Boolean getIsAvailable() {
			return isAvailable;
		}

		public void setIsAvailable(Boolean isAvailable) {
			this.isAvailable = isAvailable;
		}

		public Date getExecuteTime() {
			return executeTime;
		}

		public void setExecuteTime(Date executeTime) {
			this.executeTime = executeTime;
		}

		public String getOutPutPath() {
			return outPutPath;
		}

		public void setOutPutPath(String outPutPath) {
			this.outPutPath = outPutPath;
		}

		public Set<ReportDataConf> getReportDataConf() {
			return reportDataConf;
		}

		public void setReportDataConf(Set<ReportDataConf> reportDataConf) {
			this.reportDataConf = reportDataConf;
		}

	
		

}
