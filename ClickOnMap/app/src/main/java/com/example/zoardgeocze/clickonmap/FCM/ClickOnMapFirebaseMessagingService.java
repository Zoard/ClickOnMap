package com.example.zoardgeocze.clickonmap.FCM;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by ZoardGeocze on 03/05/2017.
 */

public class ClickOnMapFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();
            if(data.size() > 0){
                //String msg = data.get("msg");
                String msg = "";
                // Decodificando a msg
                try {
                    msg = new String(data.get("msg").getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "Msg: " + msg);

                String horario = data.get("horario");

                Log.i(TAG, "Horario: " + horario);

            }
        }

        Log.i(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
