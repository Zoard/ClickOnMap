package com.example.zoardgeocze.clickonmap.FCM;

import android.os.Handler;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZoardGeocze on 03/05/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //this.token = refreshedToken;
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        sendRegistrationToDataBase(refreshedToken,formattedDate);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken,formattedDate);
    }

    public void sendRegistrationToDataBase(String token,String formattedDate) {

        SingletonFacadeController sfc = SingletonFacadeController.getInstance();
        sfc.registerFirebaseKey(getBaseContext(),token,formattedDate);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     *
     */
    //Enviando pro SERVER através do Retrofit
    private void sendRegistrationToServer(String refreshedToken, String formattedDate) {

        Call<String> call = new RetrofitInitializer().getFirebaseService().sendFirebaseKeyToServer("send", refreshedToken, formattedDate);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("onResponse: ", call.toString());
                Log.i("onResponse: ", response.body());
                Log.i("onResponse: ", response.message());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("onFailure: ", "DEU ERRADO!");
            }
        });
    }
}
