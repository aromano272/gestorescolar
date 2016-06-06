package com.example.aromano.adm_proj_gestorescolar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
    }

    private void validatePass() {
        EditText et_username = (EditText) findViewById(R.id.et_username);
        EditText et_email = (EditText) findViewById(R.id.et_email);
        EditText et_nome = (EditText) findViewById(R.id.et_nome);

        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String nome = et_nome.getText().toString();

        DBHelper db = DBHelper.getInstance(this);
        Aluno aluno = db.readAlunos(username);

        if(aluno != null) {
            if (aluno.getEmail().contentEquals(email) && aluno.getNome().contentEquals(nome)) {
                Intent intent = new Intent();
                intent.putExtra("user", username);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        } else {
            Toast.makeText(ForgotPassActivity.this, "Dados introduzidos estão errados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.forgot_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_forgot:
                validatePass();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
