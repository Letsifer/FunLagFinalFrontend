package ru.lets.funlagitog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 *
 * @author Евгений
 */
@ClientEndpoint
public class ConnectionService {

    private static ConnectionService CONNECTION_SERVICE;

    public static ConnectionService getConnectionService() throws IOException {
        if (CONNECTION_SERVICE == null) {
            CONNECTION_SERVICE = new ConnectionService();
        }
        return CONNECTION_SERVICE;
    }

    private Session callSession;
    private final String restPath = "reg";

    private void connectToWebSocket(String websocketUrl) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            URI uri = URI.create("ws://" + websocketUrl + "/call");
            callSession = container.connectToServer(this, uri);
        } catch (DeploymentException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public Response register(String telephone, Operator operator, Region region) throws IOException {
        Client client = Client.create();
        client.getProperties().put("phone", telephone);
        client.getProperties().put("operator", operator.getId());
        WebResource webResource = client.resource("http://" + region.getIpAddress()).path(restPath);
        ClientResponse response = webResource.post(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getEntityInputStream(), Response.class);
    }

    public int findBalance(Integer id, Region region) throws IOException {
        Client client = Client.create();
        WebResource webResource = client.resource("http://" + region.getIpAddress()).path("balance/" + id);
        ClientResponse response = webResource.get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getEntityInputStream(), BalanceResponse.class).getBalance();
    }

    public void startCall(String telephone, Region region) throws IOException {
        connectToWebSocket(region.getIpAddress());
        callSession.getBasicRemote().sendText("{event: call:, data: 123423}");
    }

    public void stopCall(String telephone, Region region) throws IOException {
        callSession.close();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message from server " + message);
    }

    @OnClose
    public void onClose() {

    }
}
