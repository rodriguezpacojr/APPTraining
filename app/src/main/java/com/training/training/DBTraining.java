package com.training.training;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by francisco on 3/2/18.
 */

public class DBTraining extends SQLiteOpenHelper
{
    String sql_employee = "CREATE TABLE employee ("+
            "keyEmployee INTEGER PRIMARY KEY, "+
            "nameEmp VARCHAR(50), "+
            "lastName VARCHAR(50), "+
            "lastNameMom VARCHAR(50), "+
            "bornDate date, "+
            "emailEmp VARCHAR(50), "+
            "phone VARCHAR(50), "+
            "keyGender INTEGER, "+
            "keyJob INTEGER, "+
            //"FOREIGN KEY (keyGender) REFERENCES gender (keygender)), "+
            "FOREIGN KEY (keyJob) REFERENCES job (keyjob))";

    String sql_job = "CREATE TABLE job ("+
            "keyJob INTEGER PRIMARY KEY, "+
            "nameJob VARCHAR(50))";

    String sql_gender = "CREATE TABLE gender ("+
            "keyGender INTEGER PRIMARY KEY, "+
            "description VARCHAR(10))";

    public DBTraining(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_job);
        db.execSQL("INSERT INTO job(nameJob) VALUES ('Manager')");
        db.execSQL("INSERT INTO job(nameJob) VALUES ('Head of department')");
        db.execSQL("INSERT INTO job(nameJob) VALUES ('Secretary')");
        db.execSQL("INSERT INTO job(nameJob) VALUES ('Treasurer')");
        db.execSQL("INSERT INTO job(nameJob) VALUES ('Teacher')");

        db.execSQL(sql_gender);
        db.execSQL("INSERT INTO gender(description) VALUES ('Male')");
        db.execSQL("INSERT INTO gender(description) VALUES ('Female')");

        db.execSQL(sql_employee);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
