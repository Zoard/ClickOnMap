package com.example.zoardgeocze.clickonmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.EventCategory;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitClientInitializer;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.example.zoardgeocze.clickonmap.responses.DefaultDataResponse;
import com.example.zoardgeocze.clickonmap.responses.EventCategoryDataResponse;
import com.example.zoardgeocze.clickonmap.responses.UserDataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ZoardGeocze on 28/04/17.
 */

public class LoginActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private VGISystem vgiSystem;
    private Intent intent;
    private Bundle bundle;

    private EditText loginUserEmail;
    private EditText loginUserPassword;

    private Button loginButton;

    private String firebaseKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.generalController = SingletonFacadeController.getInstance();

        this.firebaseKey = this.generalController.getFirebaseKey();

        TextView title = (TextView) findViewById(R.id.login_title);
        this.loginUserEmail = (EditText) findViewById(R.id.login_user);
        this.loginUserPassword = (EditText) findViewById(R.id.login_password);

        this.intent = this.getIntent();
        this.bundle = intent.getExtras();

        this.vgiSystem = (VGISystem) this.bundle.getSerializable("vgiSystem");
        Log.i("onCreate_LoginActivity",vgiSystem.getAddress());

        title.setText(this.vgiSystem.getName());

        frontToRegister();
        verifyUser();

    }

    private void frontToRegister() {
        com.example.zoardgeocze.clickonmap.Design.AvenirBookTextView loginToRegisterButton = (com.example.zoardgeocze.clickonmap.Design.AvenirBookTextView)
                findViewById(R.id.login_register_btn);
        loginToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
    }

    public void backToMenu(View view) {
        finish();
    }

    //TODO: Finalizar implementação de Login
    //Botao para verificar usuario no sistema
    private void verifyUser() {

        this.loginButton = (Button) findViewById(R.id.login_btn);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = String.valueOf(loginUserEmail.getText());
                final String userPassword = String.valueOf(loginUserPassword.getText());

                if(!userEmail.equals("") && !userPassword.equals("")) {

                    final ProgressDialog mProgressDialog = new ProgressDialog(LoginActivity.this);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Verificando Usuário...");
                    mProgressDialog.show();

                    final String base_url = vgiSystem.getAddress() + "/";

                    Call<UserDataResponse> call = new RetrofitClientInitializer(base_url)
                                            .getUserService()
                                            .verifyUser("verifyUser",userEmail,userPassword,firebaseKey);

                    call.enqueue(new Callback<UserDataResponse>() {
                        @Override
                        public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {

                            Log.i("verify_user:", response.body().tag);

                            if(response.body().success == 1) {
                                getUserFromServer(base_url,userEmail); //TODO: USUÁRIO jÁ VEM NA RESPOSTA VERIFICAR ISTO AQUI
                                sendMobileSystemToServer(firebaseKey);
                            } else {
                                Toast.makeText(getBaseContext(),"Usuário não existe ou senha incorreta.",Toast.LENGTH_SHORT).show();
                            }

                            if (mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call<UserDataResponse> call, Throwable t) {

                            Log.i("verify_user:", t.getMessage());

                            //TODO: Implementar Alert Dialog
                            Toast.makeText(getBaseContext(),"Sem conexão de rede.",Toast.LENGTH_SHORT).show();

                            if (mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }

                        }
                    });
                } else {
                    Toast.makeText(getBaseContext(),"Todos os campos são obrigatórios.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getUserFromServer(final String base_url, final String userEmail) {

        new RetrofitClientInitializer(base_url)
                .getUserService()
                .getUserFromServer("getUser",userEmail)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserDataResponse>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ON_ERROR:", e.getMessage());
                    }

                    @Override
                    public void onNext(UserDataResponse response) {
                        loginVGISystem(response.user);
                    }

                });

    }

    //Realiza login no sistema VGI, se o tile já estiver no Hub, faz login direto.
    //Caso contrário, adiciona tile no Hub e registra usuário recebido pela requisição
    private void loginVGISystem(User user) {
        Log.i("loginVGISystem: ", user.getEmail());
        if(this.generalController.vgiSystemLogin(this.vgiSystem,user)) {
            Intent intent = new Intent(this,SystemActivity.class);
            intent.putExtras(this.bundle);
            startActivity(intent);
            finish();
        } else {
            final String base_url = vgiSystem.getAddress() + "/";
            getSystemCategories(base_url,user);
        }
    }

    private void getSystemCategories(final String base_url, final User user) {

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
                        registerConfirmation(response.categories,user);
                    }

                });
    }

    //Envia para o server central a chave do firebase junto com o endereço do sistema para futuras notificações
    private void sendMobileSystemToServer(String firebaseKey) {
        if (!firebaseKey.equals("")) {

            new RetrofitInitializer()
                    .getSystemService()
                    .sendMobileSystemToServer("sendMobileSystem",this.vgiSystem.getAddress(),firebaseKey)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<DefaultDataResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(DefaultDataResponse defaultDataResponse) {
                            //TODO: Implementar Alert Dialog
                        }

                    });
        }
    }

    //Confirma o registro do usuário no sistema
    private void registerConfirmation(List<EventCategory> eventCategories, User user) {

        this.vgiSystem.setCategory(eventCategories);
        bundle = intent.getExtras();
        bundle.putSerializable("vgiSystem",this.vgiSystem);
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);

        setResult(1,intent);
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            setResult(0,this.intent);
            finish();
        }
        else if(resultCode == 1) {
            Bundle bundle = data.getExtras();
            this.intent.putExtras(bundle);
            setResult(1,this.intent);
            finish();
        }
    }
}
