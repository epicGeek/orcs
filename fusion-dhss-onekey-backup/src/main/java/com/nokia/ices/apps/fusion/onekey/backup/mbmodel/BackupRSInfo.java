package com.nokia.ices.apps.fusion.onekey.backup.mbmodel;

import com.jcraft.jsch.UserInfo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 一键备份过程中连接远程服务器使用到的配置以及待执行的指令信息
 *  其中使用的协议类型(protocolType)：ssh,telent 默认使用ssh
 * User: stev.zhang
 * Date: 2015/5/22
 * Time: 15:47
 */
public class BackupRSInfo {
    private String host;
    private int  hostPort=22;//telnet默认使用的端口23
//    private int hostTelnetPort=23;
    private String userName="root"; //用户默认使用root
    private int connectionTimeOut=0;
    private String  password;//连接远程服务器的密码
    private BackupSSHUserInfo backupSSHUserInfo;

    private List<BackupCommandInfo> backupCommandInfoList; //待执行命令集合

    private String protocolType;//使用的协议

    private String telnetTerminalType;// telnet terminal type:VT100,VT52,VTNT，ANSI default:VT100,

    private String exceptionMessages;//在执行连接时发生的异常

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BackupCommandInfo> getBackupCommandInfoList() {
        return backupCommandInfoList;
    }

    public void setBackupCommandInfoList(List<BackupCommandInfo> backupCommandInfoList) {
        this.backupCommandInfoList = backupCommandInfoList;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public void setTelnetTerminalType(String telnetTerminalType) {
        this.telnetTerminalType = telnetTerminalType;
    }

    public String getTelnetTerminalType() {
        if(org.apache.commons.lang3.StringUtils.stripToNull(telnetTerminalType)==null){
            telnetTerminalType="VT100";
        }
        return telnetTerminalType;
    }

    public String getExceptionMessages() {
        return exceptionMessages;
    }

    public void setExceptionMessages(String exceptionMessages) {
        this.exceptionMessages = exceptionMessages;
    }

    /***
     * 初步校验参数的非空性
     * @param checkAuthorInfo 是否对连接验证信息的非空性进行校验
     * @return boolean 校验是否通过
     *
     * *****/
    public boolean checkParams(boolean checkAuthorInfo){
        boolean result=true;
        //服务信息时必填项，可以是IP也可以是域名,所以不对格式进行判断
        if(StringUtils.stripToNull(host)==null){
            result=false;
        }
        //如果端口小于0，则默认为不正确
        if(getHostPort()<0){
           result=false;
        }
        //如果需要验证登录用户的信息,则验证用户的密码是否为空
        if(checkAuthorInfo){
          if(StringUtils.stripToNull(getPassword())==null){
              result=false;
          }
        }
        //可执行命令必须不能为空
        if(backupCommandInfoList==null || backupCommandInfoList.size()<0){
            result=false;
        }
        return result;
    }


    public BackupSSHUserInfo getSSHUserInfo(boolean createNew){
        if(createNew || backupSSHUserInfo==null){
            backupSSHUserInfo=new BackupSSHUserInfo();
            backupSSHUserInfo.setPassword(getPassword());
        }
       return backupSSHUserInfo;
    }

    class BackupSSHUserInfo implements UserInfo{
        private String password;

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean promptPassword(String message) {
            return true;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptYesNo(String message) {
            return true;
        }

        @Override
        public void showMessage(String message) {

        }
    }

}
