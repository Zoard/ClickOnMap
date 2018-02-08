package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.zoardgeocze.clickonmap.Adapter.PendingCollabAdapter;
import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;

import java.util.ArrayList;
import java.util.List;

public class PendingCollabActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private Intent intent;
    private Bundle bundle;

    private User user;
    private VGISystem vgiSystem;
    private List<Collaboration> pendingCollabs = new ArrayList<>();

    private RecyclerView pendingCollabRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_collab);

        this.generalController = SingletonFacadeController.getInstance();

        this.pendingCollabRecycler = (RecyclerView) findViewById(R.id.pending_collab_recycler);

        this.intent = getIntent();
        this.bundle = this.intent.getExtras();


        this.vgiSystem = (VGISystem) this.bundle.getSerializable("vgiSystem");
        this.user = this.generalController.getUser(this.vgiSystem);

        this.pendingCollabs = this.generalController.getPendingCollaborations(this.vgiSystem.getAddress(),this.user.getId());

        Log.i("onCreatePending: ",String.valueOf(this.pendingCollabs.get(0).getTitle()));

        this.pendingCollabRecycler.setAdapter(new PendingCollabAdapter(this.pendingCollabs,this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        this.pendingCollabRecycler.setLayoutManager(layoutManager);

    }

    public void closePendingCollab(View view) {
        finish();
    }
}
