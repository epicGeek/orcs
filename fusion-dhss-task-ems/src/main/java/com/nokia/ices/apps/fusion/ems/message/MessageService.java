package com.nokia.ices.apps.fusion.ems.message;

import com.nokia.ices.apps.fusion.jms.vo.MessageBase;

public class MessageService extends MessageBase{
	
	private String type;
	
	private String log_path;
	
	private String script_type = "1";
	
	private String reply_type = "0";
	
	private String script_path;
	
	private String dynamic_script = "true";
	
	private String invariant;
	
	//网元名称
    private Integer msgCode;
    
    
    public Integer getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(Integer msgCode) {
        this.msgCode = msgCode;
    }
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLog_path() {
		return log_path;
	}

	public void setLog_path(String log_path) {
		this.log_path = log_path;
	}


	public String getInvariant() {
		return invariant;
	}

	public void setInvariant(String invariant) {
		this.invariant = invariant;
	}

	public String getScript_type() {
		return script_type;
	}

	public void setScript_type(String script_type) {
		this.script_type = script_type;
	}

	public String getReply_type() {
		return reply_type;
	}

	public void setReply_type(String reply_type) {
		this.reply_type = reply_type;
	}

	public String getScript_path() {
		return script_path;
	}

	public void setScript_path(String script_path) {
		this.script_path = script_path;
	}

	public String getDynamic_script() {
		return dynamic_script;
	}

	public void setDynamic_script(String dynamic_script) {
		this.dynamic_script = dynamic_script;
	}


}
