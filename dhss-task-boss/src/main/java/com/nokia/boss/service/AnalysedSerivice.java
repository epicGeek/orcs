package com.nokia.boss.service;

import java.io.IOException;
import java.util.List;

import org.dom4j.DocumentException;

/**
 * 
 * 针对四种文件，有四种不同的处理方式
 * 
 */
public interface AnalysedSerivice {
	
	public void successDataAnalysisForLog(String successLogFileName,String soapGwName) throws IOException;
	public void successDataAnalysisForGz(String successGzFileName,String soapGwName) throws IOException;
	public void failureDataAnalysisForLog(String failureLogFileName,String soapGwName) throws IOException, DocumentException;
	public void failureDataAnalysisForGz(String failureGzFileName,String soapGwName) throws IOException, DocumentException;
	/*******************Unicom version********************/
	public void successDataAnalysisForLogUnicom(String successLogFileName,String soapGwName) throws IOException;
	public void successDataAnalysisForGzUnicom(String successGzFileName,String soapGwName) throws IOException;
	public void failureDataAnalysisForLogUnicom(String failureLogFileName,String soapGwName) throws IOException, DocumentException;
	public void failureDataAnalysisForGzUnicom(String failureGzFileName,String soapGwName) throws IOException, DocumentException;
}
