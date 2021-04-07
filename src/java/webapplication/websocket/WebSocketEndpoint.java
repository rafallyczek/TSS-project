package webapplication.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Random;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/endpoint")
public class WebSocketEndpoint {

    @OnMessage
    public String onMessage(String message) {
        Random rand = new Random();
        Gson gsonBuilder = new GsonBuilder().create();
        int[] data = new int[25];
        for(int i=0;i<25;i++){
            data[i]=rand.nextInt(50)+1;
        }
        String dataJSON = gsonBuilder.toJson(data);
        return dataJSON;
    }
    
    @OnError
    public void onError(Session session, Throwable thr) {}
    
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {}
    
}
