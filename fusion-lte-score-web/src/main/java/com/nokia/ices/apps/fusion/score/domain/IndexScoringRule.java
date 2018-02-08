package com.nokia.ices.apps.fusion.score.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//指标评分规则
@SuppressWarnings("serial")
@Entity
@Table(name="ices_kpi_rule")
public class IndexScoringRule implements Serializable{

	//@NotEmpty(message = "指标ID不能为空")
	private String kpiId;  //KPI ID
	private String cnName; //KPI指标中文名称
	private String scoreRule; //评分规则
	private Integer kpiCategory; //指标类型  //指标类型不能为空
	private Integer cycle;    //周期 单位小时 默认1
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date createTime; //创建时间
	private Integer hasThreshold; //'是否有阈值(0:没有,1:有)',  不能为空
    private Integer isNotice;   //'是否需要通知(0:不需要,1需要)',
    private String  formula;  //计算公式'
    private String  omcTb;   //数据来源表(OMC库)',
    private String  description;  //'指标描述',
    private String EnName; //指标英文名称
    
    private double threshold; //不能为空 分数为0的标准 
    private String relationThreshold; //' 关系运算符 (<,<=,>,>=,=,!=)',
    
    private Integer minValOne;
    private Integer minValTwo;
    private Integer minValThree;
    private Integer minValFour;
    private Integer minValFive;
    private Integer minValSix;
    
    private Integer maxValOne;
    private Integer maxValTwo;
    private Integer maxValThree;
    private Integer maxValFour;
    private Integer maxValFive;
    private Integer maxValSix;
    
    private String operatorMin; //'关系运算符 区间小值',
    private String operatorMax; //'关系运算符',
    
    private Integer scoreThreshold;//满足不扣分的分数值
    private Integer scoreOne;
    private Integer scoreTwo;
    private Integer scoreThree;
    private Integer scoreFour;
    private Integer scoreFive;
    private Integer scoreSix;
    
    @Id
	@GeneratedValue 
	private Long id;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKpiId() {
		return kpiId;
	}
	public void setKpiId(String kpiId) {
		this.kpiId = kpiId;
	}
	public String getEnName() {
		return EnName;
	}
	public void setEnName(String enName) {
		EnName = enName;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getScoreRule() {
		return scoreRule;
	}
	public void setScoreRule(String scoreRule) {
		this.scoreRule = scoreRule;
	}

	public Integer getKpiCategory() {
		return kpiCategory;
	}
	public void setKpiCategory(Integer kpiCategory) {
		this.kpiCategory = kpiCategory;
	}
	
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	public Integer getCycle() {
		return cycle;
	}
	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getHasThreshold() {
		return hasThreshold;
	}
	public void setHasThreshold(Integer hasThreshold) {
		this.hasThreshold = hasThreshold;
	}
	public Integer getIsNotice() {
		return isNotice;
	}
	public void setIsNotice(Integer isNotice) {
		this.isNotice = isNotice;
	}

	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getOmcTb() {
		return omcTb;
	}
	public void setOmcTb(String omcTb) {
		this.omcTb = omcTb;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRelationThreshold() {
		return relationThreshold;
	}
	public void setRelationThreshold(String relationThreshold) {
		this.relationThreshold = relationThreshold;
	}
	public Integer getMinValOne() {
		return minValOne;
	}
	public void setMinValOne(Integer minValOne) {
		this.minValOne = minValOne;
	}
	public Integer getMinValTwo() {
		return minValTwo;
	}
	public void setMinValTwo(Integer minValTwo) {
		this.minValTwo = minValTwo;
	}
	public Integer getMinValThree() {
		return minValThree;
	}
	public void setMinValThree(Integer minValThree) {
		this.minValThree = minValThree;
	}
	public Integer getMinValFour() {
		return minValFour;
	}
	public void setMinValFour(Integer minValFour) {
		this.minValFour = minValFour;
	}
	public Integer getMinValFive() {
		return minValFive;
	}
	public void setMinValFive(Integer minValFive) {
		this.minValFive = minValFive;
	}
	public Integer getMinValSix() {
		return minValSix;
	}
	public void setMinValSix(Integer minValSix) {
		this.minValSix = minValSix;
	}
	public Integer getMaxValOne() {
		return maxValOne;
	}
	public void setMaxValOne(Integer maxValOne) {
		this.maxValOne = maxValOne;
	}
	public Integer getMaxValTwo() {
		return maxValTwo;
	}
	public void setMaxValTwo(Integer maxValTwo) {
		this.maxValTwo = maxValTwo;
	}
	public Integer getMaxValThree() {
		return maxValThree;
	}
	public void setMaxValThree(Integer maxValThree) {
		this.maxValThree = maxValThree;
	}
	public Integer getMaxValFour() {
		return maxValFour;
	}
	public void setMaxValFour(Integer maxValFour) {
		this.maxValFour = maxValFour;
	}
	public Integer getMaxValFive() {
		return maxValFive;
	}
	public void setMaxValFive(Integer maxValFive) {
		this.maxValFive = maxValFive;
	}
	public Integer getMaxValSix() {
		return maxValSix;
	}
	public void setMaxValSix(Integer maxValSix) {
		this.maxValSix = maxValSix;
	}
	public String getOperatorMin() {
		return operatorMin;
	}
	public void setOperatorMin(String operatorMin) {
		this.operatorMin = operatorMin;
	}
	public String getOperatorMax() {
		return operatorMax;
	}
	public void setOperatorMax(String operatorMax) {
		this.operatorMax = operatorMax;
	}
	public Integer getScoreThreshold() {
		return scoreThreshold;
	}
	public void setScoreThreshold(Integer scoreThreshold) {
		this.scoreThreshold = scoreThreshold;
	}
	public Integer getScoreOne() {
		return scoreOne;
	}
	public void setScoreOne(Integer scoreOne) {
		this.scoreOne = scoreOne;
	}
	public Integer getScoreTwo() {
		return scoreTwo;
	}
	public void setScoreTwo(Integer scoreTwo) {
		this.scoreTwo = scoreTwo;
	}
	public Integer getScoreThree() {
		return scoreThree;
	}
	public void setScoreThree(Integer scoreThree) {
		this.scoreThree = scoreThree;
	}
	public Integer getScoreFour() {
		return scoreFour;
	}
	public void setScoreFour(Integer scoreFour) {
		this.scoreFour = scoreFour;
	}
	public Integer getScoreFive() {
		return scoreFive;
	}
	public void setScoreFive(Integer scoreFive) {
		this.scoreFive = scoreFive;
	}
	public Integer getScoreSix() {
		return scoreSix;
	}
	public void setScoreSix(Integer scoreSix) {
		this.scoreSix = scoreSix;
	}
	
}