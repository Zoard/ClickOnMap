package com.example.zoardgeocze.clickonmap.fcm;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.MenuActivity;
import com.example.zoardgeocze.clickonmap.Model.EventCategory;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitClientInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.example.zoardgeocze.clickonmap.helper.Alert;
import com.example.zoardgeocze.clickonmap.observer.VGISystemNotifier;
import com.example.zoardgeocze.clickonmap.responses.EventCategoryDataResponse;
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

    private VGISystemNotifier vgiSystemNotifier;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle fcm messages here. Fazer com que o sistema mude de endereço no banco local.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received fcm
        // message, here is where that should be initiated.
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        this.generalController = SingletonFacadeController.getInstance();
        this.vgiSystemNotifier = VGISystemNotifier.getInstance();

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

                    deleteSystem(msg,oldAddress);

                } else if (msg.equals("category_change") || msg.equals("type_change")) {
                    changeCategory(msg,oldAddress);
                }

                String horario = data.get("horario");

                Log.i(TAG, "Horario: " + horario);
            }
        }

        Log.i(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }

    public void onMessageReceivedSystemTray(Context context, String msg, String oldAddress, String newAddress) {
        this.generalController = SingletonFacadeController.getInstance();
        this.vgiSystemNotifier = VGISystemNotifier.getInstance();

        if(msg.equals("change_adress")) {

            Alert dialog = new Alert(context, "Atualização", "O endereço do sistema foi atualizado.",
                    "Ok", "Cancelar");
            dialog.show();

            changeSystemAdress(msg,oldAddress,newAddress);

        } else if(msg.equals("delete_system")) {

            Alert dialog = new Alert(context, "Atualização", "O Sistema não está mais disponível para colaboração.",
                    "Ok", "Cancelar");
            dialog.show();

            deleteSystem(msg,oldAddress);

        } else if (msg.equals("category_change") || msg.equals("type_change")) {

            Alert dialog = new Alert(context, "Atualização", "O Sistema possui novas categorias.",
                    "Ok", "Cancelar");
            dialog.show();

            changeCategory(msg,oldAddress);
        }


    }

    @Override
    public void onDeletedMessages() {
        this.generalController = SingletonFacadeController.getInstance();

        List<SystemTile> systems = this.generalController.getMenuTiles();

        for (SystemTile system: systems) {
            this.generalController.updateSync(system.getSystem().getAddress(),1);
        }

    }

    //Todos os métodos abaixo fazem alterações locais e avisam as activities que VGISystem foi alterado
    private void changeSystemAdress(String message, String oldAddress, String newAddress) {

        this.generalController.updateVGISystemAddress(oldAddress,newAddress);
        this.vgiSystemNotifier.setVGIsystemNotifier(message,oldAddress,newAddress);
    }

    private void deleteSystem(String message, String oldAddress) {

        this.generalController.deleteVGISystem(oldAddress);
        this.vgiSystemNotifier.setVGIsystemNotifier(message,oldAddress,"");
    }

    //TODO: O método para tratar categorias deverá ser diferenciado.
    private void changeCategory(final String message, final String oldAddress) {
        final String base_url = oldAddress + "/";
        new RetrofitClientInitializer(base_url)
                .getSystemService()
                .getSystemCategories("getCategories")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventCategoryDataResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(EventCategoryDataResponse response) {
                        //TODO: Implementar Alert Dialog
                        VGISystem vgiSystem = new VGISystem();
                        vgiSystem.setAddress(oldAddress);
                        vgiSystem.setCategory(response.categories);

                        generalController.registerCategory(vgiSystem);
                        vgiSystemNotifier.setVGIsystemNotifier(message,oldAddress,"");
                    }

                });
    }
}
