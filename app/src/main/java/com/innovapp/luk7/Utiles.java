package com.innovapp.luk7;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utiles {


    public static long getTimestampActual() {
        // Retorna el timestamp actual (milisegundos desde el Epoch)
        return System.currentTimeMillis();
    }

    public static String getFechayHora() {
        // Fecha y hora actual en formato yyyy-MM-dd HH:mm:ss
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    public static String getHora(String timestamp) {
        try {
            // Cambiar de timestamp a hora (solo la hora)
            long timestampLong = Long.parseLong(timestamp);
            Date date = new Date(timestampLong);
            SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
            return horaFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFecha(String timestamp) {
        try {
            // Cambiar de timestamp a fecha (solo la fecha)
            long timestampLong = Long.parseLong(timestamp);
            Date date = new Date(timestampLong);

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            return formatoFecha.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }




    public static void setTema(Context c ,int tema){
        // 0 automatico , 1 claro , 2 oscuro
        SharedPreferences sharedPreferences = c.getSharedPreferences("PreferenciasTema", MODE_PRIVATE);
        sharedPreferences.edit().putInt("tema", tema).apply();
        aplicarTema(c);

    }
    public static int getTema(Context c){
        return   c.getSharedPreferences("PreferenciasTema", MODE_PRIVATE).getInt("tema",0);
    }

    public static int Tema_claro = 1;
    public static int Tema_oscuro = 2;
    public static int Tema_automatico = 0;

    public static void aplicarTema(Context c) {

        switch (getTema(c)) {
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getTema(c));
        }

    }

    public static String obtenerDiferenciaTimestamps(String timestamp1, String timestamp2) {
        try {
            long timestamp1Long = Long.parseLong(timestamp1);
            long timestamp2Long = Long.parseLong(timestamp2);

            // Calcula la diferencia en milisegundos
            long diferenciaMillis = timestamp2Long - timestamp1Long;

            // Convierte la diferencia a horas, minutos y segundos
            long horas = TimeUnit.MILLISECONDS.toHours(diferenciaMillis);
            long minutos = TimeUnit.MILLISECONDS.toMinutes(diferenciaMillis) % 60;
            long segundos = TimeUnit.MILLISECONDS.toSeconds(diferenciaMillis) % 60;

            return String.format(Locale.getDefault(), "%d H, %d min, %d seg", horas, minutos, segundos);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



    public static void setImageBitmapqr(String text, String formatos, ImageView imageView) {

        try {

            BarcodeFormat formato = null;
            int ancho = 400;
            int alto = 400;

            switch (formatos) {
                case "QR_CODE" -> formato = BarcodeFormat.QR_CODE;
                case "CODE_128" -> {
                    formato = BarcodeFormat.CODE_128;
                    ancho = 600; // Rectangular
                    alto = 200; // Rectangular
                }
                case "CODABAR" -> {
                    formato = BarcodeFormat.CODABAR;
                    // Cuadrado
                    alto = 100; // Rectangular
                }
                case "CODE_39" -> {
                    formato = BarcodeFormat.CODE_39;
                    ancho = 600; // Rectangular
                    alto = 200; // Rectangular
                }
                case "DATA_MATRIX" -> {
                    formato = BarcodeFormat.DATA_MATRIX;
                    // Cuadrado
                    // Cuadrado
                }
                case "EAN_8" -> {
                    formato = BarcodeFormat.EAN_8;
                    // Cuadrado
                    alto = 200; // Rectangular
                }
                case "EAN_13" -> {
                    formato = BarcodeFormat.EAN_13;
                    // Cuadrado
                    alto = 200; // Rectangular
                }
                case "ITF" -> {
                    formato = BarcodeFormat.ITF;
                    ancho = 600; // Rectangular
                    alto = 200; // Rectangular
                }
                case "MAXICODE" -> {
                    formato = BarcodeFormat.MAXICODE;
                    // Cuadrado
                    // Cuadrado
                }
                case "PDF_417" -> {
                    formato = BarcodeFormat.PDF_417;
                    ancho = 600; // Rectangular
                    alto = 200; // Rectangular
                }
                case "RSS_14" -> {
                    formato = BarcodeFormat.RSS_14;
                    // Cuadrado

                }
                case "RSS_EXPANDED" -> {
                    formato = BarcodeFormat.RSS_EXPANDED;

                }
                case "UPC_A" -> {
                    formato = BarcodeFormat.UPC_A;

                }
                case "UPC_E" -> {
                    formato = BarcodeFormat.UPC_E;

                }
                case "UPC_EAN_EXTENSION" -> {
                    formato = BarcodeFormat.UPC_EAN_EXTENSION;

                }
                default ->
                        formato = BarcodeFormat.QR_CODE; // Establece un valor predeterminado si el formato no se reconoce
            }

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = null;

            bitmap = barcodeEncoder.encodeBitmap(text, formato, ancho, alto);

            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
