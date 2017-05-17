package com.example.zoardgeocze.clickonmap.FCM;

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

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    //Enviando por enquanto pro BD Local
    private void sendRegistrationToServer(String token) {
        SingletonFacadeController sfc = SingletonFacadeController.getInstance();
        sfc.registerFirebaseKey(token);
        /*SendFirebaseKeyToServer sendKey = new SendFirebaseKeyToServer(getBaseContext(),token);
        try {
            sendKey.wait();
            sendKey.execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
