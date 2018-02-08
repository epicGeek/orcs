package com.nokia.ices.apps.fusion.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.apps.fusion.common.connector.Terminal;
import com.nokia.ices.core.utils.JsonMapper;

@ServerEndpoint(value = "/websocket/{cid}/terms.ws", configurator = GetHttpSessionConfigurator.class)
public class SecureShellWS {

    private static Logger log = LoggerFactory.getLogger(SecureShellWS.class);

    @OnOpen
    public void onOpen(@PathParam("cid") String cid, Session session, EndpointConfig config) {
        if (session.isOpen()) {
            try {
                Map<String, String> welcome = new HashMap<String, String>();
                welcome.put("cid", cid);
                welcome.put("output", "Connect to DHSS Successful\n");
                session.getBasicRemote().sendText(new JsonMapper().toJson(welcome));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {

        }

        // set websocket timeout
        // if(StringUtils.isNotEmpty(AppConfig.getProperty("websocketTimeout"))){
        // session.setMaxIdleTimeout( Long.parseLong(AppConfig.getProperty("websocketTimeout")) *
        // 1000);
        // } else {
        // session.setMaxIdleTimeout(0);
        // }
        // session.setMaxIdleTimeout(0);
        // this.httpSession = (HttpSession)
        // config.getUserProperties().get(HttpSession.class.getName());
        // this.session = session;
        //
        Runnable run = new SentOutputTask(cid, session);
        Thread thread = new Thread(run);
        thread.start();

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        if (StringUtils.isNotEmpty(message)) {
            log.debug("Message:" + message);
			Map jsonRoot = new JsonMapper().fromJson(message, Map.class);
            String command = (String) jsonRoot.get("command");
            String cid = (String) jsonRoot.get("cid");
            Integer keyCode = (Integer) jsonRoot.get("keyCode");
            try {
                if (keyCode != null) {
                    if (keyMap.containsKey(keyCode)) {
                        Terminal.getInstance().write(cid, keyMap.get(keyCode), "keyCode");
                    }
                } else {
                    Terminal.getInstance().write(cid, command.toString(), "command");
                }
            } catch (Exception e) {
                Map error = new HashMap();
                error.put("cid", cid);
                error.put("output", "NO CONNECTION,PLEASE CLOSE THIS WINDOW AND OPEN IT AGAIN\n");
                session.getBasicRemote().sendText(new JsonMapper().toJson(error));
            }

        }
    }


    @OnClose
    public void onClose(@PathParam("cid") String cid) {
        log.debug(cid);
        Terminal.getInstance().destroyConnector(cid);
    }

    /**
     * Maps key press events to the ascii values
     */
    static Map<Integer, byte[]> keyMap = new HashMap<Integer, byte[]>();

    static {
        // ESC
        keyMap.put(27, new byte[] {(byte) 0x1b});
        // ENTER
        keyMap.put(13, new byte[] {(byte) 0x0d});
        // LEFT
        keyMap.put(37, new byte[] {(byte) 0x1b, (byte) 0x4f, (byte) 0x44});
        // UP
        keyMap.put(38, new byte[] {(byte) 0x1b, (byte) 0x4f, (byte) 0x41});
        // RIGHT
        keyMap.put(39, new byte[] {(byte) 0x1b, (byte) 0x4f, (byte) 0x43});
        // DOWN
        keyMap.put(40, new byte[] {(byte) 0x1b, (byte) 0x4f, (byte) 0x42});
        // BS
        keyMap.put(8, new byte[] {(byte) 0x7f});
        // TAB
        keyMap.put(9, new byte[] {(byte) 0x09});
        // CTR
        keyMap.put(17, new byte[] {});
        // DEL
        keyMap.put(46, "\033[3~".getBytes());
        // CTR-A
        keyMap.put(65, new byte[] {(byte) 0x01});
        // CTR-B
        keyMap.put(66, new byte[] {(byte) 0x02});
        // CTR-C
        keyMap.put(67, new byte[] {(byte) 0x03});
        // CTR-D
        keyMap.put(68, new byte[] {(byte) 0x04});
        // CTR-E
        keyMap.put(69, new byte[] {(byte) 0x05});
        // CTR-F
        keyMap.put(70, new byte[] {(byte) 0x06});
        // CTR-G
        keyMap.put(71, new byte[] {(byte) 0x07});
        // CTR-H
        keyMap.put(72, new byte[] {(byte) 0x08});
        // CTR-I
        keyMap.put(73, new byte[] {(byte) 0x09});
        // CTR-J
        keyMap.put(74, new byte[] {(byte) 0x0A});
        // CTR-K
        keyMap.put(75, new byte[] {(byte) 0x0B});
        // CTR-L
        keyMap.put(76, new byte[] {(byte) 0x0C});
        // CTR-M
        keyMap.put(77, new byte[] {(byte) 0x0D});
        // CTR-N
        keyMap.put(78, new byte[] {(byte) 0x0E});
        // CTR-O
        keyMap.put(79, new byte[] {(byte) 0x0F});
        // CTR-P
        keyMap.put(80, new byte[] {(byte) 0x10});
        // CTR-Q
        keyMap.put(81, new byte[] {(byte) 0x11});
        // CTR-R
        keyMap.put(82, new byte[] {(byte) 0x12});
        // CTR-S
        keyMap.put(83, new byte[] {(byte) 0x13});
        // CTR-T
        keyMap.put(84, new byte[] {(byte) 0x14});
        // CTR-U
        keyMap.put(85, new byte[] {(byte) 0x15});
        // CTR-V
        keyMap.put(86, new byte[] {(byte) 0x16});
        // CTR-W
        keyMap.put(87, new byte[] {(byte) 0x17});
        // CTR-X
        keyMap.put(88, new byte[] {(byte) 0x18});
        // CTR-Y
        keyMap.put(89, new byte[] {(byte) 0x19});
        // CTR-Z
        keyMap.put(90, new byte[] {(byte) 0x1A});
        // CTR-[
        keyMap.put(219, new byte[] {(byte) 0x1B});
        // CTR-]
        keyMap.put(221, new byte[] {(byte) 0x1D});
        // INSERT
        keyMap.put(45, "\033[2~".getBytes());
        // PG UP
        keyMap.put(33, "\033[5~".getBytes());
        // PG DOWN
        keyMap.put(34, "\033[6~".getBytes());
        // END
        keyMap.put(35, "\033[4~".getBytes());
        // HOME
        keyMap.put(36, "\033[1~".getBytes());

    }

}
