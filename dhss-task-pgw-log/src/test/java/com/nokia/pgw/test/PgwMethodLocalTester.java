package com.nokia.pgw.test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.pgw.util.PGWAnalyseUtil;

public class PgwMethodLocalTester {
	private static final Logger LOGGER = LoggerFactory.getLogger(PgwMethodLocalTester.class);
	private static final String FIELD_TERMINATOR = "~";
	private static final String LINE_TERMINATOR = ";;;";
	@Test
	public void hehe(){
		File f = new File("E:/pgwdata/provgw-spml_command.log.2016_03_23-23_56_22_524");
		PGWAnalyseUtil.compressFileToGz(f.getAbsolutePath());
	}
	public void tester() {
		try {
			String line = getPgwLogLine();
			Map<String, String> frontHalfLineAnalysisMap = new HashMap<>();
			frontHalfLineAnalysisMap = analysisPgwLogLineFront(line);
			String xmlPart = frontHalfLineAnalysisMap.get("pgwLogXmlPart").toString();
			Map<String, String> backHalfLineAnalysisMap = new HashMap<>();
			backHalfLineAnalysisMap = analysisPgwLogLineXml(xmlPart);
			System.out.println(analysisPgwLogLineXml(xmlPart));
		} catch (Exception e) {

		}
	}

	public void analysisTargetFiles(File analysisTargetFile) {
		try {
			FileReader fr = new FileReader(analysisTargetFile);
			BufferedReader br = new BufferedReader(fr);
			String lineText = null;
			while (null != (lineText = br.readLine())) {
				System.out.println(lineText);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.getMessage();
			LOGGER.info("File is not found:" + analysisTargetFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("A fatal IO exception:");
		}
	}

	public String getPgwLogLine() throws IOException {
		File f = new File("E:/pgwdata/line.txt");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String lineText = null;
		StringBuilder sb = new StringBuilder();
		while (null != (lineText = br.readLine())) {
			sb.append(lineText);
		}
		br.close();
		return sb.toString();
	}

	public Map<String, String> analysisPgwLogLineFront(String pgwLogLine) {
		Map<String, String> frontHalfTextAnalayseMap = new HashMap<>();
		String frontHalfLine = pgwLogLine.substring(0, pgwLogLine.indexOf("<"));
		String responseTime = frontHalfLine.substring(0, frontHalfLine.indexOf(","));
		frontHalfTextAnalayseMap.put("responseTime", responseTime);
		String[] frontHalfLineElements = frontHalfLine.split(" ");
		String pgwName = frontHalfLineElements[2];
		frontHalfTextAnalayseMap.put("pgwName", pgwName);
		String instanceName = frontHalfLineElements[3];
		frontHalfTextAnalayseMap.put("instanceName", instanceName);
		String userName = frontHalfLineElements[4];
		frontHalfTextAnalayseMap.put("userName", userName);
		String executionContent = frontHalfLineElements[5];
		frontHalfTextAnalayseMap.put("executionContent", executionContent);
		String pgwLogXmlPart = pgwLogLine.substring(pgwLogLine.indexOf("<"));
		frontHalfTextAnalayseMap.put("pgwLogXmlPart", pgwLogXmlPart);
		return frontHalfTextAnalayseMap;
	}

	public Map<String, String> analysisPgwLogLineXml(String pgwLogLineBackHalf)  {
		SAXReader reader = new SAXReader();
		StringReader in = new StringReader(pgwLogLineBackHalf);
		Map<String, String> pgwLogBackHalfAnalysisMap = new HashMap<>();
		try {
			Document doc = reader.read(in);
			OutputFormat formater = OutputFormat.createPrettyPrint();
			formater.setEncoding("UTF-8");
			StringWriter out = new StringWriter();
			XMLWriter writer = new XMLWriter(out, formater);
			writer.write(doc);
			writer.close();
			String formatedXml = out.toString();
			pgwLogBackHalfAnalysisMap.put("formatedXml", formatedXml);
			Element root = doc.getRootElement();
			String requestID = root.attribute("requestID").getText();
			pgwLogBackHalfAnalysisMap.put("requestID", requestID);
			String identifier = root.elementText("identifier");
			pgwLogBackHalfAnalysisMap.put("identifier", identifier);
			//TODO XML ANALYSIS HERE.
		} catch (DocumentException e) {
			e.getMessage();
			LOGGER.info(pgwLogLineBackHalf);
		} catch (IOException e) {
			e.getMessage();
			LOGGER.info(pgwLogLineBackHalf);
		}
		return pgwLogBackHalfAnalysisMap;
	}
	
}
