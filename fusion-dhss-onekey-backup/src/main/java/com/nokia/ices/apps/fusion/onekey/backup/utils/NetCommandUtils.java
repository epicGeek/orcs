package com.nokia.ices.apps.fusion.onekey.backup.utils;

import com.jcraft.jsch.*;
import com.nokia.ices.apps.fusion.onekey.backup.mbmodel.BackupCommandInfo;
import com.nokia.ices.apps.fusion.onekey.backup.mbmodel.BackupRSInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;



import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

/**
 * 基于网络网络协议SSH,TELNET进行的登录和执行指令
 * 的操作封装
 * User: stev.zhang
 * Date: 2015/5/22
 * Time: 8:45
 */
public class NetCommandUtils {
    /*****
     * 连接远程服务器，并执行命令
     * ****/
    private  Session getSShConnection(BackupRSInfo rsInfo) throws Exception{
        Session session = null;
        if(rsInfo!=null && rsInfo.checkParams(false)){
            JSch.setLogger(new JschLogger());
            JSch currJsch=new JSch();
            try {
                session=currJsch.getSession(rsInfo.getUserName(),rsInfo.getHost(),rsInfo.getHostPort());
                session.setUserInfo(rsInfo.getSSHUserInfo(false));
                session.connect(rsInfo.getConnectionTimeOut());
            } catch (JSchException e) {
             //  logger.error(e);
                rsInfo.setExceptionMessages(e.getMessage());
                throw e;
            }
        }
        return session;
    }
    /*****
     * 执行命令结合,存储执行过程中的日志信息以及结果集
     * @param rsInfo 连接远程服务器的配置信息
     * ******/
    public void executeSSHCommands(BackupRSInfo rsInfo) throws Exception{
        if(rsInfo!=null && rsInfo.checkParams(true)){
            Session session=getSShConnection(rsInfo);
            if(session!=null){
                List<BackupCommandInfo> backupCommandInfos=rsInfo.getBackupCommandInfoList();
                Channel channel=null;
                InputStream in=null;
                OutputStream outputStream=null;
                boolean callProcessResult=true;
                for(BackupCommandInfo backupCommandInfo:backupCommandInfos){//循环执行配置的指令
                     try{
                         channel=session.openChannel(backupCommandInfo.getSshCommandType());
                         outputStream=channel.getOutputStream();
                         //如果是exec转换channel
                         if(StringUtils.equalsIgnoreCase("exec",backupCommandInfo.getSshCommandType())){
                             ChannelExec channelExec=(ChannelExec)channel;
                             channelExec.setCommand(backupCommandInfo.getCommand());
                             channelExec.setErrStream(backupCommandInfo.getErrorByteArrayOutputStream());
                             in=channelExec.getInputStream();
                             //channelExec.connect(backupCommandInfo.getConnectTimeOut());
                             channelExec.connect();

//                             outputStream.write();
//                             channelExec.setInputStream(null);
                         }else if(StringUtils.equalsIgnoreCase("sftp",backupCommandInfo.getSshCommandType())){ //复制文件
                             ChannelSftp sftp=(ChannelSftp)channel;
                            // sftp.connect(backupCommandInfo.getConnectTimeOut());
                             sftp.connect();
                             /*if(StringUtils.equalsIgnoreCase("get", backupCommandInfo.getCommand()){
                                sftp.;
                             }*/
                             String remoteFolder=StringUtils.stripToNull(backupCommandInfo.getRemoteFolder());
                             String remoteFileName=StringUtils.stripToNull(backupCommandInfo.getRemoteFileName());
                             if(remoteFolder!=null && remoteFileName!=null) {
                                 sftp.cd(remoteFolder);
                                 String localFileName = UUID.randomUUID().toString() +"."+ StringUtils.substringAfterLast(remoteFileName,".");
                                 sftp.get(remoteFileName, backupCommandInfo.getResultBackupFolder() + File.separator + localFileName);
                                 backupCommandInfo.setResultFileName(localFileName);
                                 backupCommandInfo.setResultFileDisplayName(remoteFileName);
                                 callProcessResult=false;
                             }
                         }else {
                             in=channel.getInputStream();
                             channel.connect(backupCommandInfo.getConnectTimeOut());
                         }
                         if(callProcessResult) {
                             backupCommandInfo.setProcessLog("Process Command:" + backupCommandInfo.getCommand() + " Result:success!");
                             backupCommandInfo.processResult(in);
                         }
                     }catch (Exception ex){
                         backupCommandInfo.setProcessLog("Command:"+backupCommandInfo.getCommand()+"  Execute Failed,exception message:"+ex.getMessage()); //记录错误日志信息
                         throw ex;//抛出异常，中断后续命令的执行
                         //logger.error(ex);
                     } finally {
                        if(channel!=null){
                            channel.disconnect();
                        }
                     }
                }
            } else{
               // logger.error("无法获得连接远程的服务器信息!");
            }

            session.disconnect();
        }
    }
    /*****
     * 获取Telnet客户端程序
     * 返回连接后的telnet客户端
     * *****/
    public TelnetClient  getTelnetClient(BackupRSInfo rsInfo) throws Exception{
        TelnetClient telnetClient=null;
        if(rsInfo!=null || !rsInfo.checkParams(false)) {
            telnetClient=new TelnetClient(rsInfo.getTelnetTerminalType()); //获得Terminal type
            telnetClient.setConnectTimeout(rsInfo.getConnectionTimeOut());
            telnetClient.setCharset(Charset.forName("UTF-8"));
            telnetClient.setDefaultPort(23);

        }
        return telnetClient;
    }

