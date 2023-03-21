package demo.config;

import com.alibaba.fastjson.JSONObject;

import demo.pojo.KeyBundle;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/websocket/{username}")
//, configurator = MySpringConfigurator.class这个地方经验证不需用加上否则多设备连接回发现两台以上设备连接 回造成下面的session变为同一个，造成其他设备推送失败，所以不要盲目复制别人的，要注意此处
public class WebSocket {
    private static int onlineCount = 0;
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();

    private static Map<String, KeyBundle> keyMap = new ConcurrentHashMap<>();
    private Session session;
    private String username;

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {
        this.username = username;
        this.session = session;

        addOnlineCount();
        clients.put(username, this);
        System.out.println("已连接" + getOnlineCount());
        System.out.println(this.username);
    }

    @OnClose
    public void onClose() throws IOException {
        clients.remove(username);
        subOnlineCount();
        System.out.println("已连接" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        // DataWrapper res = new DataWrapper();
        System.out.println("message:" + message);
        JSONObject req = JSONObject.parseObject(message);
        System.out.println(req.get("destinationUserId"));
        System.out.println(req.get("destinationRegistrationId"));
        if(req.get("destinationUserId") != null) {
            sendMessageTo(message, req.get("destinationUserId").toString());
        }

        // 发送数据给服务端
        // sendMessageAll(JSON.toJSONString(res));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessageTo(String message, String To) throws IOException {
        // session.getBasicRemote().sendText(message);
        // session.getAsyncRemote().sendText(message);
        for (WebSocket item : clients.values()) {
            if (item.username.equals(To))
                item.session.getAsyncRemote().sendText(message);
        }
    }

    public void sendMessageAll(String message) throws IOException {
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }

    public static synchronized void setKeyMap(String uid, KeyBundle keyBundle) {
        keyMap.put(uid, keyBundle);
    }

    public static synchronized KeyBundle getKeyBy(String uid) {
        return keyMap.get(uid);
    }
}
