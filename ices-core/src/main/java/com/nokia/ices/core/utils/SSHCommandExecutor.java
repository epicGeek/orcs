package com.nokia.ices.core.utils;

import java.io.BufferedReader; 
import java.io.InputStreamReader; 
import java.util.Vector; 
   
import com.jcraft.jsch.Channel; 
import com.jcraft.jsch.ChannelExec; 
import com.jcraft.jsch.JSch; 
import com.jcraft.jsch.JSchException; 
import com.jcraft.jsch.Session; 
   
/**
 * This class provide interface to execute command on remote Linux.
 */ 
   
public class SSHCommandExecutor { 
    private String ipAddress; 
   
    private String username; 
   
    private String password; 
   
    public static final int DEFAULT_SSH_PORT = 22; 
   
    private Vector<String> stdout; 
   
    public SSHCommandExecutor(final String ipAddress, final String username, final String password) { 
        this.ipAddress = ipAddress; 
        this.username = username; 
        this.password = password; 
        stdout = new Vector<String>(); 
    } 
   
    public int execute(final String command) { 
        int returnCode = 0; 
        JSch jsch = new JSch(); 
        MyUserInfo userInfo = new MyUserInfo(); 
   
        try { 
            // Create and connect session. 
            Session session = jsch.getSession(username, ipAddress, DEFAULT_SSH_PORT); 
            session.setPassword(password); 
            session.setUserInfo(userInfo); 
            session.connect(); 
   
            // Create and connect channel. 
            Channel channel = session.openChannel("exec"); 
            ((ChannelExec) channel).setCommand(command); 
   
            channel.setInputStream(null); 
            BufferedReader input = new BufferedReader(new InputStreamReader(channel 
                    .getInputStream())); 
   
            channel.connect(); 
            System.out.println("The remote command is: " + command); 
   
            // Get the output of remote command. 
            String line; 
            while ((line = input.readLine()) != null) { 
                stdout.add(line); 
            } 
            input.close(); 
   
            // Get the return code only after the channel is closed. 
            if (channel.isClosed()) { 
                returnCode = channel.getExitStatus(); 
            } 
   
            // Disconnect the channel and session. 
            channel.disconnect(); 
            session.disconnect(); 
        } catch (JSchException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return returnCode; 
    } 
   
    public Vector<String> getStandardOutput() { 
        return stdout; 
    } 
   
    public static void main(final String [] args) { 
//      SSHCommandExecutor sshExecutor = new SSHCommandExecutor("xx.xx.xx.xx", "username", "password"); 
    	SSHCommandExecutor sshExecutor = new SSHCommandExecutor("192.168.230.200", "dhss", "dhss");
//        sshExecutor.execute("uname -s -r -v"); 
        sshExecutor.execute("netstat -nr");
//        sshExecutor.execute("tar -zcvf /home/dhss/test2/zp.zip /home/dhss/test1/provgw-spml_command.log.2015_06_21-21_25_47_083.gz");
           
//        Vector<String> stdout = sshExecutor.getStandardOutput(); 
//        for (String str : stdout) { 
//            System.out.println(str); 
//        } 
    } 
}