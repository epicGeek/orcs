package com.nokia.ices.apps.fusion.websocket;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import com.nokia.ices.apps.fusion.common.connector.Terminal;
import com.nokia.ices.core.utils.JsonMapper;

public class SentOutputTask implements Runnable {


    Session session;
    String cid;

    public SentOutputTask(String cid, Session session) {
        this.cid = cid;
        this.session = session;

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void run() {

        while (session.isOpen()) {
            try {
                String outPutStr = Terminal.getInstance().read(cid);
                Map outputMap = new HashMap<String,String>();
                outputMap.put("cid", cid);
                if (outPutStr != null && !outPutStr.isEmpty()) {
                    outputMap.put("cid", cid);
                    outputMap.put("output", outPutStr);
                    this.session.getBasicRemote().sendText(new JsonMapper().toJson(outputMap));
                }
                Thread.sleep(50);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }
}
