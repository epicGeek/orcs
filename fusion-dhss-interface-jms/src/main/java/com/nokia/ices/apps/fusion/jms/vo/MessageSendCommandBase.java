package com.nokia.ices.apps.fusion.jms.vo;

/**
 * @author Mars
 * @date 2014-02-24
 *
 */
public class MessageSendCommandBase extends MessageBase {

    //指令名称
    private String commandName;
    
    //指令超时时间，单位：分钟 有效范围：10----60
    private int exeTimeoutMinutes;

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public int getExeTimeoutMinutes() {
        return exeTimeoutMinutes;
    }

    public void setExeTimeoutMinutes(int exeTimeoutMinutes) {
        this.exeTimeoutMinutes = exeTimeoutMinutes;
    }
    
}
