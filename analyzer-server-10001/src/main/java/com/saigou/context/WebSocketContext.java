package com.saigou.context;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/{controlId}")
@RequiredArgsConstructor
public class WebSocketContext {
    private static final Logger log = LoggerFactory.getLogger(WebSocketContext.class);
    @Data
    private class SessionInfo {
        private Session session;
        private Long controlId;
    }
    private static int onlineCount = 0;
    private static Map<String,SessionInfo> sessionMap = new HashMap<>();
    @OnOpen
    public void onOpen(Session session, @PathParam("controlId") Long controlId) {
        onlineCount++;
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setSession(session);
        sessionInfo.setControlId(controlId);
        sessionMap.put(session.getId(),sessionInfo);
        log.info("[controlId:"+controlId+"][sid:"+session.getId()+"]有连接加入！当前在线人数为" + onlineCount);
    }
    @OnMessage
    public void onMessage(Session session, String message, @PathParam("controlId") Long controlId) {
        log.info("[controlId:"+controlId+"][sid:"+session.getId()+"]收到客户端消息:" + message);
        //群发消息
        for (SessionInfo s : sessionMap.values()) {
            s.getSession().getAsyncRemote().sendText(message);
        }
    }
    @OnClose
    public void onClose(Session session,@PathParam("controlId") Long controlId) {
        onlineCount--;
        sessionMap.remove(session.getId());
        log.info("[controlId:"+controlId+"][sid:"+session.getId()+"]有连接关闭！当前在线人数为" + onlineCount);
    }
    public void sendMessageBySid(String sid, String message) {
        SessionInfo sessionInfo = sessionMap.get(sid);
        if (sessionInfo != null) {
            sessionInfo.getSession().getAsyncRemote().sendText(message);
        }
    }
    public void sendMessageByControlId(Long controlId, String message) {
        for (SessionInfo s : sessionMap.values()) {
            if (s.getControlId().equals(controlId)) {
                s.getSession().getAsyncRemote().sendText(message);
            }
        }
    }
    public void sendAllMessage(String message) {
        for (SessionInfo s : sessionMap.values()) {
            s.getSession().getAsyncRemote().sendText(message);
        }
    }
}
