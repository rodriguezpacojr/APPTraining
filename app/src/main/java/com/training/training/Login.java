package com.training.training;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    @BindView(R.id.edtuser) EditText edtuser;
    @BindView(R.id.edtpassword) EditText edtpassword;
    @BindView(R.id.chbsession) CheckBox chbsession;
    @BindView(R.id.btnlogIn) Button btnlogIn;
    @BindView(R.id.btnsettings) FloatingActionButton btnsettings;

    String ipAddress, portNumber;
    SharedPreferences.Editor myEditor;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ipAddress = myPreferences.getString("ipAddress", "unknow");
        portNumber = myPreferences.getString("portNumber", "unknow");
        if(ipAddress.equals("unknow") || portNumber.equals("unknow")){
            ipAddress = "127.0.0.1";
            portNumber = "8080";
        }
        myEditor = myPreferences.edit();
        myEditor.putString("ipAddress", ipAddress);
        myEditor.putString("portNumber", portNumber);

        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        Log.e("volley_error", error.toString());
    }

    @Override
    public void onResponse(String response) {
        Log.d("volley_response", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String token = jsonObject.getString("token");
            Setting.token = token;
            if (!token.equalsIgnoreCase("Access Denied"))
                startActivity(new Intent(this, SplashScreen.class));
            else
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnlogIn, R.id.btnsettings})
    public void onClick(View v) {
        if (v.getId() == R.id.btnlogIn){
            if (edtuser.getText().toString().equals("") || edtpassword.getText().toString().equals(""))
                Toast.makeText(this,"Fill all the fields",Toast.LENGTH_SHORT).show();
            else
                validate();
        }

        else if (v.getId() == R.id.btnsettings) {
            Intent intConf = new Intent(this, Settings.class);
            startActivity(intConf);
            finish();
        }
    }

    void validate() {
        String URL = "http://"+ipAddress+":"+portNumber+"/Training/api/usr/validate/";
        String user = edtuser.getText().toString();
        String password = edtpassword.getText().toString();
        URL += user + "/" + password;

        stringRequest = new StringRequest(Request.Method.GET, URL, this, this){
            @Override
            public Map<String, String> getHeaders () {
                Map<String, String> params = new HashMap<>();
                params.put(
                    "Authorization",
                    String.format("Basic %s", Base64.encodeToString(
                            String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                return params;
            }
            };
        requestQueue.add(stringRequest);
    }
}