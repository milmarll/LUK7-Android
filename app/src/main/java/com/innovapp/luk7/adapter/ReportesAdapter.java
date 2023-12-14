package com.innovapp.luk7.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.snackbar.Snackbar;
import com.innovapp.luk7.LPNActivity;
import com.innovapp.luk7.R;
import com.innovapp.luk7.Utiles;
import com.innovapp.luk7.arreglos.ReportesArreglo;
import com.innovapp.luk7.db.ReportesDB;

import java.util.ArrayList;
import java.util.List;

public class ReportesAdapter extends RecyclerView.Adapter<ReportesAdapter.MetaViewHolder>
        implements ItemClickListener1, ItemLongClickListener1, Filterable {


    private List<ReportesArreglo> items;
    private List<ReportesArreglo> listSelected;

    private Context context;
    private ProgressDialog progressDialog;


    public ReportesAdapter(List<ReportesArreglo> items, Context context) {
        this.context = context;
        this.items = items;
        listSelected = new ArrayList<>();
        listSelected.addAll(items);
    }


    @Override
    public int getItemCount() {
        return listSelected.size();
    }

    public void addIntent(ReportesArreglo reportesArreglo) {
        listSelected.add(reportesArreglo);
        notifyItemInserted(getItemCount() - 1);

    }
    public void addIntent(ReportesArreglo reportesArreglo, int posi) {
        listSelected.add(posi,reportesArreglo );

        notifyItemInserted(posi);

    }
    public void updatedel2(boolean is) {

        for (int i = 0 ; i < listSelected.size(); i++){
            listSelected.get(i).setDel2(is);
            notifyItemChanged(i);

        }


    }

    public void delItem(int position) {

        listSelected.remove(position);
        notifyItemRemoved(position);


    }

    @NonNull
    @Override
    public MetaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adaptermisreportes, viewGroup, false);
        return new MetaViewHolder(v, this, this);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull MetaViewHolder viewHolder, int i) {

        final ReportesArreglo reportesArreglo = listSelected.get(i);

        viewHolder.fecha.setText(Utiles.getFecha(reportesArreglo.getFecha()) + "\n" + Utiles.getHora(reportesArreglo.getFecha()));
        viewHolder.titulo.setText(reportesArreglo.getTitulo());
        viewHolder.descripcion.setText(reportesArreglo.getDescripcion());
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        viewHolder.eliminarlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSnackbarConContador(viewHolder.eliminarlayout , viewHolder.getAdapterPosition());
            }
        });


        viewHolder.swipeLayout.setSwipeEnabled(true);

        viewHolder.swipeLayout.setOnTouchListener(new View.OnTouchListener() {

            float startX;
            float endX;
            final float MIN_DISTANCE = 20; // Puedes ajustar esta distancia según tus necesidades

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        @SuppressLint("ClickableViewAccessibility") float deltaX = endX - startX;

                        if (Math.abs(deltaX) < MIN_DISTANCE) {
                         ///   context.startActivity(new Intent(context, LPNActivity.class).putExtra("id", listSelected.get( viewHolder.getAdapterPosition()).getId()));


                            ShowDialogo( listSelected.get( viewHolder.getAdapterPosition()) ,  viewHolder.getAdapterPosition());
                        } else {
                            // Esto es un deslizamiento, no hagas nada o maneja el deslizamiento aquí
                        }
                        return true;
                }
                return false;
            }
        });



        if(reportesArreglo.isDel2()){
            viewHolder.del2.setVisibility(View.VISIBLE);
        }else{
            viewHolder.del2.setVisibility(View.GONE);
        }

        viewHolder.del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarSnackbarConContador(viewHolder.del2 , viewHolder.getAdapterPosition());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void ShowDialogo(ReportesArreglo reportesArreglo ,int posicion){

        ReportesDB reportesDB = new ReportesDB(context);
        reportesDB.open();
        final Dialog fullscreenDialog = new Dialog(context, R.style.DialogFullscreen);
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

                    tituloedit.setError(context.getResources().getString(R.string.rellene));
                    return;
                }
                if(descipcionedit.getText().toString().equals("")){
                    descipcionedit.setError(context.getResources().getString(R.string.rellene));
                    return;
                }

                reportesArreglo2.setTitulo(tituloedit.getText().toString());
                reportesArreglo2.setDescripcion(descipcionedit.getText().toString());



                if(reportesDB.isOpen()){

                    if(reportesArreglo2.getId().equals("")) {

                        if (reportesDB.addReportes(reportesArreglo2)) {

                            reportesArreglo2 = reportesDB.getReportesfechatitulo(reportesArreglo2.getFecha(), reportesArreglo2.getTitulo());

                            addIntent(reportesArreglo2);
                        }
                    }else {
                        if (reportesDB.updateData(reportesArreglo2)) {

                            reportesArreglo2 = reportesDB.getReportesfechatitulo(reportesArreglo2.getFecha(), reportesArreglo2.getTitulo());
                            delItem(posicion);
                            addIntent(reportesArreglo2,posicion);

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
    public void onItemClick(View view, int position) {

        ///context.startActivity(new Intent(context, LPNActivity.class).putExtra("id", listSelected.get(position).getId()));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }



    private void mostrarSnackbarConContador(View view , int posision) {

        final ReportesArreglo reportesArreglo = listSelected.get(posision);


        final Snackbar snackbar = Snackbar.make(view, context.getResources().getString(R.string.eliminarcontadorr)  + 4 + " " + context.getResources().getString(R.string.segundos) , Snackbar.LENGTH_INDEFINITE);


        delItem(posision);

        snackbar.setAction(context.getResources().getString(R.string.cancelar), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                addIntent(reportesArreglo,posision);

            }
        });

        // Inicia el temporizador de 4 segundos
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                snackbar.setText(context.getResources().getString(R.string.eliminarcontadorr)   + millisUntilFinished / 1000 + " " + context.getResources().getString(R.string.segundos) );
            }

            @Override
            public void onFinish() {
                ReportesDB reportesDB = new ReportesDB(context);
                reportesDB.open();
                if(reportesDB.isOpen()){
                    reportesDB.delete(reportesArreglo.getId());
                }

                snackbar.dismiss();

            }
        }.start();

        snackbar.show();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence query) {
                List<ReportesArreglo> listResult = new ArrayList<>();
                if (query == null)
                    listResult.addAll(items);
                else {
                    query = query.toString().toLowerCase();
                    for (ReportesArreglo generosArreglo : items)
                        //condicion xx
                        if(generosArreglo.getTitulo().toLowerCase().contains(query) || generosArreglo.getDescripcion().toLowerCase().contains(query) ) {
                            listResult.add(generosArreglo);
                        }
                }

                FilterResults result = new FilterResults();
                result.values = listResult;
                result.count = listResult.size();
                return result;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence query, FilterResults result) {
                listSelected.clear();
                if (result == null)
                    listSelected.addAll(items);
                else
                    listSelected.addAll((List<ReportesArreglo>) result.values);

                notifyDataSetChanged();

            }
        };
    }


    public static class MetaViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        // Campos respectivos de un item
        public TextView titulo;
        public TextView descripcion;
        public TextView fecha;
        SwipeLayout swipeLayout;
        ItemClickListener1 listener;
        ItemLongClickListener1 listenerlong;
        LinearLayout eliminarlayout;

        ImageView del2;
        LinearLayout linear;

        public MetaViewHolder(View v, ItemClickListener1 listener, ItemLongClickListener1 listenerlong) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
            fecha = (TextView) v.findViewById(R.id.fecha);

            swipeLayout = v.findViewById(R.id.swipe);
            eliminarlayout = v.findViewById(R.id.eliminarlayout);
            del2 = v.findViewById(R.id.del2);
            linear  = v.findViewById(R.id.linear);
            this.listener = listener;
            this.listenerlong = listenerlong;

            //v.setOnLongClickListener(this);
            //linear.setOnClickListener(this);
           // swipeLayout.setOnClickListener(this);
            //v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listenerlong.onItemLongClick(v, getAdapterPosition());
            return false;
        }
    }
}


interface ItemClickListener1 {
    void onItemClick(View view, int position);
}

interface ItemLongClickListener1 {
    void onItemLongClick(View view, int position);
}