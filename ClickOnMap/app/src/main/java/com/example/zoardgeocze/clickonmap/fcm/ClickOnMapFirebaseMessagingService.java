package com.example.zoardgeocze.clickonmap.fcm;

import android.util.Log;

import com.example.zoardgeocze.clickonmap.Model.EventCategory;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitClientInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZoardGeocze on 03/05/2017.
 */

public class ClickOnMapFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "fcm Service";

    private SingletonFacadeController generalController;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle fcm messages here. Fazer com que o sistema mude de endereço no banco local.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received fcm
        // message, here is where that should be initiated.
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        generalController = SingletonFacadeController.getInstance();

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();
            if(data.size() > 0){
                String msg = "";
                // Decodificando a msg e o corpo
                try {
                    msg = new String(data.get("message").getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "Msg: " + msg);

                String oldAddress = data.get("oldAdress");
                String newAddress = data.get("newAdress");

                if(msg.equals("change_adress")) {

                    changeSystemAdress(msg,oldAddress,newAddress);

                } else if(msg.equals("delete_system")) {

                    deleteSystem(oldAddress);

                } else if (msg.equals("category_change")) {
                    changeCategory(oldAddress);
                }

                String horario = data.get("horario");

                Log.i(TAG, "Horario: " + horario);
            }
        }

        Log.i(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }

    private void changeSystemAdress(String message,String oldAddress, String newAddress) {
        this.generalController = SingletonFacadeController.getInstance();
        this.generalController.updateVGISystemAddress(oldAddress,newAddress);
    }

    private void deleteSystem(String address) {
        this.generalController = SingletonFacadeController.getInstance();
        this.generalController.deleteVGISystem(address);
    }

    //TODO: O método para tratar categorias deverá ser diferenciado.
    private void changeCategory(final String address) {
        final String base_url = address + "/";
        new RetrofitClientInitializer(base_url)
                .getSystemService()
                .getSystemCategories("getCategories")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<EventCategory>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<EventCategory> eventCategories) {
                        VGISystem vgiSystem = new VGISystem();
                        vgiSystem.setAddress(address);
                        vgiSystem.setCategory(eventCategories);

                        generalController.registerCategory(vgiSystem);
                    }
                });
    }
}
