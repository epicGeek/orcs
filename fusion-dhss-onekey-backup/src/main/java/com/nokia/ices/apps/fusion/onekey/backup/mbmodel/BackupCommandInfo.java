package com.nokia.ices.apps.fusion.onekey.backup.mbmodel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 登录远程服务器后，需要执行的指令
 * 如果使用SSH登录，创建Session后，打开的channel类型包含：
 * 1.session
 * 2.shell
 * 3.exec
 * 4.x11
 * 5.auth-agent@openssh.com
 * 6.direct-tcpip
 * 7.forwarded-tcpip
 * 8.sftp
 * 9.subsystem
 *
 * User: stev.zhang
 * Date: 2015/5/22
 * Time: 18:26
 */
public class BackupCommandInfo {
    private String command;
    private String sshCommandType;  //ssh命令类型

    private int connectTimeOut=30000;

    private String executeResult;//success ,fail
    private String processLog;

    private  String resultBackupFolder;//定义输出结果文件到本地文件夹路径

    private String resultFileName;//默认使用UUID+".txt"
    private String resultFileDisplayName;//默认使用日期+".txt"
    private String resultFileType;//设置的文件结果类型

    private String remoteFolder;
    private String remoteFileName;

    private ByteArrayOutputStream errorByteArrayOutputStream=new ByteArrayOutputStream();

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSshCommandType() {
        return sshCommandType;
    }

    public void setSshCommandType(String sshCommandType) {
        this.sshCommandType = sshCommandType;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public String getProcessLog() {
        return processLog;
    }

    public void setProcessLog(String processLog) {
        this.processLog = processLog;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public ByteArrayOutputStream getErrorByteArrayOutputStream() {
        return errorByteArrayOutputStream;
    }

    public void setErrorByteArrayOutputStream(ByteArrayOutputStream errorByteArrayOutputStream) {
        this.errorByteArrayOutputStream = errorByteArrayOutputStream;
    }

    public String getResultBackupFolder() {
        return resultBackupFolder;
    }

    public void setResultBackupFolder(String resultBackupFolder) {
        this.resultBackupFolder = resultBackupFolder;
    }

    public String getResultFileName() {
        return resultFileName;
    }

    public void setResultFileName(String resultFileName) {
        this.resultFileName = resultFileName;
    }

    public String getResultFileDisplayName() {
        return resultFileDisplayName;
    }

    public void setResultFileDisplayName(String resultFileDisplayName) {
        this.resultFileDisplayName = resultFileDisplayName;
    }

    public String getResultFileType() {
        return resultFileType;
    }

    public void setResultFileType(String resultFileType) {
        this.resultFileType = resultFileType;
    }

    public String getRemoteFolder() {
        return remoteFolder;
    }

    public void setRemoteFolder(String remoteFolder) {
        this.remoteFolder = remoteFolder;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }


    /*****
     * 处理SSH或者Telenet命令执行后的结果
     * 默认支持：
     * 1.读取流为字符串
     * 2.把读出的内容写往本地文件
     * *****/
    public void processResult(InputStream inputStream){
        //新的文件名称
        String fileName= UUID.randomUUID().toString();
        String fileResultType= StringUtils.stripToNull(getResultFileType())==null?".txt":getResultFileType();
        String backupFolder= StringUtils.stripToNull(getResultBackupFolder())==null?"./":getResultBackupFolder();
        File file=new File(backupFolder+File.separator+fileName+fileResultType);
        FileWriter fileWriter=null;
        if(inputStream!=null) {
            try {
                int i = inputStream.available();
                if (i > 0) {
                    fileWriter = new FileWriter(file);
                    int r = inputStream.read();
                    IOUtils.copy(inputStream, fileWriter, "UTF-8");
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                this.setProcessLog("Save Result to file error! errormsg:" + e.getMessage());
                this.setExecuteResult("fail");
            } finally {
                try {
//                    inputStream.close();如果使用telnet则不需要关闭
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

            }
        }
        this.setResultFileDisplayName(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"_backup"+fileResultType);
        this.setResultFileName(fileName+fileResultType);
        this.setProcessLog("Save Result success!");
        this.setExecuteResult("success");
    }

    private static final Logger logger= LoggerFactory.getLogger(BackupCommandInfo.class);
}
