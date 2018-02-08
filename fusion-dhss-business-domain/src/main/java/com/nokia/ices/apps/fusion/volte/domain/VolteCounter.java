package com.nokia.ices.apps.fusion.volte.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VolteCounter {
	@Id
	@GeneratedValue
	private Long id;
	private Long subs_hss_2_boss;
	private String rates_hss_2_boss;
	private Long subs_boss_2_hss_t;
	private String rates_boss_2_hss_t;
	private Long subs_boss_2_hss_o;
	private String rates_boss_2_hss_o;
	private Integer ads_boss_hss;
	private Date start_time;
	private Date stop_time;
	private Double interval_time;
	private String DN;
	private String dhss;
	private String fileAbsDir;

	private Double rates_hss_2_boss_num;
	private Double rates_boss_2_hss_t_num;
	private Double rates_boss_2_hss_o_num;
	

	public String getFileAbsDir() {
		return fileAbsDir;
	}
	public void setFileAbsDir(String fileAbsDir) {
		this.fileAbsDir = fileAbsDir;
	}
	public Double getRates_hss_2_boss_num() {
		return rates_hss_2_boss_num;
	}
	public void setRates_hss_2_boss_num(Double rates_hss_2_boss_num) {
		this.rates_hss_2_boss_num = rates_hss_2_boss_num;
	}
	public Double getRates_boss_2_hss_t_num() {
		return rates_boss_2_hss_t_num;
	}
	public void setRates_boss_2_hss_t_num(Double rates_boss_2_hss_t_num) {
		this.rates_boss_2_hss_t_num = rates_boss_2_hss_t_num;
	}
	public Double getRates_boss_2_hss_o_num() {
		return rates_boss_2_hss_o_num;
	}
	public void setRates_boss_2_hss_o_num(Double rates_boss_2_hss_o_num) {
		this.rates_boss_2_hss_o_num = rates_boss_2_hss_o_num;
	}
	public String getDhss() {
		return dhss;
	}
	public void setDhss(String dhss) {
		this.dhss = dhss;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSubs_hss_2_boss() {
		return subs_hss_2_boss;
	}
	public void setSubs_hss_2_boss(Long subs_hss_2_boss) {
		this.subs_hss_2_boss = subs_hss_2_boss;
	}
	public String getRates_hss_2_boss() {
		return rates_hss_2_boss;
	}
	public void setRates_hss_2_boss(String rates_hss_2_boss) {
		this.rates_hss_2_boss = rates_hss_2_boss;
	}
	public Long getSubs_boss_2_hss_t() {
		return subs_boss_2_hss_t;
	}
	public void setSubs_boss_2_hss_t(Long subs_boss_2_hss_t) {
		this.subs_boss_2_hss_t = subs_boss_2_hss_t;
	}
	public String getRates_boss_2_hss_t() {
		return rates_boss_2_hss_t;
	}
	public void setRates_boss_2_hss_t(String rates_boss_2_hss_t) {
		this.rates_boss_2_hss_t = rates_boss_2_hss_t;
	}
	public Long getSubs_boss_2_hss_o() {
		return subs_boss_2_hss_o;
	}
	public void setSubs_boss_2_hss_o(Long subs_boss_2_hss_o) {
		this.subs_boss_2_hss_o = subs_boss_2_hss_o;
	}
	public String getRates_boss_2_hss_o() {
		return rates_boss_2_hss_o;
	}
	public void setRates_boss_2_hss_o(String rates_boss_2_hss_o) {
		this.rates_boss_2_hss_o = rates_boss_2_hss_o;
	}
	public Integer getAds_boss_hss() {
		return ads_boss_hss;
	}
	public void setAds_boss_hss(Integer ads_boss_hss) {
		this.ads_boss_hss = ads_boss_hss;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getStop_time() {
		return stop_time;
	}
	public void setStop_time(Date stop_time) {
		this.stop_time = stop_time;
	}
	
	public Double getInterval_time() {
		return interval_time;
	}
	public void setInterval_time(Double interval_time) {
		this.interval_time = interval_time;
	}
	public String getDN() {
		return DN;
	}
	public void setDN(String dN) {
		DN = dN;
	}
	
	
	
}
