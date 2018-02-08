package com.nokia.ices.apps.fusion.subtool.service;

import java.util.List;


public interface EditVrlOrSgsnService {
	
	/**
	 * 通过soap 协议更改Vlr或者SGSN 命令
	 * @param type 更改类型：VLR/SGSN
	 * @param value imsi或者msisdn
	 * @param checkName 检查项
	 * @param pgwIp
	 */
	public void editVlrOrSgsn(String value,List<String> pgwIp,String checkName);

}
