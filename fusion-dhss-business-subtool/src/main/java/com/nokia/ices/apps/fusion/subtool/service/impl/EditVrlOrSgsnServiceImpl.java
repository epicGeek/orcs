package com.nokia.ices.apps.fusion.subtool.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;*/
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nokia.ices.apps.fusion.equipment.service.NumberSectionService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.subtool.domain.CheckSubtoolResult;
import com.nokia.ices.apps.fusion.subtool.repository.SubtoolRepository;
import com.nokia.ices.apps.fusion.subtool.service.EditVrlOrSgsnService;

@Service("editVrlOrSgsnService")
public class EditVrlOrSgsnServiceImpl implements EditVrlOrSgsnService {

	private final static String SERVER_PORT = "7777";
	private final static String USER_NAME = "jzwhpt";
	private final static String PASS_WORD = "Nsn1234!";
	private final static String SUCCESS = "0";
	private final static String FAILURE = "1";

	@Autowired
	NumberSectionService numberSectionService;

	@Autowired
	SubtoolRepository subtoolRepository;

	private final static Logger logger = LoggerFactory.getLogger(EditVrlOrSgsnServiceImpl.class);

	@Override
	public void editVlrOrSgsn(String command, List<String> pgwIp, String checkName) {

		logger.debug("start soap command = " + command);
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		CheckSubtoolResult sub = new CheckSubtoolResult();
		String[] _value = command.split(" ");
		try {
			httpclient = HttpClients.createDefault();
			/**
			 * 参数顺序：msisdn imsi,hlrsn,vlrno,mscno,sgsno 
			 * subtool ZMIMVLR msisdn 460029243509684  54 8612345678 8612345678
			 * subtool ZMIMSGSN msisdn 460029243509684 54 8612345678
			 * 
			 */
			StringBuffer soapRequestData = new StringBuffer();
			soapRequestData.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			soapRequestData.append(
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hss=\"http://www.chinamobile.com/HSS/\">");
			soapRequestData.append("<soapenv:Header>");
			soapRequestData.append("<hss:PassWord>" + PASS_WORD + "</hss:PassWord>");
			soapRequestData.append("<hss:UserName>" + USER_NAME + "</hss:UserName>");
			soapRequestData.append("</soapenv:Header>");
			soapRequestData.append("<soapenv:Body>");
			soapRequestData.append("<hss:MOD_LCADDRESS>");
			soapRequestData.append("<hss:HLRSN>" + _value[4] + "</hss:HLRSN>");
			soapRequestData.append("<hss:IMSI>" + _value[3] + "</hss:IMSI>");
			soapRequestData.append("<hss:MSISDN>" + _value[2] + "</hss:MSISDN>");
			if (command.contains("ZMIMSGSN")) {
				soapRequestData.append("<hss:SGSNO>" + _value[5] + "</hss:SGSNO>");
			} else if (command.contains("ZMIMVLR")) {
				soapRequestData.append("<hss:VLRNO>" + _value[5] + "</hss:VLRNO>");
				soapRequestData.append("<hss:MSCNO>" + _value[6] + "</hss:MSCNO>");
			}
			soapRequestData.append("</hss:MOD_LCADDRESS>");
			soapRequestData.append("</soapenv:Body>");
			soapRequestData.append("</soapenv:Envelope>");

			logger.debug("request message[" + soapRequestData.toString() + "]");

			int size = pgwIp.size();
			for(int i=0;i<size;i++){
				logger.debug("pgwip or soapip = "+pgwIp.get(i));
			}
			logger.debug("size  >>>>>>>>>>>>>>>>" + size);
			if (httpclient != null && size > 0) {
				String soaoIp = pgwIp.get(0).split(":")[1];
				String sendAdd = "http://" + soaoIp + ":" + SERVER_PORT + "?wsdl";
				logger.debug("soap url:" + sendAdd);
				HttpPost httppost = new HttpPost(sendAdd);
				StringEntity entiy = new StringEntity(soapRequestData.toString(), "UTF-8");

				httppost.setEntity(entiy);
				httppost.setHeader("Content-Type", "text/xml; charset=UTF-8");
				httppost.setHeader("SOAPAction", "http://WebXml.com.cn/getSupportCity");
				response = httpclient.execute(httppost);
				StatusLine StatusLine = response.getStatusLine();
				logger.debug("StatusLine =  "+StatusLine.toString());
				if (200 == StatusLine.getStatusCode()) {
					// 成功返回结果
					String soapxml = EntityUtils.toString(response.getEntity());
					// HttpEntity e = response.getEntity();
					String ResultDesc = getContext("ResultDesc", soapxml);
					if ("[SUCCESS]".equalsIgnoreCase(ResultDesc)) {
						sub.setExeResults(SUCCESS);
					} else {
						sub.setExeResults(FAILURE);
						sub.setErrorMessage(ResultDesc);
						logger.debug("once and for all errorMessage:" + ResultDesc);
					}
				} else if (command.contains("ZMIMSGSN")) {
					// 修改sgsn返回异常，视为成功
					sub.setExeResults(SUCCESS);
				}
			}

		} catch (Exception e) {
			/**
			 * 因第三方接口返回异常，本该是成功
			 */
			sub.setExeResults(SUCCESS);
			logger.error("httpclient soap request failed......." + e.toString());
		} finally {
			// Close the connection
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("httpclient close error......" + e.toString());
			}
		}
		
		// 持久化对象
		sub.setFilePath("The soap protocol get");
		sub.setCheckName(checkName);
		sub.setCreateTime(new Date());
		sub.setCreateName(shiroUser.getRealName());
		sub.setUserNumber(_value[2]);
		subtoolRepository.save(sub);
		
		logger.debug("end soap request ..................");
	}

	/**
	 * 提取错误信息
	 * 
	 * @param tag
	 * @param html
	 * @return
	 */
	public static String getContext(String tag, String html) {
		List<String> resultList = new ArrayList<String>();
		// 正则表达式 commend by danielinbiti
		Pattern p = Pattern.compile("<" + tag + "[^>]*>([^<]*)</" + tag + ">");
		Matcher m = p.matcher(html);//
		while (m.find()) {
			resultList.add(m.group(1));//
		}
		return resultList.toString();
	}

}