    /****
     * 根据连接参数，基于telnet协议连接远程服务,并执行指令
     * 记录处理过程和结果到指令定义中
     * @param rsInfo 连接服务器配置以及待执行指令集信息
     * *****/
    public void executeTelentCommands(BackupRSInfo rsInfo) throws Exception{

        if(rsInfo!=null || !rsInfo.checkParams(false)) {
            //设置telnet client的连接参数,连接远程服务器
            TelnetClient telnetClient =null;
//            PrintStream outputStream=null;
            OutputStream outputStream=null;
            InputStream inputStream=null;
            char prompt = '$';
            try {
                telnetClient = getTelnetClient(rsInfo);
                telnetClient.connect(rsInfo.getHost(),rsInfo.getHostPort());
//                outputStream= new PrintStream(telnetClient.getOutputStream());
                outputStream= telnetClient.getOutputStream();
//                inputStream=telnetClient.getInputStream();
                //登录
                IOUtils.write(rsInfo.getUserName(),outputStream,"UTF-8");
                IOUtils.write(rsInfo.getPassword(),outputStream,"UTF-8");
                prompt = StringUtils.equals(rsInfo.getUserName(),"root") ? '#' : '>';
            } catch (Exception e) {
                rsInfo.setExceptionMessages(e.getMessage());
            }
            System.out.println("telnet Client connect::::"+telnetClient.isConnected());
            //如果telnet客户端不为空，则执行命令
            try {
                if (telnetClient!=null ){
                    //循环执行命令
                    for(BackupCommandInfo commandInfo:rsInfo.getBackupCommandInfoList()){
                          if(commandInfo!=null && StringUtils.stripToNull(commandInfo.getCommand())!=null){
                              IOUtils.write(commandInfo.getCommand(),outputStream,"UTF-8");
                              outputStream.flush();
                              inputStream=telnetClient.getInputStream();
                              commandInfo.processResult(inputStream);
                          }
                    }

/*
                    //写入命令
                    IOUtils.write("open baidu.com >/root/test.txt", outputStream, "UTF-8");
//                    IOUtils.write("\r\n",outputStream,"UTF-8");
//                    outputStream.println("pwd > /test.txt");
//                    outputStream.flush();
                    inputStream=telnetClient.getInputStream();
                    System.out.println("in available......"+inputStream.available());
                    byte[] buffer=null;
                    if(inputStream.available()>0) {
                        buffer=new byte[inputStream.available()];
                        IOUtils.readFully(inputStream,buffer);
                        System.out.println(new String(buffer));
                    }
                    IOUtils.write("get apl",outputStream,"UTF-8");
                    System.out.println("get in available......"+inputStream.available());
                    if(inputStream.available()>0) {
                        List<String> result = IOUtils.readLines(inputStream, "UTF-8");
                        for (String tmp : result) {
                            System.out.println(tmp);
                        }
                    }*/
                } else{
                    rsInfo.setExceptionMessages("没有连接上远程服务器!");
                }
            } catch (Exception e){

            }finally {
                if(telnetClient!=null && telnetClient.isConnected()){
                    telnetClient.disconnect();
                }
            }

        }
    }
    public static  void main(String[] args){
       /* NetCommandUtils instance=new NetCommandUtils();
        BackupRSInfo serverInfo=new BackupRSInfo();
        serverInfo.setHost("10.219.100.101");
//        serverInfo.setHost("localhost");
        serverInfo.setHostPort(22);
        serverInfo.setUserName("root");
        serverInfo.setPassword("password");
        serverInfo.setTelnetTerminalType("VT100");
        List<BackupCommandInfo> backupCommandInfos=new ArrayList<BackupCommandInfo>();
        BackupCommandInfo commandInfo=new BackupCommandInfo();
        commandInfo.setCommand("cd / ; touch aa.txt");
//        commandInfo.setCommand("cd /; touch aa.txt");
        commandInfo.setSshCommandType("exec");
        backupCommandInfos.add(commandInfo);*/

     /*   BackupCommandInfo commandInfo1=new BackupCommandInfo();
        commandInfo1.setCommand("touch aa.txt");
        commandInfo1.setSshCommandType("exec");
        backupCommandInfos.add(commandInfo1);*//*

        serverInfo.setBackupCommandInfoList(backupCommandInfos);

        instance.executeSSHCommand(serverInfo)*/;
      /*  try {
            instance.executeTelentCommands(serverInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("11111111111111_"+ UUID.randomUUID().toString());*/
        /*Session session=instance.getSShConnection(serverInfo);

        if (session != null) {
            try {
                session.connect(serverInfo.getConnectionTimeOut());
                Channel channel= session.openChannel(commandInfo.getSshCommandType());
                if(channel!=null){
                    ChannelExec channelExec=(ChannelExec)channel;
                    channelExec.setCommand(commandInfo.getCommand());
                    channelExec.setErrStream(System.err,true);
                    channelExec.setInputStream(null);

                 *//*   InputStream in = channelExec.getExtInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String buf = null;
                    while ((buf = reader.readLine()) != null) {
                        result+= new String(buf.getBytes("gbk"),"UTF-8")+"    <br>\r\n";
                    }*//*
                    channelExec.connect();
                    channelExec.disconnect();
                }
            } catch (JSchException e) {
                e.printStackTrace();
            } catch (Exception ex){
                 ex.printStackTrace();
            }finally {
                session.disconnect();
            }
        }*/
    }

    class JschLogger implements com.jcraft.jsch.Logger {
        private java.util.Hashtable name=new java.util.Hashtable();
        public JschLogger(){
            name.put(new Integer(DEBUG), "DEBUG:");
            name.put(new Integer(INFO), "INFO:");
            name.put(new Integer(WARN), "WARN:");
            name.put(new Integer(ERROR), "ERROR:");
            name.put(new Integer(FATAL), "FATAL:");

        }
        public boolean isEnabled(int level){
            return true;
        }
       public void log(int level, String message){
//            switch (level){
//                case DEBUG:
//                    logger.debug(message);
//                    break;
//                case INFO:
//                    logger.info(message);
//                    break;
//                case WARN:
//                    logger.warn(message);
//                    break;
//                case ERROR:
//                    logger.error(message);
//                    break;
//                case FATAL:
//                    logger.fatal(message);
//                    break;
//                default:
//                    logger.info(message);
//            }
       }
    }
    //private static final Logger logger= LogManager.getLogger(NetCommandUtils.class);
}
