package com.nokia.ices.apps.fusion.websocket;


/**
 * Output from ssh session
 */
public class SessionOutput {
    Long sessionId;
    Long hostSystemId;
    Integer instanceId;
    String output;



    public Long getHostSystemId() {
        return hostSystemId;
    }

    public void setHostSystemId(Long hostSystemId) {
        this.hostSystemId = hostSystemId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }


    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Integer getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }
}