package com.example.aromano.adm_proj_gestorescolar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextView tv_register;
    TextView tv_forgotpass;
    EditText et_user;
    EditText et_pass;
    Button btn_login;
    CheckBox cb_rememberme;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = DBHelper.getInstance(this);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forgotpass = (TextView) findViewById(R.id.tv_forgotpass);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pass = (EditText) findViewById(R.id.et_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        cb_rememberme = (CheckBox) findViewById(R.id.cb_rememberme);


        tv_register.setPaintFlags(tv_register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_forgotpass.setPaintFlags(tv_forgotpass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final SharedPreferences users_pass = getSharedPreferences("users_pass", MODE_PRIVATE);
        final SharedPreferences default_user = getSharedPreferences("default_user", MODE_PRIVATE);

        if(default_user.getString("default_user", null) != null) {
            et_user.setText(default_user.getString("default_user", ""));
            cb_rememberme.setChecked(true);
        }

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = et_user.getText().toString();
                String pass = et_pass.getText().toString();

                if (users_pass.getString(user, null) != null) {
                    if (users_pass.getString(user, null).contentEquals(pass)) {
                        if(cb_rememberme.isChecked()) {
                            default_user.edit().putString("default_user", user).apply();
                        }
                        launchContentActivity(user);
                    } else {
                        Toast.makeText(LoginActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong username!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void launchContentActivity(String user) {
        Aluno aluno = db.readAlunos(user);

        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("aluno", aluno);
        startActivity(intent);
    }

}
