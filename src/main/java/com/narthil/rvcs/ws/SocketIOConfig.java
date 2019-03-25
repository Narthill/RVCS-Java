package com.narthil.rvcs.ws;

import java.util.List;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.channel.ChannelHandlerContext;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ExceptionListener;

@Configuration
public class SocketIOConfig {

    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private Integer port;

    @Value("${socketio.bossCount}")
    private int bossCount;

    @Value("${socketio.workCount}")
    private int workCount;

    @Value("${socketio.allowCustomRequests}")
    private boolean allowCustomRequests;

    @Value("${socketio.upgradeTimeout}")
    private int upgradeTimeout;

    @Value("${socketio.pingTimeout}")
    private int pingTimeout;

    @Value("${socketio.pingInterval}")
    private int pingInterval;

    // private Logger log = LoggerFactory.getLogger(MessageEventHandler.class);
    /**
     * 以下配置在上面的application.properties中已经注明
     * @return
     */
    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);

           /** 异常监听事件，必须覆写全部方法 */
           config.setExceptionListener(new ExceptionListener(){
                @Override
                public void onConnectException(Exception e, SocketIOClient client) {
                    System.out.println(client.getRemoteAddress()+"，连接异常:"+e);
                    client.sendEvent("exception","连接异常！");
                }
                @Override
                public void onDisconnectException(Exception e, SocketIOClient client) {
                    System.out.println(client.getRemoteAddress()+"，断开异常:"+e);
                    client.sendEvent("exception","断开异常！");
                }
                @Override
                public void onEventException(Exception e, List<Object> data, SocketIOClient client) {
                    System.out.println(client.getRemoteAddress()+"，服务器异常:"+e+"，传入数据:"+data);
                    client.sendEvent("exception","服务器异常");
                }
                @Override
                public void onPingException(Exception e, SocketIOClient client) {
                    System.out.println(client.getRemoteAddress()+"，ping超时异常:"+e);
                    client.sendEvent("exception","ping超时异常！");
                }
                @Override
                public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
                    return false;
                }
        });

        return new SocketIOServer(config);
    }
}