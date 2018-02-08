package com.nokia.ices.apps.fusion.common.connector;

import java.util.TimerTask;

/**
 * 终端空闲超时处理
 * 
 * @author kongdy
 *
 */
public class IdleTimeoutTask extends TimerTask {

	private String cid;

	public IdleTimeoutTask(String cid) {
		this.cid = cid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		Terminal.getInstance().destroyConnector(cid);
	}

}
