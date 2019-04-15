package com.example.zoardgeocze.clickonmap;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Adapter.AddSystemAdapter;
import com.example.zoardgeocze.clickonmap.DTO.VGISystemSync;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.example.zoardgeocze.clickonmap.responses.VGISystemDataResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

public class AddSystemActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private List<VGISystem> vgiSystems = new ArrayList<>();
    private RecyclerView addSystemRecycler;
    private SwipeRefreshLayout swipeSystemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_system);

        this.addSystemRecycler = (RecyclerView) findViewById(R.id.add_system_recycler);

        this.generalController = SingletonFacadeController.getInstance();

        getSystemsFromServer(true);

        this.swipeSystemList = (SwipeRefreshLayout) findViewById(R.id.swipe_system_list);
        this.swipeSystemList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vgiSystems.clear();
                getSystemsFromServer(false);
            }
        });
    }

    private void getSystemsFromServer(boolean showProgDialog) {

        final ProgressDialog mProgressDialog = new ProgressDialog(this);

        if(showProgDialog) {
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Carregando Sistemas VGI...");
            mProgressDialog.show();
        }

        new RetrofitInitializer()
                .getSystemService()
                .getVGISystemList("getVGISystem")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VGISystemDataResponse>() {
                    @Override
                    public void onCompleted() {
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        } else {
                            swipeSystemList.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        } else {
                            swipeSystemList.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onNext(VGISystemDataResponse response) {
                        //TODO: Colocar Alert Dialog aqui
                        loadAvailableVGISystems(response.vgiSystems);
                    }
                });

    }


    private void loadAvailableVGISystems(List<VGISystem> availableVgiSystems) {

        if(availableVgiSystems != null) {
            for (VGISystem vs : availableVgiSystems) {
                if(this.generalController.searchVGISystem(vs)) {
                    this.vgiSystems.add(vs);
                }
            }
        }

        Log.i("onResponse_VGISYSTEM: ", String.valueOf(vgiSystems.size()));

        if(!this.vgiSystems.isEmpty()) {
            this.addSystemRecycler.setAdapter(new AddSystemAdapter(this.vgiSystems,this));
            RecyclerView.LayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            this.addSystemRecycler.setLayoutManager(layout);
        } else {
            Toast.makeText(getBaseContext(),"Nenhum sistema VGI disponível",Toast.LENGTH_SHORT).show();
        }
    }

    public void backToMenu(View view) {
        finish();
    }
}
