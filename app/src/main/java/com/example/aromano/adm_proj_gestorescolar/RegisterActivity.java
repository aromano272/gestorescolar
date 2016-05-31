package com.example.aromano.adm_proj_gestorescolar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText et_nome;
    EditText et_email;
    EditText et_username;
    EditText et_password;
    EditText et_confirmarpassword;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_nome = (EditText) findViewById(R.id.et_nome);
        et_email = (EditText) findViewById(R.id.et_email);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirmarpassword = (EditText) findViewById(R.id.et_confirmarpassword);
        db = DBHelper.getInstance(this);

    }

    private void registerAluno() {
        String nome, email, username, password, confirmarpassword;
        nome = et_nome.getText().toString();
        email = et_email.getText().toString();
        username = et_username.getText().toString();
        password = et_password.getText().toString();
        confirmarpassword = et_confirmarpassword.getText().toString();

        if(nome.length() < 3) {
            et_nome.setError("O nome tem que ter pelo menos 3 letras.");
            return;
        } else if (!isValidEmail(email)) {
            et_email.setError("Email inválido.");
            return;
        } else if(username.length() < 4) {
            et_username.setError("O username tem que ter pelo menos 4 letras.");
            return;
        } else if(db.readAlunos(username) != null) {
            et_username.setError("Este username não está disponivel.");
            return;
        } else if(password.length() < 4) {
            et_password.setError("A password tem que ter pelo menos 4 letras.");
            return;
        } else if(!password.contentEquals(confirmarpassword)) {
            et_confirmarpassword.setError("Ambas as passwords têm que ser iguais.");
            return;
        }

        Aluno aluno = new Aluno(username, nome, email);
        SharedPreferences users_pass = getSharedPreferences("users_pass", MODE_PRIVATE);
        users_pass.edit().putString(username, password).apply();

        aluno.setIdaluno(db.createAlunos(aluno));
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.putExtra("aluno", aluno);
        // starts it as a new task and clears the backstack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.register_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_registerAluno:
                registerAluno();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
