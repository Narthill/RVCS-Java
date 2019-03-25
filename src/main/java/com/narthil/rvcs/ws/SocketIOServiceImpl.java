package com.narthil.rvcs.ws;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.narthil.rvcs.dao.user.UserDao;
import com.narthil.rvcs.dao.user.UserRepository;
import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.pojo.UserInfo;
import com.narthil.rvcs.service.UserService;
import com.narthil.rvcs.ws.dto.*;

@Service(value = "socketIOService")
public class SocketIOServiceImpl implements SocketIOService {

    // 用来存已连接的客户端
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRepository userRepository;
    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() throws Exception  {
        stop();
    }
    
    @Override
    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String userId = getUserIdByClient(client);
            // 上线关联群
            // UserInfo user=userRepository.findById(userId);
            // if(user!=null){
            //     for (String groupId : user.getGroups()) {
            //         client.joinRoom(groupId);
            //     }
            // }

            if (userId != null) {
                System.out.println(userId+"上线");
                clientMap.put(userId, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String userId = getUserIdByClient(client);
            if (userId != null) {
                clientMap.remove(userId);
                System.out.println(userId+"下线");
                client.disconnect();
            }
        });

        // 群聊
        socketIOServer.addEventListener("groupChatEvent", PushMessage.class, (client, data, ackSender) -> {
            // TODO do something
            String sa = client.getRemoteAddress().toString();
            String clientIp = sa.substring(1, sa.indexOf(":"));

            System.out.println(data.getUserId()+"发来消息：" + data.getMessage()+"ip: "+clientIp);
            data.setMessage(data.getMessage()+"-----------时间："+new Date().toLocaleString());
            socketIOServer.getRoomOperations(data.getGroupId()).sendEvent("groupChatEvent",data);
        });

        // 单聊
        socketIOServer.addEventListener("chatEvent", PushMessage.class, (client, data, ackSender) -> {
            System.out.println(data.getUserId()+"发来消息：" + data.getMessage());
            // socketIOServer.getBroadcastOperations().sendEvent("groupChatEvent",data);
            data.setMessage(data.getMessage()+"-----------时间："+new Date().toLocaleString());
            // 源
            SocketIOClient fromSocketIoClient = clientMap.get(data.getUserId());
            // 目标
            SocketIOClient toSocketIoClient = clientMap.get(data.getTargetId());

            

            System.out.println("目标"+data.getTargetId()); 
            if(toSocketIoClient!=null){
                toSocketIoClient.sendEvent("chatEvent",data);
            }
            if(fromSocketIoClient!=null){
                fromSocketIoClient.sendEvent("chatEvent",data);
            }
        });

        // 好友添加
        socketIOServer.addEventListener("newFriend", NewFriend.class, (client, data, ackSender) -> {
            System.out.println(data.getSourceId()+"请求添加好友"+data.getTargetId() );
            // // socketIOServer.getBroadcastOperations().sendEvent("groupChatEvent",data);
            // // 源
            // SocketIOClient fromSocketIoClient = clientMap.get(data.getUserId());
            // // 目标
            SocketIOClient toSocketIoClient = clientMap.get(data.getTargetId());

            // toSocketIoClient.
            ResultInfo<Map<String,Object>> user=userService.getInfoByUserId(data.getSourceId());
            // System.out.println("目标"+data.getTargetId()); 
            if(toSocketIoClient!=null){
                toSocketIoClient.sendEvent("newFriend",user.getData());
            }
            // if(fromSocketIoClient!=null){
            //     fromSocketIoClient.sendEvent("newFriend",data);
            // }
        });

        socketIOServer.start();
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    // @Override
    // public void pushMessageToUser(PushMessage pushMessage) {
    //     String userId = pushMessage.getUserId();
    //     if (StringUtils.isNotBlank(userId)) {
    //         SocketIOClient client = clientMap.get(userId);
    //         if (client != null)
    //             client.sendEvent(PUSH_EVENT, pushMessage);
    //     }
    // }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     * @param client
     * @return
     */
    private String getUserIdByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的userId必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("userId");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}