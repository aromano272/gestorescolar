package com.example.aromano.adm_proj_gestorescolar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
    Aluno aluno;
    long timer_start = 0;

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

        if(getIntent().getParcelableExtra("aluno") != null) {
            aluno = getIntent().getParcelableExtra("aluno");
            et_user.setText(aluno.getUsername());
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
                        } else {
                            default_user.edit().putString("default_user", "").apply();
                        }
                        launchContentActivity(user);
                    } else {
                        et_pass.setError("Wrong password!");
                    }
                } else {
                    et_user.setError("Wrong username!");
                }
            }
        });

        tv_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void launchContentActivity(String user) {
        Aluno aluno = db.readAlunos(user);

        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("aluno", aluno);
        // starts it as a new task and clears the backstack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if(resultCode == Activity.RESULT_OK) {
                    String user = data.getStringExtra("user");
                    et_user.setText(user);
                    SharedPreferences users_pass = getSharedPreferences("users_pass", MODE_PRIVATE);
                    et_pass.setText(users_pass.getString(user, ""));
                    et_pass.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int delay = 2000;
        long timer_now = System.currentTimeMillis();

        if (timer_now - timer_start < delay) {
            finish();
        } else {
            timer_start = timer_now;
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
        }
    }

}
