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
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.EventCategory;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitClientInitializer;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.example.zoardgeocze.clickonmap.responses.DefaultDataResponse;
import com.example.zoardgeocze.clickonmap.responses.EventCategoryDataResponse;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
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

public class RegisterActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private User user;

    private String firebaseKey;

    private VGISystem vgiSystem;

    private EditText registerUserName;
    private EditText registerUserEmail;
    private EditText registerPassword;
    private EditText registerPasswordConfirmation;
    private Button registerButton;

    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        this.generalController = SingletonFacadeController.getInstance();

        this.firebaseKey = this.generalController.getFirebaseKey();

        this.registerUserName = (EditText) findViewById(R.id.register_user_name);
        this.registerUserEmail = (EditText) findViewById(R.id.register_user_email);
        this.registerPassword = (EditText) findViewById(R.id.register_password);
        this.registerPasswordConfirmation = (EditText) findViewById(R.id.register_password_confirmation);

        this.intent = getIntent();

        registerUser();

    }

    public void backToLogin(View view) {
        //overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        Intent intent = getIntent();
        setResult(0,intent);
        finish();
    }

    //TODO: Falta implementar verificação para saber se Usuário já Existe no Sistema
    //Botao para registrar o usuário
    public void registerUser() {

        this.registerButton = (Button) findViewById(R.id.register_btn);
        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = String.valueOf(registerUserName.getText());
                String userEmail = String.valueOf(registerUserEmail.getText());
                String userPass = String.valueOf(registerPassword.getText());
                String userPassConfirm = String.valueOf(registerPasswordConfirmation.getText());

                if(!userName.equals("") && !userEmail.equals("") && !userPass.equals("") && !userPassConfirm.equals("")) {
                    if(userPass.equals(userPassConfirm)) {

                        Date date = new Date();
                        Timestamp timestamp = new Timestamp(date.getTime());

                        user = new User(userEmail,userEmail,userName,userPass,String.valueOf(timestamp));

                        Intent intent = getIntent();
                        Bundle bundle = intent.getExtras();

                        vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

                        sendUserToServer();

                    } else {
                        Toast.makeText(getBaseContext(),"Senhas são diferentes.",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getBaseContext(),"Todos os campos são obrigatórios.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Confirma o registro do usuário no sistema
    private void registerConfirmation(List<EventCategory> eventCategories) {

        this.vgiSystem.setCategory(eventCategories);
        bundle = intent.getExtras();
        bundle.putSerializable("vgiSystem",this.vgiSystem);
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);

        setResult(1,intent);
        finish();

    }

    //TODO: Terminar a implementação na parte do servidor
    private void sendUserToServer() {
        final ProgressDialog mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Cadastrando usuário no sistema...");
        mProgressDialog.show();

        final String base_url = this.vgiSystem.getAddress() + "/";
        Log.i("sendUserToServer: ", base_url);

        Call<DefaultDataResponse> call = new RetrofitClientInitializer(base_url)
                .getUserService()
                .sendUserToServer("insertUser",
                this.user.getEmail(),this.user.getName(),this.user.getPassword(),
                        this.user.getType(),this.user.getRegisterDate(),this.firebaseKey);

        call.enqueue(new Callback<DefaultDataResponse>() {
            @Override
            public void onResponse(Call<DefaultDataResponse> call, Response<DefaultDataResponse> response) {
                Log.i("Register_User_Server: ", response.body().tag);

                if(response.body().success == 1) {
                    sendMobileSystemToServer(firebaseKey);
                    getSystemCategories(base_url);
                } else {
                    Toast.makeText(getBaseContext(),response.body().error_msg,Toast.LENGTH_SHORT).show();
                }

                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DefaultDataResponse> call, Throwable t) {
                Log.i("Register_User_Server:", t.getMessage());

                Toast.makeText(getBaseContext(),"Sem conexão de rede.",Toast.LENGTH_SHORT).show();

                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
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
                            //TODO: Implement Alert Dialog
                        }

                    });
        }
    }

    private void getSystemCategories(final String base_url) {

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
                        registerConfirmation(response.categories);
                    }

                });

    }
}



















