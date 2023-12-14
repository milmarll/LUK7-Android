package com.innovapp.luk7.ui.reports;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.innovapp.luk7.R;
import com.innovapp.luk7.Utiles;
import com.innovapp.luk7.adapter.ReportesAdapter;
import com.innovapp.luk7.arreglos.ReportesArreglo;
import com.innovapp.luk7.databinding.FragmentReportsBinding;
import com.innovapp.luk7.db.ReportesDB;

import java.util.ArrayList;
import java.util.Objects;


public class ReportsFragment extends Fragment {
    RecyclerView recyclerView;

    ReportesAdapter reportesAdapter;


    LinearLayoutManager linearLayoutManager;
    ReportesDB reportesDB;
    ImageView imageView;
    TextView textView;

    TextView add;
    TextView edit;

    SearchView search;
    private FragmentReportsBinding binding;


    @Override
    public void onResume() {
        super.onResume();
        ArrayList<ReportesArreglo> reportesArregloArrayList = new ArrayList<>();

        if (reportesDB.isOpen()) {
            reportesArregloArrayList = reportesDB.getAllReportes();
        }



        reportesAdapter = new ReportesAdapter(reportesArregloArrayList, getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(reportesAdapter);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit.getText().equals(getResources().getString(R.string.editar))){
                    reportesAdapter.updatedel2(true);
                    edit.setText(getResources().getString(R.string.done));
                }else{
                    reportesAdapter.updatedel2(false);
                    edit.setText(getResources().getString(R.string.editar));
                }


            }
        });
        reportesAdapter.updatedel2(!edit.getText().equals(getResources().getString(R.string.editar)));




    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


     //   notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        recyclerView = binding.recicler;

        add = binding.add;

        edit = binding.edit;

        search = binding.search;

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(reportesAdapter != null){
                    reportesAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(reportesAdapter != null){
                    reportesAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        reportesDB = new ReportesDB(getActivity());
        reportesDB.open();


        // reportesArregloArrayList.add(new ReportesArreglo());
        //  reportesArregloArrayList.add(new ReportesArreglo());
        //  reportesArregloArrayList.add(new ReportesArreglo());


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   ShowDialogo(null, -1);

            }
        });


        return root;
    }
    @SuppressLint("SetTextI18n")
    public void ShowDialogo(ReportesArreglo reportesArreglo ,int posicion){

        final Dialog fullscreenDialog = new Dialog(requireActivity(), R.style.DialogFullscreen);
        fullscreenDialog.setContentView(R.layout.dialogoreportesadd);

        ImageView cancelar = fullscreenDialog.findViewById(R.id.cancelar);
        ImageView ok = fullscreenDialog.findViewById(R.id.ok);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText tituloedit = fullscreenDialog.findViewById(R.id.titulo);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText descipcionedit = fullscreenDialog.findViewById(R.id.descripcion);

        TextView info =  fullscreenDialog.findViewById(R.id.info);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReportesArreglo reportesArreglo2 = new ReportesArreglo();
                reportesArreglo2.setFecha(String.valueOf(Utiles.getTimestampActual()));

                if (reportesArreglo != null){
                    reportesArreglo2 = reportesArreglo;
                 }

                if(tituloedit.getText().toString().equals("")){

                    tituloedit.setError(getResources().getString(R.string.rellene));
                    return;
                }
                if(descipcionedit.getText().toString().equals("")){
                    descipcionedit.setError(getResources().getString(R.string.rellene));
                    return;
                }

                reportesArreglo2.setTitulo(tituloedit.getText().toString());
                reportesArreglo2.setDescripcion(descipcionedit.getText().toString());



                if(reportesDB.isOpen()){

                    if(reportesArreglo2.getId().equals("")) {

                        if (reportesDB.addReportes(reportesArreglo2)) {

                            reportesArreglo2 = reportesDB.getReportesfechatitulo(reportesArreglo2.getFecha(), reportesArreglo2.getTitulo());

                            reportesAdapter.addIntent(reportesArreglo2);
                        }
                    }else {
                        if (reportesDB.updateData(reportesArreglo2)) {

                            reportesArreglo2 = reportesDB.getReportesfechatitulo(reportesArreglo2.getFecha(), reportesArreglo2.getTitulo());
                            reportesAdapter.delItem(posicion);
                            reportesAdapter.addIntent(reportesArreglo2,posicion);

                        }
                    }

                }


                fullscreenDialog.dismiss();

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenDialog.dismiss();
            }
        });

        if(reportesArreglo != null){

            info.setText(Utiles.getFecha(reportesArreglo.getFecha()) + " " + Utiles.getHora(reportesArreglo.getFecha()));
            tituloedit.setText(reportesArreglo.getTitulo());
            descipcionedit.setText(reportesArreglo.getDescripcion());


        }


        fullscreenDialog.show();




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}