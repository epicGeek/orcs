package com.nokia.ices.apps.fusion.score.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/*
 * 分数等级
 * */
@SuppressWarnings("serial")
@Entity
@Table(name="scorelevel")
public class Scorelevel implements Serializable{

	
	//private static final long serialVersionUID = 1L;
	
	private Integer scorefrom;           
	private Integer scoreto;
	private Integer level;    //等级
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date updatetime; //修改时间
	@Id
	@GeneratedValue  
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getScorefrom() {
		return scorefrom;
	}
	public void setScorefrom(Integer scorefrom) {
		this.scorefrom = scorefrom;
	}
	public Integer getScoreto() {
		return scoreto;
	}
	public void setScoreto(Integer scoreto) {
		this.scoreto = scoreto;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	
}
