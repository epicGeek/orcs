package com.nokia.boss.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.dom4j.DocumentException;

public interface AnalysedSerivice {
	
	public void soapLogDataAnalysis(File soapLogFile,String soapGwName) throws IOException;
	public void errLogDataAnalysis(File errLogFile,String soapGwName) throws IOException, DocumentException;
	/*******************Unicom version********************/
	public void soapLogDataAnalysisUnicom(File soapLogFile,String soapGwName) throws IOException;
	public void errLogDataAnalysisUnicom(File errLogFile,String soapGwName) throws IOException, ParseException;
}
