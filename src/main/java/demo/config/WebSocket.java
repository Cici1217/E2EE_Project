package demo.config;

import com.alibaba.fastjson.JSONObject;

import demo.Mapper.GroupMapper;
import demo.pojo.KeyBundle;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static demo.OpenMybatis.sqlSession;

@Component
@ServerEndpoint(value = "/websocket/{username}")
//configurator = MySpringConfigurator.class这个地方经验证不需用加上否则多设备连接回发现两台以上设备连接
// 回造成下面的session变为同一个，造成其他设备推送失败，所以不要盲目复制别人的，要注意此处
public class WebSocket {
    private static int onlineCount = 0;
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();

    public Map<Integer, WebSocket> groups = new HashMap<>();
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
        JSONObject req = JSONObject.parseObject(message);


        System.out.println(req.get("sourceUserId"));
        System.out.println(req.get("sourceRegistrationId"));
        System.out.println(req.get("destinationUserId"));
        System.out.println(req.get("destinationRegistrationId"));
        JSONObject req2 = req.getJSONObject("ciphertext");
//        System.out.println(req2.get("body"));
        System.out.println(req.get("groupId"));

        System.out.println("thisUserName" + username);

        if (req.get("groupId") != null) {
            List<Integer> listUserIds = test(req.get("groupId").toString());
            if (listUserIds.size() == 0) {
                if (req.get("destinationUserId") != null) {
                    sendMessageTo(message, req.get("destinationUserId").toString());
                }
            } else {
                for (int i = 0; i < listUserIds.size(); i++) {
                    //群发
                    sendMessageTo(message, String.valueOf(listUserIds.get(i)));
                    System.out.println("send!");
                }
            }


        }

        // 发送数据给服务端
        // sendMessageAll(JSON.toJSONString(res));
    }

    public List<Integer> test(String gId) {
        int groupId = Integer.parseInt(gId);
        if (groupId == -1) {
            return new ArrayList<>();
        } else {
            GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);
            //返回该组的user -> id列表
            return groupMapper.selectByGroupId(groupId);
        }
    }

    public void sendMessageTo(String message, String To) throws IOException {
        for (WebSocket item : clients.values()) {
            if (item.username.equals(To)) {
                item.session.getAsyncRemote().sendText(message);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
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
