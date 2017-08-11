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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        getSystemsFromServer();

        this.swipeSystemList = (SwipeRefreshLayout) findViewById(R.id.swipe_system_list);
        this.swipeSystemList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSystemsFromServerRefresh();
            }
        });


    }


    private void getSystemsFromServer() {

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Carregando Sistemas VGI...");
        mProgressDialog.show();

        Call<VGISystemSync> call = new RetrofitInitializer().getSystemService().getVGISystemList("getVGISystem");
        call.enqueue(new Callback<VGISystemSync>() {
            @Override
            public void onResponse(Call<VGISystemSync> call, Response<VGISystemSync> response) {

                VGISystemSync vgiSystemSync = response.body();

                if(vgiSystemSync != null) {
                    for (VGISystem vs : vgiSystemSync.getVgiSystems()) {
                        if(generalController.searchVGISystem(vs)) {
                            vgiSystems.add(vs);
                        }
                    }
                }


                Log.i("onResponse_VGISYSTEM: ", String.valueOf(vgiSystems.size()));

                if(!vgiSystems.isEmpty()) {
                    loadAvailableVGISystems();
                } else {
                    Toast.makeText(getBaseContext(),"Nenhum sistema VGI disponível",Toast.LENGTH_SHORT).show();
                }

                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<VGISystemSync> call, Throwable t) {
                Log.i("onFailure_VGISYSTEM: ", t.getMessage());

                Toast.makeText(getBaseContext(),"Sem conexão",Toast.LENGTH_SHORT).show();

                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            }
        });

    }

    private void getSystemsFromServerRefresh() {

        Call<VGISystemSync> call = new RetrofitInitializer().getSystemService().getVGISystemList("getVGISystem");
        call.enqueue(new Callback<VGISystemSync>() {
            @Override
            public void onResponse(Call<VGISystemSync> call, Response<VGISystemSync> response) {

                VGISystemSync vgiSystemSync = response.body();

                if(vgiSystemSync != null) {
                    for (VGISystem vs : vgiSystemSync.getVgiSystems()) {
                        if(generalController.searchVGISystem(vs)) {
                            vgiSystems.add(vs);
                        }
                    }
                }


                Log.i("onResponse_VGISYSTEM: ", String.valueOf(vgiSystems.size()));

                if(!vgiSystems.isEmpty()) {
                    loadAvailableVGISystems();
                } else {
                    Toast.makeText(getBaseContext(),"Nenhum sistema VGI disponível",Toast.LENGTH_SHORT).show();
                }

                swipeSystemList.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<VGISystemSync> call, Throwable t) {
                Log.i("onFailure_VGISYSTEM: ", t.getMessage());

                Toast.makeText(getBaseContext(),"Sem conexão",Toast.LENGTH_SHORT).show();

                swipeSystemList.setRefreshing(false);
            }
        });

    }

    private void loadAvailableVGISystems() {
        this.addSystemRecycler.setAdapter(new AddSystemAdapter(this.vgiSystems,this));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        this.addSystemRecycler.setLayoutManager(layout);
    }

    public void backToMenu(View view) {
        finish();
    }
}
