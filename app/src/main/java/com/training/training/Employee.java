package com.training.training;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Employee extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    DBTraining objDBE;
    SQLiteDatabase objSQL;

    @BindView(R.id.edtnameEmp) EditText edtnameEmp;
    @BindView(R.id.edtlastName) EditText edtlastName;
    @BindView(R.id.edtlastNameMom) EditText edtlastNameMom;
    @BindView(R.id.edtbornDate) EditText edtbornDate;
    @BindView(R.id.edtemailEmp) EditText edtemailEmp;
    @BindView(R.id.edtphone) EditText edtphone;
    @BindView(R.id.edtrfc) EditText edtrfc;
    @BindView(R.id.spnGender) Spinner spnGender;
    @BindView(R.id.spnJob) Spinner spnJob;

    String ipAddress, portNumber;
    SharedPreferences.Editor myEditor;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        objDBE = new DBTraining(this, "Trainings", null, 1);
        objSQL = objDBE.getWritableDatabase();

        ButterKnife.bind(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        String query = "SELECT * FROM job ORDER BY keyjob ASC";
        Cursor cursor = objSQL.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                adapter.add(cursor.getString(1));
            }while (cursor.moveToNext());
            spnJob.setAdapter(adapter);
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        String query2 = "SELECT * FROM gender ORDER BY keygender ASC";
        Cursor cursor2 = objSQL.rawQuery(query2, null);
        if(cursor2.moveToFirst()) {
            do {
                adapter2.add(cursor2.getString(1));
            }while (cursor2.moveToNext());
            spnGender.setAdapter(adapter2);
        }

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        myEditor = myPreferences.edit();
        ipAddress = myPreferences.getString("ipAddress", "unknow");
        portNumber = myPreferences.getString("portNumber", "unknow");

        requestQueue = Volley.newRequestQueue(this);
    }

    @OnClick({R.id.btnAcept, R.id.btnaboutOf})
    public void onClick(View v) {
        if(v.getId() == R.id.btnAcept) {
            String nameEmp = edtnameEmp.getText().toString();
            String lastName = edtlastName.getText().toString();
            String lastNameMom = edtlastNameMom.getText().toString();
            String bornDate = edtbornDate.getText().toString();
            String emailEmp = edtemailEmp.getText().toString();
            String phone = edtphone.getText().toString();
            String rfc = edtrfc.getText().toString();
            String keyGender, keyJob, keyUser;
            keyGender = keyJob = keyUser = "1";

            String URL = "http://"+ipAddress+":"+portNumber+"/Training/api/employee/insertemployee/";
            URL += Setting.token;

            jsonObject =  new JSONObject();
            try {
                jsonObject.put("nameEmp", nameEmp);
                jsonObject.put("lastName", lastName);
                jsonObject.put("lastNameMom", lastNameMom);
                jsonObject.put("bornDate", bornDate);
                jsonObject.put("emailEmp", emailEmp);
                jsonObject.put("phone", phone);
                jsonObject.put("RFC", rfc);
                jsonObject.put("keyGender", keyGender);
                jsonObject.put("keyJob", keyJob);
                jsonObject.put("keyUser", keyUser);
            }
            catch (Exception e) {
                Log.e("Error", e.toString());
            }

            System.out.println(jsonObject);
            stringRequest = new StringRequest(Request.Method.POST, URL, this, this) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonObject.toString().getBytes("utf-8");
                    }
                    catch (UnsupportedEncodingException uee) {
                        Log.e("Error", uee.toString());
                        return null;
                    }
                }
                @Override
                public Map<String, String> getHeaders () {
                    Map<String, String> params = new HashMap<>();
                    params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };
            requestQueue.add(stringRequest);

            /*
            String query = "SELECT * FROM job WHERE namejob = '" + spnJob.getSelectedItem().toString() + "'";
            Cursor cursor = objSQL.rawQuery(query, null);
            if (cursor.moveToFirst())
                keyJob = cursor.getInt(0);

            String query2 = "SELECT * FROM gender WHERE description = '" + spnGender.getSelectedItem().toString() + "'";
            Cursor cursor2 = objSQL.rawQuery(query2, null);
            if (cursor2.moveToFirst())
                keyGender = cursor.getInt(0);

            String sql = "INSERT INTO employee(nameemp, lastname, lastnamemom, borndate, emailemp, phone, keygender, keyjob) VALUES" +
                    " ('" + nameEmp + "','" + lastName + "','" + lastNameMom + "','" + bornDate+"', '" + emailEmp + "','" + phone + "', " + keyGender + ", "+ keyJob + ")";

            objSQL.execSQL("PRAGMA FOREIGN_KEYS = ON");
            objSQL.execSQL(sql);
            objSQL.execSQL("PRAGMA FOREIGN_KEYS = OFF");*/
            Toast.makeText(this, "The employee was inserted", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.btnaboutOf) {
            Intent intaboutOf = new Intent(Employee.this, AboutOf.class);
            Employee.this.startActivity(intaboutOf);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

    }
}