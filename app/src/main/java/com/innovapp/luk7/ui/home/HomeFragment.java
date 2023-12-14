package com.innovapp.luk7.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.google.zxing.BarcodeFormat;
import com.innovapp.luk7.LPNActivity;
import com.innovapp.luk7.MainActivity;
import com.innovapp.luk7.R;
import com.innovapp.luk7.Utiles;
import com.innovapp.luk7.adapter.ProduccionAdapter;
import com.innovapp.luk7.adapter.SwipeToDeleteCallback;
import com.innovapp.luk7.arreglos.ProduccionArreglo;
import com.innovapp.luk7.databinding.FragmentHomeBinding;
import com.innovapp.luk7.db.ProduccionDB;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    RecyclerView recyclerView;

    ProduccionAdapter produccionAdapter;


    LinearLayoutManager linearLayoutManager;
    ProduccionDB produccionDB;
    ImageView imageView;
    TextView textView;

    TextView add;
    TextView edit;

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<ProduccionArreglo> produccionArregloArrayList = new ArrayList<>();

        if (produccionDB.isOpen()) {
            produccionArregloArrayList = produccionDB.getAllProduccion();
        }



        produccionAdapter = new ProduccionAdapter(produccionArregloArrayList, getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(produccionAdapter);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit.getText().equals(getResources().getString(R.string.editar))){
                    produccionAdapter.updatedel2(true);
                    edit.setText(getResources().getString(R.string.done));
                }else{
                    produccionAdapter.updatedel2(false);
                    edit.setText(getResources().getString(R.string.editar));
                }


            }
        });
        produccionAdapter.updatedel2(!edit.getText().equals(getResources().getString(R.string.editar)));


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //    textView = binding.textHome;
        // homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        recyclerView = binding.recicler;

        add = binding.add;

        edit = binding.edit;

        produccionDB = new ProduccionDB(getActivity());
        produccionDB.open();


        // produccionArregloArrayList.add(new ProduccionArreglo());
        //  produccionArregloArrayList.add(new ProduccionArreglo());
        //  produccionArregloArrayList.add(new ProduccionArreglo());


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProduccionArreglo produccionArreglo = new ProduccionArreglo();
                produccionArreglo.setFechainser(String.valueOf(Utiles.getTimestampActual()));
                if(produccionDB.isOpen()){

                    if(produccionDB.addProduccion(produccionArreglo)){

                        produccionArreglo = produccionDB.getProduccionfechainser(produccionArreglo.getFechainser());


                    }
                    produccionAdapter.addIntent(produccionArreglo);
                }


            }
        });


        imageView = binding.imageView;
        Button button = binding.button;

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("LPN RR HJ968", BarcodeFormat.CODE_128, 1200, 300);
            imageView.setImageBitmap(bitmap);
        } catch (Exception ignored) {

        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ScanOptions options = new ScanOptions();
                //  options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
                options.setPrompt("Scan code");
                options.setCameraId(0);  // Use a specific camera of the device
                options.setBeepEnabled(true);
                options.setBarcodeImageEnabled(true);
                options.setOrientationLocked(false);
                barcodeLauncher.launch(options);

            }
        });


        return root;
    }
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    ;
                    Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    //  textView.setText( result.getContents() + "\n" + result.getFormatName() );
                    try {
                        String contenido = result.getContents(); // El contenido que deseas codificar
                        String formatoDeseado = result.getFormatName(); // Formato deseado (puede ser "QR_CODE", "CODE_128", etc.)

                        BarcodeFormat formato = null;
                        int ancho = 400;
                        int alto = 400;

                        switch (formatoDeseado) {
                            case "QR_CODE":
                                formato = BarcodeFormat.QR_CODE;
                                break;
                            case "CODE_128":
                                formato = BarcodeFormat.CODE_128;
                                ancho = 600; // Rectangular
                                alto = 200; // Rectangular
                                break;
                            case "CODABAR":
                                formato = BarcodeFormat.CODABAR;
                                ancho = 400; // Cuadrado
                                alto = 100; // Rectangular
                                break;
                            case "CODE_39":
                                formato = BarcodeFormat.CODE_39;
                                ancho = 600; // Rectangular
                                alto = 200; // Rectangular
                                break;
                            case "DATA_MATRIX":
                                formato = BarcodeFormat.DATA_MATRIX;
                                ancho = 400; // Cuadrado
                                alto = 400; // Cuadrado
                                break;
                            case "EAN_8":
                                formato = BarcodeFormat.EAN_8;
                                ancho = 400; // Cuadrado
                                alto = 200; // Rectangular
                                break;
                            case "EAN_13":
                                formato = BarcodeFormat.EAN_13;
                                ancho = 400; // Cuadrado
                                alto = 200; // Rectangular
                                break;
                            case "ITF":
                                formato = BarcodeFormat.ITF;
                                ancho = 600; // Rectangular
                                alto = 200; // Rectangular
                                break;
                            case "MAXICODE":
                                formato = BarcodeFormat.MAXICODE;
                                ancho = 400; // Cuadrado
                                alto = 400; // Cuadrado
                                break;
                            case "PDF_417":
                                formato = BarcodeFormat.PDF_417;
                                ancho = 600; // Rectangular
                                alto = 200; // Rectangular
                                break;
                            case "RSS_14":
                                formato = BarcodeFormat.RSS_14;
                                ancho = 400; // Cuadrado
                                alto = 400; // Cuadrado
                                break;
                            case "RSS_EXPANDED":
                                formato = BarcodeFormat.RSS_EXPANDED;
                                ancho = 400; // Cuadrado
                                alto = 400; // Cuadrado
                                break;
                            case "UPC_A":
                                formato = BarcodeFormat.UPC_A;
                                ancho = 400; // Cuadrado
                                alto = 400; // Cuadrado
                                break;
                            case "UPC_E":
                                formato = BarcodeFormat.UPC_E;
                                ancho = 400; // Cuadrado
                                alto = 400; // Cuadrado
                                break;
                            case "UPC_EAN_EXTENSION":
                                formato = BarcodeFormat.UPC_EAN_EXTENSION;
                                ancho = 400; // Cuadrado
                                alto = 400; // Cuadrado
                                break;
                            default:
                                formato = BarcodeFormat.QR_CODE; // Establece un valor predeterminado si el formato no se reconoce
                                break;
                        }

                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.encodeBitmap(contenido, formato, ancho, alto);
                        imageView.setImageBitmap(bitmap);
                    } catch (Exception e) {

                    }

                }
            });


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}