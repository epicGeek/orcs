package com.nokia.ices.apps.fusion.jms;

import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查结果临时交换服务-DHSS
 * <p>
 * 1.向AMQ发送消息时，将检查记录临时存放此处
 * <p>
 * 2. 异步从AMQ接收到消息时，更新检查记录，并移除
 * 
 * @author Adrian LAI
 *
 */
public class CheckResultCacheDhss {

    private final static Logger logger = LoggerFactory.getLogger(CheckResultCacheDhss.class);

    /** 结果缓存<sessionId,[resultCode,logOrLogPath,message]> */
    private static final Map<String, String[]> CACHE_MAP = new Hashtable<String, String[]>();

    /**
     * 从AMQ接收到命令执行结果后执行该方法
     * 
     * @param sessionId
     *            发送AMQ消息的唯一标识
     * @param resultCode
     *            检查项命令执行结果码
     * @param msg
     *            检查项命令日志或日志文件路径
     * @param message
     *            返回信息
     */
    synchronized public static void push(String sessionId, String resultCode, String msg) {
	CACHE_MAP.put(sessionId, new String[] { resultCode, msg });

    }

    /**
     * <p>
     * 利用session查询缓存的结果，查询到后结果移出缓存
     * 
     * @param sessionId
     * @return
     */
    synchronized public static String[] pop(String sessionId) {
	return CACHE_MAP.remove(sessionId);
    }

    synchronized public static void print() {
	StringBuilder builder = new StringBuilder();
	builder.append("********Dhss Result Cache********\r\n");
	for (String sessionId : CACHE_MAP.keySet()) {
	    builder.append("sessionId:" + sessionId + "\r\n");
	}
	builder.append("******************************\r\n");
	logger.info(builder.toString());
    }

    synchronized public static void clear() {
	logger.info("CheckResultCacheDhss.CACHE_MAP.size::" + CACHE_MAP.size());
	CACHE_MAP.clear();
	logger.info("CheckResultCacheDhss.CACHE_MAP.size::" + CACHE_MAP.size());
	logger.info("CheckResultCacheDhss.CACHE_MAP cleared");

    }
}
