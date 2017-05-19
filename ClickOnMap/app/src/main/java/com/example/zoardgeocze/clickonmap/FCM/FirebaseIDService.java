package com.example.zoardgeocze.clickonmap.FCM;

import android.os.Handler;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.Server.SendFirebaseKeyToServer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ZoardGeocze on 03/05/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    //private String token = "";

    /*public String getToken() {
        return token;
    }*/

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //this.token = refreshedToken;
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToDataBase(refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToDataBase(String token) {
        SingletonFacadeController sfc = SingletonFacadeController.getInstance();
        sfc.registerFirebaseKey(getBaseContext(),token);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     *
     */
    //Enviando por enquanto pro BD Local
    private void sendRegistrationToServer(String refreshedToken) {
        final SendFirebaseKeyToServer sendKey = new SendFirebaseKeyToServer(getBaseContext(),refreshedToken);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendKey.execute();
            }
        }).start();
        //sendKey.execute();
        /*try {
            sendKey.wait();
            sendKey.execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
