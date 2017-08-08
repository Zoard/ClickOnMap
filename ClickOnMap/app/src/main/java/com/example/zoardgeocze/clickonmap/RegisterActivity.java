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

import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;

import java.sql.Timestamp;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZoardGeocze on 28/04/17.
 */

public class RegisterActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private User user;

    private VGISystem vgiSystem;

    private EditText registerUserName;
    private EditText registerUserEmail;
    private EditText registerPassword;
    private EditText registerPasswordConfirmation;
    private Button registerButton;

    private boolean sendMobileSystemVerifier = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        this.generalController = SingletonFacadeController.getInstance();

        this.registerUserName = (EditText) findViewById(R.id.register_user_name);
        this.registerUserEmail = (EditText) findViewById(R.id.register_user_email);
        this.registerPassword = (EditText) findViewById(R.id.register_password);
        this.registerPasswordConfirmation = (EditText) findViewById(R.id.register_password_confirmation);

        registerUser();

    }

    public void backToLogin(View view) {
        //overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        Intent intent = getIntent();
        setResult(0,intent);
        finish();
    }

    //TODO: Falta implementar verificação para saber se Usuário já Existe no Sistema
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

                        Intent intent = getIntent();
                        Bundle bundle = intent.getExtras();

                        vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");
                        String firebaseKey = generalController.getFirebaseKey();

                        if(sendMobileSystemToServer(firebaseKey)) {
                            Date date = new Date();
                            Timestamp timestamp = new Timestamp(date.getTime());

                            user = new User(userEmail,userEmail,userName,userPass,String.valueOf(timestamp));

                            bundle.putSerializable("user",user);

                            intent.putExtras(bundle);

                            setResult(1,intent);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(),"Entrou antes de requisitar no sistema.",Toast.LENGTH_SHORT).show();
                            setResult(0,intent);
                            finish();
                        }

                    } else {
                        Toast.makeText(getBaseContext(),"Senhas são diferentes.",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getBaseContext(),"Todos os campos são obrigatórios.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean sendMobileSystemToServer(String firebaseKey) {
        if (!firebaseKey.equals("")) {

            /*final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Carregando Sistemas VGI...");
            mProgressDialog.show();*/

            Call<String> call = new RetrofitInitializer()
                    .getSystemService()
                    .sendMobileSystemToServer("sendMobileSystem",firebaseKey,this.vgiSystem.getAdress());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i("SendMobileSystem", response.body());
                    sendMobileSystemVerifier = true;

                    /*if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }*/
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("SendMobileSystem", t.getMessage());
                    sendMobileSystemVerifier = false;

                    /*if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }*/
                }
            });
        }

        return sendMobileSystemVerifier;
    }
}
