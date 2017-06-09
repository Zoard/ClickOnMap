package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by ZoardGeocze on 28/04/17.
 */

public class RegisterActivity extends AppCompatActivity {

    private User user;

    private VGISystem vgiSystem;

    private EditText registerUserName;
    private EditText registerUserEmail;
    private EditText registerPassword;
    private EditText registerPasswordConfirmation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        this.registerUserName = (EditText) findViewById(R.id.register_user_name);
        this.registerUserEmail = (EditText) findViewById(R.id.register_user_email);
        this.registerPassword = (EditText) findViewById(R.id.register_password);
        this.registerPasswordConfirmation = (EditText) findViewById(R.id.register_password_confirmation);

    }

    public void backToLogin(View view) {
        //overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        Intent intent = getIntent();
        setResult(0,intent);
        finish();
    }

    //Falta implementar verificação para saber se Usuário já Existe no Sistema
    public void registerUser(View view) {

        String userName = String.valueOf(this.registerUserName.getText());
        String userEmail = String.valueOf(this.registerUserEmail.getText());
        String userPass = String.valueOf(this.registerPassword.getText());
        String userPassConfirm = String.valueOf(this.registerPasswordConfirmation.getText());

        if(!userName.equals("") && !userEmail.equals("") && !userPass.equals("") && !userPassConfirm.equals("")) {
            if(userPass.equals(userPassConfirm)) {

                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());

                this.user = new User(userEmail,userName,userPass,String.valueOf(timestamp));

                Intent intent = getIntent();

                //Bundle bundle = new Bundle();
                Bundle bundle = intent.getExtras();
                bundle.putSerializable("user",this.user);

                intent.putExtras(bundle);

                setResult(1,intent);
                finish();

            } else {
                Toast.makeText(this,"Senhas são diferentes.",Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this,"Todos os campos são obrigatórios.",Toast.LENGTH_SHORT).show();
        }

    }
}
