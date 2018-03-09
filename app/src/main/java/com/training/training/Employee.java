package com.training.training;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Employee extends AppCompatActivity
{
    DBEmployee objDBE;
    SQLiteDatabase objSQL;

    @BindView(R.id.edtnameEmp) EditText edtnameEmp;
    @BindView(R.id.edtlastName) EditText edtlastName;
    @BindView(R.id.edtlastNameMom) EditText edtlastNameMom;
    @BindView(R.id.edtbornDate) EditText edtbornDate;
    @BindView(R.id.edtemailEmp) EditText edtemailEmp;
    @BindView(R.id.edtphone) EditText edtphone;
    @BindView(R.id.spnGender) Spinner spnGender;
    @BindView(R.id.spnJob) Spinner spnJob;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        objDBE = new DBEmployee(this, "Trainings", null, 1);
        objSQL = objDBE.getWritableDatabase();

        ButterKnife.bind(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        String query = "SELECT * FROM job ORDER BY keyjob ASC";
        Cursor cursor = objSQL.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do
            {
                adapter.add(cursor.getString(1));
            }while (cursor.moveToNext());
            spnJob.setAdapter(adapter);
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        String query2 = "SELECT * FROM gender ORDER BY keygender ASC";
        Cursor cursor2 = objSQL.rawQuery(query2, null);
        if(cursor2.moveToFirst())
        {
            do
            {
                adapter2.add(cursor2.getString(1));
            }while (cursor2.moveToNext());
            spnGender.setAdapter(adapter2);
        }
    }

    @OnClick({R.id.btnAcept, R.id.btnaboutOf})
    public void onClick(View v)
    {
        if(v.getId() == R.id.btnAcept)
        {
            String nameEmp = edtnameEmp.getText().toString();
            String lastName = edtlastName.getText().toString();
            String lastNameMom = edtlastNameMom.getText().toString();
            String bornDate = edtbornDate.getText().toString();
            String emailEmp = edtemailEmp.getText().toString();
            String phone = edtphone.getText().toString();
            int keyGender = 0;
            int keyJob = 0;

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
            objSQL.execSQL("PRAGMA FOREIGN_KEYS = OFF");
            Toast.makeText(this, "The employee was inserted", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.btnaboutOf)
        {
            Intent intaboutOf = new Intent(Employee.this, AboutOf.class);
            Employee.this.startActivity(intaboutOf);
        }
    }
}