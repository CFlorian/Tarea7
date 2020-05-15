package com.cksolutions.tarea7.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class dbData {
    private static final int DBVER = 1  ;
    private static final String DBNAME = "TAREA7";
    private DBHelper conn;
    @SuppressWarnings("unused")
    private Context context;
    private String strResultado;

    public dbData(Context ctx) {
        this.context = ctx;
        conn = new DBHelper(ctx);
    }

    public String inserta_foto(String nombre) {

        SQLiteDatabase db;
        db = conn.getWritableDatabase();
        String strSql = "";

        strSql = "";
        strSql += "INSERT INTO file (Nombre) ";
        strSql += "VALUES(";
        strSql += "'" + nombre + "')";
        db.execSQL(strSql);
        db.close();
        return "Foto Grabada!!";
    }

    public ArrayList<String> getFotos() {
        ArrayList<String> my_array = new ArrayList<String>();
        my_array.clear();
        SQLiteDatabase db;
        db = conn.getWritableDatabase();
        Cursor allrows = db.rawQuery("SELECT Nombre FROM file ", null);
        if (allrows.moveToFirst()) {
            do {
                String nombre = allrows.getString(0);
                my_array.add(nombre);
            } while (allrows.moveToNext());
        }
        allrows.close();
        db.close();
        return my_array;
    }


    class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DBNAME, null, DBVER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String strSql = "CREATE TABLE file ("
                    + " Id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " Nombre TEXT )";
            db.execSQL(strSql);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (newVersion > oldVersion) {

                String strSql = "DROP TABLE IF EXISTS file";
                db.execSQL(strSql);

                strSql = "CREATE TABLE file ("
                        + " Id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " Nombre TEXT )";
                db.execSQL(strSql);
            }

        }



    }
}
