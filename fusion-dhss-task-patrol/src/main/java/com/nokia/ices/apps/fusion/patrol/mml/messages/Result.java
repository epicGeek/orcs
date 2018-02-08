/**
 */
package com.nokia.ices.apps.fusion.patrol.mml.messages;

import java.util.Calendar;

/**
 * 用于表示集中操作Active MQ返回结果
 * @author Mars
 * @date 2014-02-24
 */
public class Result
{
	private String resultCode;

	private String message;
	
	private String neId;

	private Calendar date;

	public Result(String resultCode, String message, String neId, Calendar date)
	{
		this.resultCode = resultCode;
		this.message = message;
		this.neId = neId;
		this.date = date;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNeId() {
		return neId;
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
}
