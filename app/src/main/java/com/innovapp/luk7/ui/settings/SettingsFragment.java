package com.innovapp.luk7.ui.settings;

import static com.innovapp.luk7.Utiles.Tema_automatico;
import static com.innovapp.luk7.Utiles.Tema_claro;
import static com.innovapp.luk7.Utiles.Tema_oscuro;
import static com.innovapp.luk7.Utiles.getTema;
import static com.innovapp.luk7.Utiles.setTema;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.innovapp.luk7.R;
import com.innovapp.luk7.Utiles;
import com.innovapp.luk7.arreglos.ReportesArreglo;
import com.innovapp.luk7.databinding.FragmentSettingsBinding;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    LinearLayout desarrollador,compartirluck,websss,soportetext;
    TextView versiontext;

    RadioButton radioButton3,radioButton2,radioButton1;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        desarrollador = binding.desarrollador;
        desarrollador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowDialogo();

            }
        });
        versiontext = binding.versiontext;
        String versionName = "null";
        int versionCode = -1;
        try {
           PackageInfo packageInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versiontext.setText(versionName);

        compartirluck = binding.compartirluck;
        compartirluck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hola, Innovapp lo(a) invita a descargar " +getResources().getString(R.string.app_name)+", el enlace es: https://enlacedeplay store /pacekage bla bla ");

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.compartir));

                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.compartir)));
            }
        });
        websss = binding.websss;
        websss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri weburi = Uri.parse("https://innovapp-soft.blogspot.com");
                CustomTabsIntent.Builder customtabintent = new CustomTabsIntent.Builder();
               // customtabintent.setToolbarColor(Color.parseColor(colorseting));
                customtabintent.setShowTitle(true);

                String c = "com.android.chrome";
                String f = "org.mozilla.firefox";
                String i = "com.android.broser";


                if(interntExplorerInstaller()){
                    customtabintent.build().intent.setPackage(i);

                }
                if(firefoxInstaller()){
                    customtabintent.build().intent.setPackage(f);

                }


                if(chromeInstaller()){
                    customtabintent.build().intent.setPackage(c);

                }




                customtabintent.build().launchUrl(requireActivity(),weburi);

            }
        });
        soportetext =binding.soportetext;
        String finalVersionName = versionName;
        int finalVersionCode = versionCode;
        soportetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destinatario = "marlonrivero1999@gmail.com";
                String asunto = "Report Bug";
                String SDK = "" + Build.VERSION.SDK_INT + "";

                String Versionandroidnombresistema = "";

                if(SDK.equals("21") || SDK.equals("22")){
                    Versionandroidnombresistema = "Lillipop (5)";
                }else{
                    if(SDK.equals("23")){
                        Versionandroidnombresistema = "Marshmallow (6)";
                    }else{
                        if(SDK.equals("24") || SDK.equals("25")){
                            Versionandroidnombresistema = "Nougat (7)";
                        }else{
                            if(SDK.equals("26") || SDK.equals("27")){
                                Versionandroidnombresistema = "Oreo (8)";
                            }else{
                                if(SDK.equals("28") ){
                                    Versionandroidnombresistema = "Pie (9)";
                                }else{
                                    if(SDK.equals("29") ){
                                        Versionandroidnombresistema = "Q (10)";
                                    }else{
                                        if(SDK.equals("30" )){
                                            Versionandroidnombresistema = "R (11)";
                                        }else{
                                            if(SDK.equals("31") ){
                                                Versionandroidnombresistema = "S (12)";
                                            }else{
                                                if(SDK.equals("32") ){
                                                    Versionandroidnombresistema = "SV2 (12L)";
                                                }else{
                                                    if(SDK.equals("33") ){
                                                        Versionandroidnombresistema = "TIRAMISU (12)";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                String cuerpo = "..Bug: ..\n\n\n\n"+
                        "_________________________\n"+
                        getResources().getString(R.string.app_name) + " V-Name: " + finalVersionName +"\n" +
                        getResources().getString(R.string.app_name) + " V-Code: " + finalVersionCode + "\n"+
                        "SDK: " + SDK + "\n" +
                        "Android: " + Versionandroidnombresistema + "\n" +

                "Modelo: " + Build.MODEL + " (" + Build.PRODUCT + ")\n" +
                "Fabricante: " + Build.MANUFACTURER + "\n" +
                "Compilación: " + Build.DISPLAY;





                Intent correoIntent = new Intent(Intent.ACTION_SENDTO);
                correoIntent.setData(Uri.parse("mailto:" + Uri.encode(destinatario)  +"?subject=" +  Uri.encode(asunto)   +"&body=" +  Uri.encode(cuerpo)));

                correoIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
                correoIntent.putExtra(Intent.EXTRA_TEXT, cuerpo);


                startActivity(correoIntent);


            }
        });


        radioButton1 =binding.radioButton1;
        radioButton2 =binding.radioButton2;
        radioButton3 =binding.radioButton3;

        radioButton1.setChecked(getTema(requireActivity()) == Tema_claro);
        radioButton2.setChecked(getTema(requireActivity()) == Tema_oscuro);
        radioButton3.setChecked(getTema(requireActivity()) == Tema_automatico);

        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    setTema(requireContext(),Tema_claro);
                }
            }
        });
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    setTema(requireContext(),Tema_oscuro);
                }
            }
        });
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    setTema(requireContext(),Tema_automatico);
                }
            }
        });







        return root;
    }
    public boolean chromeInstaller(){
        try{
            requireActivity().getPackageManager().getPackageInfo("com.android.chrome",0);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
    public boolean firefoxInstaller(){
        try{
            requireActivity().getPackageManager().getPackageInfo("org.mozilla.firefox",0);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
    public boolean interntExplorerInstaller(){
        try{
            requireActivity().getPackageManager().getPackageInfo("com.android.broser",0);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    public void ShowDialogo(){

        final Dialog fullscreenDialog = new Dialog(requireActivity(), R.style.DialogFullscreen);
        fullscreenDialog.setContentView(R.layout.dialogodesarrollador);

        TextView atras = fullscreenDialog.findViewById(R.id.atras);

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenDialog.dismiss();
            }
        });


        fullscreenDialog.show();




    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}