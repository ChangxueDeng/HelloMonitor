package hello.monitor.server.websocket;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import hello.monitor.server.entity.dto.ClientDetail;
import hello.monitor.server.entity.dto.ClientSsh;
import hello.monitor.server.mapper.ClientDetailMapper;
import hello.monitor.server.mapper.ClientSshMapper;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ChangxueDeng
 */
@Slf4j
@Component
@ServerEndpoint("/terminal/{clientId}")
public class TerminalWebSocket {
    private static ClientDetailMapper clientDetailMapper;
    private static ClientSshMapper clientSshMapper;

    @Resource
    public void setClientDetailMapper(ClientDetailMapper clientDetailMapper) {
        TerminalWebSocket.clientDetailMapper = clientDetailMapper;
    }
    @Resource
    public void setClientSshMapper(ClientSshMapper clientSshMapper) {
        TerminalWebSocket.clientSshMapper = clientSshMapper;
    }

    private static final Map<Session, Shell> sessionMap = new ConcurrentHashMap<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "clientId") String clientId) throws Exception {
        ClientDetail detail = clientDetailMapper.selectById(clientId);
        ClientSsh clientSsh = clientSshMapper.selectById(clientId);
        if (detail == null || clientSsh == null) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "无法识别"));
            return;
        }
        if (this.createSshConnection(session, clientSsh)) {
            log.info("客户端: {} 的SSH连接已创建", clientSsh.getIp());
        }

    }
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        Shell shell = sessionMap.get(session);
        OutputStream outputStream = shell.outputStream;
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
    @OnClose
    public void onClose(Session session) throws IOException {
        Shell shell = sessionMap.get(session);
        if (shell != null) {
            shell.close();
            sessionMap.remove(session);
            log.info("客户端: {} 的SSH连接已断开", shell.jSchSession.getHost());
        }
    }
    @OnError
    public void onError(Session session, Throwable throwable) throws IOException{
        log.error("WebSocket连接出现错误", throwable);
        session.close();
    }

    private boolean createSshConnection(Session session, ClientSsh ssh) throws IOException{
        try {
            JSch jSch = new JSch();
            com.jcraft.jsch.Session jSchSession = jSch.getSession(ssh.getUsername(), ssh.getIp(), ssh.getPort());
            jSchSession.setPassword(ssh.getPassword());
            jSchSession.setConfig("StrictHostKeyChecking", "no");
            jSchSession.setTimeout(3000);
            jSchSession.connect();
            ChannelShell channel = (ChannelShell) jSchSession.openChannel("shell");
            channel.setPtyType("xterm");
            channel.connect(1000);
            sessionMap.put(session, new Shell(session, jSchSession, channel));
            return true;
        } catch (JSchException e) {
            String message = e.getMessage();
            if (message.equals("Auth fail")) {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "认证失败，用户名或密码错误"));
                log.error("连接SSH失败，用户名或密码错误");
            } else if (message.equals("Connection refused")) {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "连接被拒绝，请检查IP地址和端口号"));
                log.error("连接SSH失败，连接被拒绝，请检查IP地址和端口号");
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, message));
                log.error("连接SSH失败，出现错误", e);
            }
        }
        return false;
    }
    private class Shell {
        private final Session session;
        private final com.jcraft.jsch.Session jSchSession;
        private final ChannelShell channel;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public Shell(Session session, com.jcraft.jsch.Session jSchSession, ChannelShell channel) throws IOException {
            this.session = session;
            this.jSchSession = jSchSession;
            this.channel = channel;
            this.inputStream = channel.getInputStream();
            this.outputStream = channel.getOutputStream();
            executorService.submit(this::read);
        }
        private void read() {
            try{
                byte[] buffer = new byte[1024];
                int i;
                while ((i = inputStream.read(buffer)) != -1) {
                    String text = new String(Arrays.copyOfRange(buffer, 0, i), StandardCharsets.UTF_8);
                    session.getBasicRemote().sendText(text);
                }
            } catch (IOException e) {
                log.error("读取SSH输入流出现错误", e);
            }
        }
        private void close() throws IOException {
            this.inputStream.close();
            this.outputStream.close();
            this.channel.disconnect();
            this.jSchSession.disconnect();
            executorService.shutdown();
        }
    }
}
