package com.innovapp.luk7.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.innovapp.luk7.arreglos.ReportesArreglo;

import java.io.File;
import java.util.ArrayList;

public class ReportesDB {


    private final ArrayList<String> columnas;
    private final ArrayList<Integer> columnas_index;


    private final String DATABASE_NAME = "Reportes.luk7";

    private String DATABASE_PATH = "";

    private final String TABLE_REPORTES = "Reportes";

    private final Context context;

    private SQLiteDatabase db;

    public ReportesDB(Context paramContext) {
        this.context = paramContext;

        DATABASE_PATH = "" + context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()) + File.separator  + "Luk7";

        columnas = new ArrayList<>();
        columnas_index = new ArrayList<>();

        columnas.add("id");
        columnas.add("titulo");
        columnas.add("descripcion");
        columnas.add("fecha");


        for(int a = 0 ; a <  columnas.size() ; a++){
            columnas_index.add(a);
        }





    }

    public boolean addReportes(ReportesArreglo reportesArreglo) {

        if(exist(reportesArreglo)){
            Log.e("exisytee", "puede ser existee");
            return updateData(reportesArreglo);
        }
        if (isOpen()) {
            ContentValues contentValues = new ContentValues();

            for(int f = 1 ; f < columnas.size();f++){
                contentValues.put( this.columnas.get(f) , reportesArreglo.get( this.columnas.get(f)  )) ;
            }

            try {
                if (-1L != this.db.insert(this.TABLE_REPORTES, null, contentValues)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public boolean updateData(ReportesArreglo reportesArreglo){



        if (isOpen()) {
            ContentValues contentValues = new ContentValues();
            for(int f = 1 ; f < columnas.size();f++){
                contentValues.put( this.columnas.get(f) , reportesArreglo.get( this.columnas.get(f)  )) ;
            }

            int result = db.update(TABLE_REPORTES, contentValues, ""+this.columnas.get(columnas.indexOf("id"))+"=?", new String[]{ reportesArreglo.get( this.columnas.get(columnas.indexOf("id")))  });
            return result > 0;
        }return false;
    }

    public void close() {
        if (isOpen())
            this.db.close();
    }

    public void createTable() {

        StringBuilder str = new StringBuilder("CREATE TABLE \"" + this.TABLE_REPORTES);



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
        this.db.delete(this.TABLE_REPORTES, this.columnas.get(columnas.indexOf("id")) + " = " + paramString + "", null);
    }


    public boolean exist(ReportesArreglo reportesArreglo) {
        if (!isOpen())
            return false;

        return getReportes(reportesArreglo.get(columnas.get(0)) ) != null;




    }
    public int zizeReportes(){
        int zi = 0;
        if (!isOpen())
            return 0;
        Cursor cursor = this.db.query(false, this.TABLE_REPORTES, null, null, null, null, null, null, null);
        zi = cursor.getCount();
        cursor.close();
        return zi;

    }

    public ArrayList<ReportesArreglo> getAllReportes() {
        ArrayList<ReportesArreglo> arrayList = new ArrayList<>();
        if (!isOpen())
            return arrayList;
        Cursor cursor = this.db.query(false, this.TABLE_REPORTES, null, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            while (true) {

                ReportesArreglo reportesArreglo = new ReportesArreglo();

                for(int t = 0 ; t < columnas.size() ; t++){
                    if(t ==0){
                        reportesArreglo.set(columnas.get(t), String.valueOf( cursor.getInt(this.columnas_index.get(t))) );
                    }else{
                        reportesArreglo.set(columnas.get(t),  cursor.getString(this.columnas_index.get(t)));
                    }

                }


                arrayList.add(reportesArreglo);
                if (!cursor.moveToNext()) {
                    cursor.close();
                    return arrayList;
                }
            }
        cursor.close();
        return arrayList;
    }
    public ReportesArreglo getReportes(String id) {

        if (!isOpen())
            return null;
        Cursor cursor = this.db.query(false, this.TABLE_REPORTES, null, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            while (true) {
                boolean bool;

                if(id.equals(String.valueOf(cursor.getInt( this.columnas_index.get(columnas.indexOf("id")))))){
                    ReportesArreglo reportesArreglo = new ReportesArreglo();

                    for(int t = 0 ; t < columnas.size() ; t++){

                        if(t ==0){
                            reportesArreglo.set(columnas.get(t), String.valueOf( cursor.getInt(this.columnas_index.get(t))) );
                        }else{
                            reportesArreglo.set(columnas.get(t),  cursor.getString(this.columnas_index.get(t)));
                        }


                    }

                    return reportesArreglo;
                }

                if (!cursor.moveToNext()) {
                    cursor.close();
                    return null;
                }
            }
        cursor.close();
        return null;
    }
    public ReportesArreglo getReportesfechatitulo(String fecha1 ,String titulo) {

        if (!isOpen())
            return null;
        Cursor cursor = this.db.query(false, this.TABLE_REPORTES, null, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            while (true) {
                boolean bool;

                if(fecha1.equals(cursor.getString( this.columnas_index.get(columnas.indexOf("fecha"))))  && titulo.equals(cursor.getString( this.columnas_index.get(columnas.indexOf("titulo")))) ){
                    ReportesArreglo reportesArreglo = new ReportesArreglo();

                    for(int t = 0 ; t < columnas.size() ; t++){
                        if(t ==0){
                            reportesArreglo.set(columnas.get(t), String.valueOf( cursor.getInt(this.columnas_index.get(t))) );
                        }else{
                            reportesArreglo.set(columnas.get(t),  cursor.getString(this.columnas_index.get(t)));
                        }

                    }

                    return reportesArreglo;
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
                this.db.query(this.TABLE_REPORTES, null, null, null, null, null, null, "1");
        } catch (Exception exception) {
            createTable();
        }
    }




}


