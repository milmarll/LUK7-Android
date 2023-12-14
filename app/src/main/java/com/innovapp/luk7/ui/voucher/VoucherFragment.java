package com.innovapp.luk7.ui.voucher;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.innovapp.luk7.LPNActivity;
import com.innovapp.luk7.R;
import com.innovapp.luk7.Utiles;
import com.innovapp.luk7.databinding.FragmentVoucherBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class VoucherFragment extends Fragment {

    private FragmentVoucherBinding binding;
    ImageView imageViewqr, imagenviewqrgenerado,imageViewcesto;
    EditText nombre;
    TextView id;
    SharedPreferences sharedPreferences ;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences =  requireActivity().getSharedPreferences("vauncher", MODE_PRIVATE);

        binding = FragmentVoucherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        id = binding.id;

        imagenviewqrgenerado = binding.imagenviewqrgenerado;

        if(!sharedPreferences.getString("qr","").equals("")){
            Utiles.setImageBitmapqr(sharedPreferences.getString("qr",""), "CODE_128", imagenviewqrgenerado);

            id.setText("ID: " + sharedPreferences.getString("qr","").equals(""));
        }

        imageViewqr = binding.imageViewqr;


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

        nombre = binding.nombre;
        nombre.setText(sharedPreferences.getString("nombre","---"));
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sharedPreferences.edit().putString("nombre",charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        imageViewcesto = binding.imageViewcesto;
        imageViewcesto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("qr","" ).apply();
                sharedPreferences.edit().putString("nombre","---" ).apply();
                nombre.setText("---");
                id.setText("ID: ");
                imagenviewqrgenerado.setImageResource(R.drawable.baseline_qr_code_24);


            }
        });
        return root;
    }


    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if ( result.getContents() != null) {

                    try {
                        String contenido =   result.getContents(); // El contenido que deseas codificar
                        String formatoDeseado =  "";// result.getFormatName(); // Formato deseado (puede ser "QR_CODE", "CODE_128", etc.)
                        sharedPreferences.edit().putString("qr",contenido ).apply();

                        Utiles.setImageBitmapqr(contenido, "CODE_128", imagenviewqrgenerado);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //  Toast.makeText(LPNActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                }
            });
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}