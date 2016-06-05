package com.developer.socketio;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText edtMsg;
    Button btnSend;
    private List<Message> messageList;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    public static String ID;

    private Socket socket;{
        try {
            socket = IO.socket("http://192.168.137.1:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSend = (Button) findViewById(R.id.btn_send);
        edtMsg = (EditText) findViewById(R.id.edt_msg);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView = (RecyclerView) findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        socket.connect();

        ID = socket.id();

        JSONObject jsonMsg = new JSONObject();
        try {
            jsonMsg.put("content","");
            jsonMsg.put("id",socket.id());
            jsonMsg.put("type",Message.TYPE_CONNECT);
            jsonMsg.put("username", RegisterActivity.USERNAME);
            socket.emit("new message", jsonMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.on("new message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = (JSONObject) args[0];
                        try {
                            jsonObject = jsonObject.getJSONObject("message");
                            String msg = jsonObject.getString("content");
                            String uname = jsonObject.getString("username");
                            int type = jsonObject.getInt("type");
                            messageList.add(new Message(uname, msg, type));
                            messageAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messageList.size()-1);
                            if(!uname.equals(RegisterActivity.USERNAME) && type == Message.TYPE_MESSAGE){
                                notification(new Message(uname, msg, type));
                            }

                        } catch (JSONException e) {
                            System.out.println("xxxx Error : "+e);
                        }
                    }
                });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edtMsg.getText().toString().trim();
                if(msg.isEmpty()) return;
                JSONObject jsonMsg = new JSONObject();
                try {
                    jsonMsg.put("content",msg);
                    jsonMsg.put("type",Message.TYPE_MESSAGE);
                    jsonMsg.put("username", RegisterActivity.USERNAME);
                    jsonMsg.put("id",socket.id());
                    socket.emit("new message", jsonMsg);
                    System.out.println("xxxx message send");
                    edtMsg.setText(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject jsonMsg = new JSONObject();
        try {
            jsonMsg.put("content","");
            jsonMsg.put("id",socket.id());
            jsonMsg.put("type",Message.TYPE_DISCONNECT);
            jsonMsg.put("username", RegisterActivity.USERNAME);
            socket.emit("new message", jsonMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.disconnect();
    }

    int i=0;
    private void notification(Message message){
        //build your notification here.
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(message.getUsername())
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(message.getMessage());
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(i++, mBuilder.build());
    }
}
