package com.training.training;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Settings extends AppCompatActivity {
    @BindView(R.id.edtipAddress) EditText edtipAddress;
    @BindView(R.id.edtportNumber) EditText edtportNumber;

    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        myEditor = myPreferences.edit();
        String ipAddress = myPreferences.getString("ipAddress", "unknow");
        String portNumber = myPreferences.getString("portNumber", "unknow");
        edtipAddress.setText(ipAddress);
        edtportNumber.setText(portNumber);
    }

    @OnClick(R.id.btnok)
    public void click1() {
        String ipAddress = edtipAddress.getText().toString();
        String portNumber = edtportNumber.getText().toString();

        if(edtipAddress.getText().toString().equals("") || edtportNumber.getText().toString().equals(""))
            Toast.makeText(this,"Fill all the fields",Toast.LENGTH_SHORT).show();
        else
        {
            myEditor.putString("ipAddress",ipAddress);
            myEditor.putString("portNumber",portNumber);
            myEditor.commit();

            Intent intLogin = new Intent(this, Login.class);
            startActivity(intLogin);
            finish();
        }
    }
}