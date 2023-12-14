package com.innovapp.luk7;

import static com.innovapp.luk7.Utiles.aplicarTema;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.innovapp.luk7.arreglos.ProduccionArreglo;
import com.innovapp.luk7.db.ProduccionDB;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

public class LPNActivity extends AppCompatActivity {


    TextView atras, title, textViewlpm1, textViewhora1, textViewinfoscaner1, textViewlpm2, textViewhora2, textViewtotal, textViewtiempo, textViewclasificacion, textViewinfoscaner2,textViewinfosranking;
    ProduccionDB produccionDB;
    ImageView imageViewscaner1, imageViewscaner2, imageViewqr;
    ProduccionArreglo produccionArreglo;
    String idd = "";

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aplicarTema(LPNActivity.this);
        setContentView(R.layout.activity_lpnactivity);

        atras = findViewById(R.id.atras);

        title = findViewById(R.id.title);
        textViewlpm1 = findViewById(R.id.textViewlpm1);
        textViewhora1 = findViewById(R.id.textViewhora1);
        textViewinfoscaner1 = findViewById(R.id.textViewinfoscaner1);
        textViewlpm2 = findViewById(R.id.textViewlpm2);
        textViewhora2 = findViewById(R.id.textViewhora2);
        textViewtotal = findViewById(R.id.textViewtotal);
        textViewtiempo = findViewById(R.id.textViewtiempo);
        textViewclasificacion = findViewById(R.id.textViewclasificacion);
        textViewinfoscaner2 = findViewById(R.id.textViewinfoscaner2);
        imageViewscaner1 = findViewById(R.id.imageViewscaner1);
        imageViewscaner2 = findViewById(R.id.imageViewscaner2);
        imageViewqr = findViewById(R.id.imageViewqr);

        textViewinfosranking = findViewById(R.id.textViewinfosranking);
        produccionArreglo = new ProduccionArreglo();

        produccionDB = new ProduccionDB(LPNActivity.this);
        produccionDB.open();
        try {
            idd = Objects.requireNonNull(getIntent().getExtras()).getString("id", "");
            if (!idd.equals("")) {
                if (produccionDB.isOpen()) {
                    produccionArreglo = produccionDB.getProduccion(idd);
                }
            }
        } catch (Exception ignored) {

        }


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        imageViewqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanOptions options = new ScanOptions();
                //  options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
                options.setPrompt(getString(R.string.scan_code));
                options.setCameraId(0);  // Use a specific camera of the device
                options.setBeepEnabled(true);
                options.setBarcodeImageEnabled(true);
                options.setOrientationLocked(false);
                barcodeLauncher.launch(options);
            }
        });
        actualizar();


    }


    @SuppressLint("SetTextI18n")
    public void actualizar() {

        textViewinfosranking.setText(getString(R.string.rankinnoterminado));

        if(  !produccionArreglo.getId().equals("") ){
            title.setText("LPN " + Utiles.getFecha(produccionArreglo.getFechainser()) + " " + Utiles.getHora(produccionArreglo.getFechainser()));
        }

        if ( !produccionArreglo.getId().equals("") && !produccionArreglo.getLpn_inicial().equals("")) {

            textViewhora1.setText(Utiles.getHora(produccionArreglo.getFecha_inical()));
            textViewinfoscaner1.setText(getString(R.string.primerlpnescaneado));
            textViewlpm1.setText(produccionArreglo.getLpn_inicial());


            Utiles.setImageBitmapqr(produccionArreglo.getLpn_inicial(), "CODE_128", imageViewscaner1);

            if (!produccionArreglo.getLpn_fin().equals("")) {
                textViewinfoscaner2.setText(getString(R.string.segundoescaneado));
                textViewlpm2.setText(produccionArreglo.getLpn_fin());
                textViewhora2.setText(Utiles.getHora(produccionArreglo.getFecha_fin()));
                Utiles.setImageBitmapqr(produccionArreglo.getFecha_fin(), "CODE_128", imageViewscaner2);
                imageViewqr.setVisibility(View.INVISIBLE);

                textViewinfosranking.setText(getString(R.string.ratingmostradotext));

                int lnpnumini = Integer.parseInt(produccionArreglo.getLpn_inicial().toUpperCase().replace(" ","").replace("LPNRRHJ",""));
                int lnpnumfin = Integer.parseInt(produccionArreglo.getLpn_fin().toUpperCase().replace(" ","").replace("LPNRRHJ",""));


                textViewtotal.setText(String.valueOf((lnpnumfin - lnpnumini )));
                textViewtiempo.setText(Utiles.obtenerDiferenciaTimestamps(produccionArreglo.getFecha_inical(), produccionArreglo.getFecha_fin()));

            } else {
                textViewinfoscaner2.setText(getString(R.string.escaneesegundolpn));
            }

        } else {

            textViewinfoscaner1.setText(getString(R.string.escaneeprimerlpn));
            textViewinfoscaner2.setText(getString(R.string.escaneeprimerlpn));

        }
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if ( result.getContents() != null) {
                  //  Toast.makeText(LPNActivity.this, result.getContents() , Toast.LENGTH_LONG).show();

                    //  textView.setText( result.getContents() + "\n" + result.getFormatName() );
                    try {
                        String contenido =   result.getContents(); // El contenido que deseas codificar
                        String formatoDeseado =  "";// result.getFormatName(); // Formato deseado (puede ser "QR_CODE", "CODE_128", etc.)

                        if(contenido.toUpperCase().contains("LPNRRHJ")) {

                            if (produccionDB.isOpen()) {

                                if (!produccionArreglo.getId().equals("") && produccionArreglo.getFecha_inical().equals("")) {

                                    produccionArreglo.setFecha_inical(String.valueOf(Utiles.getTimestampActual()));
                                    produccionArreglo.setLpn_inicial(contenido);

                                    produccionDB.updateData(produccionArreglo);


                                    getOnBackPressedDispatcher();

                                } else if (!produccionArreglo.getId().equals("") && !produccionArreglo.getFecha_inical().equals("")) {


                                    produccionArreglo.setFecha_fin(String.valueOf(Utiles.getTimestampActual()));
                                    produccionArreglo.setLpn_fin(contenido);
                                    produccionDB.updateData(produccionArreglo);

                                }


                            } else {
                                //   Toast.makeText(LPNActivity.this,"No abrio db" , Toast.LENGTH_LONG).show();

                            }
                        }else{
                            Toast.makeText(LPNActivity.this,getResources().getString(R.string.noeslpn) , Toast.LENGTH_LONG).show();
                        }

                        actualizar();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                   //  Toast.makeText(LPNActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                }
            });


}