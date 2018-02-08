package com.nokia.ices.apps.fusion.kpi.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



@Entity
public class KpiItem {
	@Id
	@GeneratedValue
	private Long id;
	
	
	/**
	 * 执行数据库操作的SQL语句
	 */
	@Column(length=4096)
	private String kpiQueryScript;
	/**
	 * kpi分类(场景) : 语音、短信。。。。
	 */
	private String kpiCategory;
	

    /**
     * kpi名称 如：鉴权成功率
     */
	private String kpiName;
	
	/**
	 * KPI代码 ：kpi001,kpi002....
	 */
	private String kpiCode;

    /**
     * 数据源
     */
	private String dataSourceName;

    /**
     * 下一步操作
     */
	private String nextOperation;
	

    /**
     * 输出字段success_rate,fail_rate,total_count,success_count,fail_count
     */
    private String outPutField;
    /***************门限相关的字段**************/
    /**
     * 请求次数的样本基数。对应quota_monitor里面的kpi_request_count.以样本基数判断忙时闲时。
     * 样本基数=0，该KPI无样本基数概念或者该单元设备上不存在这类KPI。
     */
    private Integer requestSample;
    
    private String threshold;
    /**
     * 取消门限。KPI恢复正常所要达到的门限值。
     */
    private String thresholdCancel;

    private String compareMethod;
    

	/**
     * 监控时间段：开始-结束。其他时间段不触发告警
     * 1-4,6,8-9
     */
    private String monitorTimeString;

	public String getMonitorTimeString() {
		return monitorTimeString;
	}

	public void setMonitorTimeString(String monitorTimeString) {
		this.monitorTimeString = monitorTimeString;
	}

	public String getThresholdCancel() {
		return thresholdCancel;
	}

	public void setThresholdCancel(String thresholdCancel) {
		this.thresholdCancel = thresholdCancel;
	}

	public Integer getRequestSample() {
		return requestSample;
	}

	public void setRequestSample(Integer requestSample) {
		this.requestSample = requestSample;
	}

	public String getOutPutField() {
        return outPutField;
    }

    public void setOutPutField(String outPutField) {
        this.outPutField = outPutField;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKpiQueryScript() {
		return kpiQueryScript;
	}

	public void setKpiQueryScript(String kpiQueryScript) {
		this.kpiQueryScript = kpiQueryScript;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getKpiCode() {
		return kpiCode;
	}

	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getNextOperation() {
		return nextOperation;
	}

	public void setNextOperation(String nextOperation) {
		this.nextOperation = nextOperation;
	}
	public String getKpiCategory() {
        return kpiCategory;
    }

    public void setKpiCategory(String kpiCategory) {
        this.kpiCategory = kpiCategory;
    }

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

    public String getCompareMethod() {
		return compareMethod;
	}

	public void setCompareMethod(String compareMethod) {
		this.compareMethod = compareMethod;
	}
	/*@Override
	public String toString() {
		return "KpiItem [id=" + id + ", kpiReportTemplate=" + kpiReportTemplate + ", kpiQueryScript=" + kpiQueryScript
				+ ", kpiName=" + kpiName + ", kpiCode=" + kpiCode + ", dataSourceName=" + dataSourceName
				+ ", nextOperation=" + nextOperation + "]";
	}*/
	

}
