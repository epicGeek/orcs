package com.dhss.app.boss.domain;

import java.io.File;
import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.dhss.app.boss.task.util.GzUtils;

@Entity
public class BossCounterMarker {
	@Id
	@GeneratedValue
	private Long id;
    private String soapGwName;
    private String fileAbsPath;
    private Integer startLine;
	public Long getId() {
		return id;
	}
	
	

	public String getFileAbsPath() {
		return fileAbsPath;
	}



	public void setFileAbsPath(String fileAbsPath) {
		this.fileAbsPath = fileAbsPath;
	}



	public void setId(Long id) {
		this.id = id;
	}
	public String getSoapGwName() {
		return soapGwName;
	}
	public void setSoapGwName(String soapGwName) {
		this.soapGwName = soapGwName;
	}
	public Integer getStartLine() {
		return startLine;
	}
	public void setStartLine(Integer startLine) {
		this.startLine = startLine;
	}
    
	public File getSoapFile() throws IOException{
		if(!this.getFileAbsPath().endsWith(".gz")){
			return new File(this.fileAbsPath);
		}else{
			return GzUtils.gzToFile(new File(this.fileAbsPath));
		}
	}
	
}
