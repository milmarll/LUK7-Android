package com.innovapp.luk7.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.snackbar.Snackbar;
import com.innovapp.luk7.LPNActivity;
import com.innovapp.luk7.R;
import com.innovapp.luk7.Utiles;
import com.innovapp.luk7.arreglos.ProduccionArreglo;
import com.innovapp.luk7.db.ProduccionDB;

import java.util.ArrayList;
import java.util.List;

public class ProduccionAdapter extends RecyclerView.Adapter<ProduccionAdapter.MetaViewHolder>
        implements ItemClickListener, ItemLongClickListener, Filterable {


    private List<ProduccionArreglo> items;
    private List<ProduccionArreglo> listSelected;

    private Context context;
    private ProgressDialog progressDialog;


    public ProduccionAdapter(List<ProduccionArreglo> items, Context context) {
        this.context = context;
        this.items = items;
        listSelected = new ArrayList<>();
        listSelected.addAll(items);
    }


    @Override
    public int getItemCount() {
        return listSelected.size();
    }

    public void addIntent(ProduccionArreglo produccionArreglo) {
        listSelected.add(produccionArreglo);
        notifyItemInserted(getItemCount() - 1);

    }
    public void addIntent(ProduccionArreglo produccionArreglo, int posi) {
        listSelected.add(posi,produccionArreglo );

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
                .inflate(R.layout.adapterproduccion, viewGroup, false);
        return new MetaViewHolder(v, this, this);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull MetaViewHolder viewHolder, int i) {

        final ProduccionArreglo produccionArreglo = listSelected.get(i);

        viewHolder.text.setText(Utiles.getFecha(produccionArreglo.getFechainser()) + " " + Utiles.getHora(produccionArreglo.getFechainser()));


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
                            context.startActivity(new Intent(context, LPNActivity.class).putExtra("id", listSelected.get( viewHolder.getAdapterPosition()).getId()));
                        } else {
                            // Esto es un deslizamiento, no hagas nada o maneja el deslizamiento aquí
                        }
                        return true;
                }
                return false;
            }
        });



        if(produccionArreglo.isDel2()){
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


    @Override
    public void onItemClick(View view, int position) {

        context.startActivity(new Intent(context, LPNActivity.class).putExtra("id", listSelected.get(position).getId()));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }



    private void mostrarSnackbarConContador(View view , int posision) {

        final ProduccionArreglo produccionArreglo = listSelected.get(posision);


        final Snackbar snackbar = Snackbar.make(view, context.getResources().getString(R.string.eliminarcontadorr)  + 4 + " " + context.getResources().getString(R.string.segundos) , Snackbar.LENGTH_INDEFINITE);


        delItem(posision);

        snackbar.setAction(context.getResources().getString(R.string.cancelar), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                addIntent(produccionArreglo,posision);

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
                ProduccionDB produccionDB = new ProduccionDB(context);
                produccionDB.open();
                if(produccionDB.isOpen()){
                    produccionDB.delete(produccionArreglo.getId());
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
                List<ProduccionArreglo> listResult = new ArrayList<>();
                if (query == null)
                    listResult.addAll(items);
                else {
                    query = query.toString().toLowerCase();
                    for (ProduccionArreglo generosArreglo : items)
                        //condicion xx
                        listResult.add(generosArreglo);
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
                    listSelected.addAll((List<ProduccionArreglo>) result.values);

                notifyDataSetChanged();

            }
        };
    }


    public static class MetaViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        // Campos respectivos de un item
        public TextView text;
        SwipeLayout swipeLayout;
        ItemClickListener listener;
        ItemLongClickListener listenerlong;
        LinearLayout eliminarlayout;

        ImageView del2;
        LinearLayout linear;

        public MetaViewHolder(View v, ItemClickListener listener, ItemLongClickListener listenerlong) {
            super(v);
            text = (TextView) v.findViewById(R.id.text);
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


interface ItemClickListener {
    void onItemClick(View view, int position);
}

interface ItemLongClickListener {
    void onItemLongClick(View view, int position);
}