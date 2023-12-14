package com.innovapp.luk7.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;


import com.innovapp.luk7.arreglos.ProduccionArreglo;

import java.io.File;
import java.util.ArrayList;

public class ProduccionDB {


    private final ArrayList<String> columnas;
    private final ArrayList<Integer> columnas_index;


    private final String DATABASE_NAME = "Produccion.luk7";

    private String DATABASE_PATH = "";

    private final String TABLE_PRODUCCION = "Produccion";

    private final Context context;

    private SQLiteDatabase db;

    public ProduccionDB(Context paramContext) {
        this.context = paramContext;

        DATABASE_PATH = "" + context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()) + File.separator  + "Luk7";

        columnas = new ArrayList<>();
        columnas_index = new ArrayList<>();

        columnas.add("id");
        columnas.add("fecha_inical");
        columnas.add("fecha_fin");
        columnas.add("lpn_inicial");
        columnas.add("lpn_fin");
        columnas.add("fechainser");

        for(int a = 0 ; a <  columnas.size() ; a++){
            columnas_index.add(a);
        }





    }

    public boolean addProduccion(ProduccionArreglo produccionArreglo) {

        if(exist(produccionArreglo)){
            Log.e("exisytee", "puede ser existee");
            return updateData(produccionArreglo);
        }
        if (isOpen()) {
            ContentValues contentValues = new ContentValues();

            for(int f = 1 ; f < columnas.size();f++){
                contentValues.put( this.columnas.get(f) , produccionArreglo.get( this.columnas.get(f)  )) ;
            }

            try {
                if (-1L != this.db.insert(this.TABLE_PRODUCCION, null, contentValues)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public boolean updateData(ProduccionArreglo produccionArreglo){



        if (isOpen()) {
            ContentValues contentValues = new ContentValues();
            for(int f = 1 ; f < columnas.size();f++){
                contentValues.put( this.columnas.get(f) , produccionArreglo.get( this.columnas.get(f)  )) ;
            }

            int result = db.update(TABLE_PRODUCCION, contentValues, ""+this.columnas.get(columnas.indexOf("id"))+"=?", new String[]{ produccionArreglo.get( this.columnas.get(columnas.indexOf("id")))  });
            return result > 0;
        }return false;
    }

    public void close() {
        if (isOpen())
            this.db.close();
    }

    public void createTable() {

        StringBuilder str = new StringBuilder("CREATE TABLE \"" + this.TABLE_PRODUCCION);



        for(int f = 0 ; f < columnas.size();f++){
            if(f == 0){
                str.append("\" (" + "\"").append(columnas.get(f)).append("\"  INTEGER PRIMARY KEY AUTOINCREMENT ,");
            }else if(f < columnas.size() - 1 ){
                str.append("\"").append(columnas.get(f)).append("\"  TEXT NOT NULL,");
            }else {
                str.append("\"").append(columnas.get(f)).append("\"  TEXT NOT NULL);");
            }

        }

        this.db.execSQL(str.toString());
    }

    public void delete(String paramString) {
        if (!isOpen())
            return;
        this.db.delete(this.TABLE_PRODUCCION, this.columnas.get(columnas.indexOf("id")) + " = " + paramString + "", null);
    }


    public boolean exist(ProduccionArreglo produccionArreglo) {
        if (!isOpen())
            return false;

        return getProduccion(produccionArreglo.get(columnas.get(0)) ) != null;




    }
    public int zizeProduccion(){
        int zi = 0;
        if (!isOpen())
            return 0;
        Cursor cursor = this.db.query(false, this.TABLE_PRODUCCION, null, null, null, null, null, null, null);
        zi = cursor.getCount();
        cursor.close();
        return zi;

    }

    public ArrayList<ProduccionArreglo> getAllProduccion() {
        ArrayList<ProduccionArreglo> arrayList = new ArrayList<>();
        if (!isOpen())
            return arrayList;
        Cursor cursor = this.db.query(false, this.TABLE_PRODUCCION, null, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            while (true) {

                ProduccionArreglo produccionArreglo = new ProduccionArreglo();

                for(int t = 0 ; t < columnas.size() ; t++){
                    if(t ==0){
                        produccionArreglo.set(columnas.get(t), String.valueOf( cursor.getInt(this.columnas_index.get(t))) );
                    }else{
                        produccionArreglo.set(columnas.get(t),  cursor.getString(this.columnas_index.get(t)));
                    }

                }


                arrayList.add(produccionArreglo);
                if (!cursor.moveToNext()) {
                    cursor.close();
                    return arrayList;
                }
            }
        cursor.close();
        return arrayList;
    }
    public ProduccionArreglo getProduccion(String id) {

        if (!isOpen())
            return null;
        Cursor cursor = this.db.query(false, this.TABLE_PRODUCCION, null, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            while (true) {
                boolean bool;

                if(id.equals(String.valueOf(cursor.getInt( this.columnas_index.get(columnas.indexOf("id")))))){
                    ProduccionArreglo produccionArreglo = new ProduccionArreglo();

                    for(int t = 0 ; t < columnas.size() ; t++){

                        if(t ==0){
                            produccionArreglo.set(columnas.get(t), String.valueOf( cursor.getInt(this.columnas_index.get(t))) );
                        }else{
                            produccionArreglo.set(columnas.get(t),  cursor.getString(this.columnas_index.get(t)));
                        }


                    }

                    return produccionArreglo;
                }

                if (!cursor.moveToNext()) {
                    cursor.close();
                    return null;
                }
            }
        cursor.close();
        return null;
    }
    public ProduccionArreglo getProduccionfechainicialpn1(String fecha1 ,String lpn1) {

        if (!isOpen())
            return null;
        Cursor cursor = this.db.query(false, this.TABLE_PRODUCCION, null, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            while (true) {
                boolean bool;

                if(fecha1.equals(cursor.getString( this.columnas_index.get(columnas.indexOf("fecha_inical"))))  && lpn1.equals(cursor.getString( this.columnas_index.get(columnas.indexOf("lpn_inicial")))) ){
                    ProduccionArreglo produccionArreglo = new ProduccionArreglo();

                    for(int t = 0 ; t < columnas.size() ; t++){
                        if(t ==0){
                            produccionArreglo.set(columnas.get(t), String.valueOf( cursor.getInt(this.columnas_index.get(t))) );
                        }else{
                            produccionArreglo.set(columnas.get(t),  cursor.getString(this.columnas_index.get(t)));
                        }

                    }

                    return produccionArreglo;
                }

                if (!cursor.moveToNext()) {
                    cursor.close();
                    return null;
                }
            }
        cursor.close();
        return null;
    }

    public ProduccionArreglo getProduccionfechainser(String fechainsert) {

        if (!isOpen())
            return null;
        Cursor cursor = this.db.query(false, this.TABLE_PRODUCCION, null, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            while (true) {
                boolean bool;

                if(fechainsert.equals(cursor.getString( this.columnas_index.get(  columnas.indexOf("fechainser")   ))) ){
                    ProduccionArreglo produccionArreglo = new ProduccionArreglo();

                    for(int t = 0 ; t < columnas.size() ; t++){
                        if(t ==0){
                            produccionArreglo.set(columnas.get(t), String.valueOf( cursor.getInt(this.columnas_index.get(t))) );
                        }else{
                            produccionArreglo.set(columnas.get(t),  cursor.getString(this.columnas_index.get(t)));
                        }

                    }

                    return produccionArreglo;
                }

                if (!cursor.moveToNext()) {
                    cursor.close();
                    return null;
                }
            }
        cursor.close();
        return null;
    }

    public File getDatabasePath() {
        return this.context.getDatabasePath(this.DATABASE_NAME);
    }

    public boolean isOpen() {
        return (this.db != null) && this.db.isOpen();
    }

    @SuppressLint("Recycle")
    public void open() throws SQLException {





        File file = new File(this.DATABASE_PATH);
        if (!file.exists())
            file.mkdirs();


        try {
            this.db = SQLiteDatabase.openOrCreateDatabase(new File(this.DATABASE_PATH + File.separator + this.DATABASE_NAME), null);
        } catch (Exception e) {
            e.printStackTrace();
           // Toast.makeText(this.context, exception.getMessage(), Toast.LENGTH_LONG).show();
        }
        try {
            if (isOpen())
                this.db.query(this.TABLE_PRODUCCION, null, null, null, null, null, null, "1");
        } catch (Exception exception) {
            createTable();
        }
    }




}


