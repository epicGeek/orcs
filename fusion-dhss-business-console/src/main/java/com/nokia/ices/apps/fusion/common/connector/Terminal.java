package com.nokia.ices.apps.fusion.common.connector;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.apps.fusion.TerminalProperties;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.core.utils.ModuleDownLoadNameDefinition;

/**
 * 多协议终端
 * 
 * @author kongdy
 * 
 */
public class Terminal {
	
	private final Logger logger = LoggerFactory.getLogger(Terminal.class);
	
	/**
	 * 连接器空闲超时时，销毁连接器（断开连接、删除连接超时处理任务、删除结果缓存）
	 */
	private final Timer idleTimeoutTimer = new Timer("Idle Timeout GC");

	/**
	 * 连接器列表
	 */
	private final Map<String, Connector> connectorList = new HashMap<String, Connector>();

	/**
	 * 连接超时处理任务列表
	 */
	private final Map<String, TimerTask> idleTimeoutTaskList = new HashMap<String, TimerTask>();

	/**
	 * 结果缓存列表
	 */
	private final Map<String, Queue<String>> outputBuffer = new HashMap<String, Queue<String>>();

	private static Terminal instance = new Terminal();
	
//	/**
//	 * 集中登录会话日志文件存放路径
//	 */
//	public static String terminalSessionLogDirectory = TerminalProperties.getLogBasePath();

	private Terminal() {
	}

	public static Terminal getInstance() {
		return instance;
	}

	public void destroyConnector(String cid) {
		for (String connectorid : connectorList.keySet()) {
			logger.debug(connectorid);
		}
		Connector connector = connectorList.get(cid);
		if (null != connector) {
			try {
				connector.logout();
			} catch (Exception e) {
				// do nothing
			}
		}

		if (connectorList.containsKey(cid)) {
			connectorList.remove(cid);
		}

		if (outputBuffer.containsKey(cid)) {
			outputBuffer.remove(cid);
		}
		
		if (idleTimeoutTaskList.containsKey(cid)) {
			idleTimeoutTaskList.get(cid).cancel();
			idleTimeoutTaskList.remove(cid);
		}
		
	}

	public Account login(String hostname, int port, ConnectConfig config,String systemUserName) throws Exception {
		Account account = new Account(UUID.randomUUID().toString());

		Connector connector = ConnectorFactory.createConnector(Protocol.SSH2);
		if (0 != config.idleTimeoutInSec) {
			connector.setIdleTimeout(config.idleTimeoutInSec);
		}
		if (null != config.hostFilter) {
			connector.setHostFilter(config.hostFilter);
		}
		if (null != config.commandFilter) {
			connector.setCommandFilter(config.commandFilter);
		}

		// TODO 进行主机过滤
		// 区分不同主机
		if(null!=config.ext) {
			config.ext.put("srcQ", account.getCid());
		}
		
		account.setTip(connector.login(hostname, port, config.username, config.password, config.ext));

		String cid = account.getCid();
		
		// 创建结果缓存文件 add by yudq 2015-04-14.
		String logFilePath = ProjectProperties.getLogBasePath()+ File.separator+
		                        ModuleDownLoadNameDefinition.DOWNLOAD_TERMINALHISTORY + File.separator + 
		                        systemUserName+ File.separator +cid;

        String logFileParent = ProjectProperties.getLogBasePath()+ File.separator+
                ModuleDownLoadNameDefinition.DOWNLOAD_TERMINALHISTORY + File.separator + systemUserName;
        File parent = new File(logFileParent);
        if (!parent.exists()) {
            logger.info(parent.getAbsolutePath());
            parent.mkdirs();
        }
        
		File sessionFile = new File(logFilePath);
		if (!sessionFile.exists()) {
		    try {
	            logger.debug(sessionFile.getAbsolutePath());
	            sessionFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
		}

		connectorList.put(cid, connector);

		outputBuffer.put(cid, new ConcurrentLinkedQueue<String>());
		

        new ReadThread(logFilePath, connector.readOut(), outputBuffer.get(cid)).start();
        new ReadThread(logFilePath, connector.readErr(), outputBuffer.get(cid)).start();
		

		IdleTimeoutTask idleTsk = new IdleTimeoutTask(cid);
		idleTimeoutTaskList.put(cid, idleTsk);
		idleTimeoutTimer.schedule(idleTsk, connector.getIdleTimeout());

		return account;
	}

    public void write(String cid, Object command,String type) throws Exception {
        if (connectorList.containsKey(cid)) {
            logger.debug("Send {}, type {}",command.toString(),type);

            // TODO 过滤命令
            if(type.equalsIgnoreCase("keyCode")){
                connectorList.get(cid).write((byte[])command,true);
            }else{
                if(command instanceof String)
                    connectorList.get(cid).write(String.valueOf(command).getBytes(),false);
                else
                    connectorList.get(cid).write((byte[])command,false);

            }

            // 更新空闲超时任务
            if (idleTimeoutTaskList.containsKey(cid)) {
                idleTimeoutTaskList.get(cid).cancel();
                idleTimeoutTaskList.remove(cid);

                IdleTimeoutTask idleTsk = new IdleTimeoutTask(cid);
                idleTimeoutTaskList.put(cid, idleTsk);
                idleTimeoutTimer.schedule(idleTsk, connectorList.get(cid).getIdleTimeout());
            }
        } else {
            throw new Exception("The connector has been destroyed.");
        }
    }

	public InputStream readOut(String cid) throws Exception {
		if (connectorList.containsKey(cid)) {
			return connectorList.get(cid).readOut();
		} else {
			throw new Exception("The connector has been destroyed.");
		}
	}

	public InputStream readErr(String cid) throws Exception {
		if (connectorList.containsKey(cid)) {
			return connectorList.get(cid).readErr();
		} else {
			throw new Exception("The connector has been destroyed.");
		}
	}

	public String read(String cid) throws Exception {
		if (outputBuffer.containsKey(cid)) {
			StringBuilder out = new StringBuilder();

			Queue<String> buffer = outputBuffer.get(cid);
			while (!buffer.isEmpty()) {
				out.append(buffer.poll());
			}
			return out.toString();
		} else {
			return "";
		}
	}
	
	public void put(String cid, String content){
		outputBuffer.get(cid).add(content);
	}
}
