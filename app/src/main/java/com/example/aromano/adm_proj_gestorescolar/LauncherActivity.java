package com.example.aromano.adm_proj_gestorescolar;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    int CONTAINER_ID;
    String ACTIVE_FRAG_TAG;
    String LOGIN_FRAG_TAG = "LoginFragment";
    String REGISTER_FRAG_TAG = "RegisterFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        CONTAINER_ID = R.id.layout_fragholder;

        final FragmentManager fm = getSupportFragmentManager();

        if(savedInstanceState != null) {
            ACTIVE_FRAG_TAG = savedInstanceState.getString("ACTIVE_FRAG_TAG", "");
        } else {
            ACTIVE_FRAG_TAG = "";
        }

        if(ACTIVE_FRAG_TAG.isEmpty()) {
            LoginFragment loginFragment = new LoginFragment();
            fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(CONTAINER_ID, loginFragment, LOGIN_FRAG_TAG).commit();
            ACTIVE_FRAG_TAG = LOGIN_FRAG_TAG;
        } else if(fm.findFragmentByTag(ACTIVE_FRAG_TAG) != null){
            // active fragment didnt change, so we let the system handle it
        } else {
            fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(CONTAINER_ID, fm.findFragmentByTag(ACTIVE_FRAG_TAG)).commit();
        }
    }

    private void launchRegisterFragment() {
        final FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(REGISTER_FRAG_TAG) != null) {
            fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(CONTAINER_ID, fm.findFragmentByTag(REGISTER_FRAG_TAG), REGISTER_FRAG_TAG);
        } else {
            RegisterFragment registerFragment = new RegisterFragment();
            fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(CONTAINER_ID, registerFragment, REGISTER_FRAG_TAG).commit();
        }
        ACTIVE_FRAG_TAG = REGISTER_FRAG_TAG;
    }

    @Override
    public void onFragmentInteraction(String data) {
        if(data.contentEquals("LaunchRegisterFragment") && ACTIVE_FRAG_TAG.contentEquals(LOGIN_FRAG_TAG)) {
            launchRegisterFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ACTIVE_FRAG_TAG", ACTIVE_FRAG_TAG);
    }
}
