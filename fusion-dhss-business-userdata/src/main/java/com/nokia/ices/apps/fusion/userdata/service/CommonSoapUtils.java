package com.nokia.ices.apps.fusion.userdata.service;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonSoapUtils {

	private static final Logger logger = LoggerFactory.getLogger(CommonSoapUtils.class);
	public static void main(String[] args) {
		List<EquipmentUnit> urlList = new ArrayList<>();
		EquipmentUnit e = new EquipmentUnit();
		e.setServerIp("10.11.12.123");
		urlList.add(e);
		Object[] o = getUserXml("1","8613520270239",urlList);
		System.out.println(o);
	}
	public static Object[] getUserXml(String type, String value, List<EquipmentUnit> urlList) {
		logger.debug("参数{操作类型=" + type + "  值=" + value + "}  pgwipSize=" + urlList.size());

		SOAPConnection connection = null;
		Object[] result = new Object[2];
		try {
			SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
			connection = soapConnFactory.createConnection();

			MessageFactory messageFactory = MessageFactory.newInstance();

			SOAPMessage message = messageFactory.createMessage();

			SOAPPart soapPart = message.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			SOAPBody body = envelope.getBody();
			SOAPElement bodyElement = body.addChildElement(
					envelope.createName("searchRequest", "spml", "urn:siemens:names:prov:gw:SPML:2:0"));

			bodyElement.addChildElement("version").addTextNode("SUBSCRIBER_v10");
			SOAPElement baseElemnt = bodyElement.addChildElement(envelope.createName("base"));
			baseElemnt.setAttribute("xsi:type", "spml:SearchBase");
			baseElemnt.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			SOAPElement objectclass = baseElemnt.addChildElement("objectclass");
			objectclass.addTextNode("Subscriber");
			SOAPElement aliasElemnt = baseElemnt.addChildElement("alias");
			if ("2".equals(type)) {
				aliasElemnt.setAttribute("name", "imsi");
				aliasElemnt.setAttribute("value", value);
			} else {
				aliasElemnt.setAttribute("name", "msisdn");
				aliasElemnt.setAttribute("value", value);
			}

			message.saveChanges();

			message.writeTo(System.out);
			result = sendSoapRequest(message, connection, urlList, result);

			if (null != connection) {
				try {
					connection.close();
				} catch (SOAPException e) {
					logger.debug(e.toString());
				}
				connection = null;
			}
		} catch (Exception e) {
			logger.debug(e.toString());

			if (null != connection) {
				try {
					connection.close();
				} catch (SOAPException s) {
					logger.debug("SOAPException error:" + e.toString());
				}
				connection = null;
			}
		} finally {
			if (null != connection) {
				try {
					connection.close();
				} catch (SOAPException e) {
					logger.debug(e.toString());
				}
				connection = null;
			}
		}

		return result;
	}

	private static Object[] sendSoapRequest(SOAPMessage message, SOAPConnection connection, List<EquipmentUnit> url,
			Object[] result) {
		if (null != message) {
			int size = url.size();
			for (int i = 0; i < size; i++) {
				try {
					String address = ((EquipmentUnit) url.get(i)).getServerIp();
						String sendAdd = "http://" + address + ":8089" 
				+ "/ProvisioningGateway/services/SPMLSubscriber10Service";
				//	String sendAdd = "http://" + address + ":" + ProjectProperties.getSoap_port()
//							+ "/ProvisioningGateway/services/SPMLSubscriber10Service";


					logger.debug("send request is url=" + sendAdd);

					SOAPMessage reply = connection.call(message, sendAdd);
					if (reply != null) {
						Source source = reply.getSOAPPart().getContent();
						Transformer transformer = TransformerFactory.newInstance().newTransformer();
						ByteArrayOutputStream myOutStr = new ByteArrayOutputStream();
						StreamResult res = new StreamResult();
						res.setOutputStream(myOutStr);
						transformer.transform(source, res);
						String soapxml = myOutStr.toString("UTF-8");
						logger.debug(address+"=== result soapxml:" + soapxml);
						if (soapxml.contains("<errorMessage>")) {
							if (i == size - 1) {
								String error = getContext("errorMessage", soapxml);
								result[0] = "error";
								logger.debug("errorMessage--------------->:" + error);
							}
						} else {
							result[0] = soapxml;
							result[1] = url.get(i);
							break;
						}
					}
				} catch (Exception e) {
					logger.debug("error fial.... " + e.toString());
				}
			}
		}

		return result;
	}

	public static String getContext(String tag, String html) {
		List<String> resultList = new ArrayList<String>();

		Pattern p = Pattern.compile("<" + tag + "[^>]*>([^<]*)</" + tag + ">");
		Matcher m = p.matcher(html);
		while (m.find()) {
			resultList.add(m.group(1));
		}
		return resultList.toString();
	}

	public static void fileWriter(String xmlPath, String xml) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlPath), "UTF-8"));
			writer.write(xml);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error("Exception error:"+e.getMessage());
		}
	}

	public static String formatXML(String inputXML) throws IOException, DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(new StringReader(inputXML));
		String requestXML = null;
		XMLWriter writer = null;
		if (document != null) {
			try {
				StringWriter stringWriter = new StringWriter();
				OutputFormat format = new OutputFormat(" ", true);
				writer = new XMLWriter(stringWriter, format);
				writer.write(document);
				writer.flush();
				requestXML = stringWriter.getBuffer().toString();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
		return requestXML;
	}

}
