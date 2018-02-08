package com.nokia.ices.apps.fusion.common.connector;

/**
 * 登陆终端身份信息
 * @author kongdy
 *
 */
public class Account {

	/**
	 * 本次连接标识
	 */
	private String cid;

	/**
	 * 建立连接提示信息
	 */
	private String tip;

	public Account(String cid) {
		this.cid = cid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

}
