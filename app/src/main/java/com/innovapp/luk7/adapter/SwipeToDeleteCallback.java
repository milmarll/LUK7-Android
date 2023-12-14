package com.innovapp.luk7.adapter;

import android.graphics.Canvas;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.innovapp.luk7.R;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
   private ProduccionAdapter mAdapter;

   public SwipeToDeleteCallback(ProduccionAdapter adapter) {
      super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
      mAdapter = adapter;
   }

   @Override
   public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      return false; // No queremos manejar movimiento
   }

   @Override
   public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
      int position = viewHolder.getAdapterPosition();
      mAdapter.delItem(position); //
   }

   @Override
   public void onChildDraw(Canvas c, @NonNull RecyclerView recyclerView,
                           RecyclerView.ViewHolder viewHolder,
                           float dX, float dY,
                           int actionState, boolean isCurrentlyActive) {
      final View itemView = viewHolder.itemView;
      final Button deleteButton = itemView.findViewById(R.id.delete_button);
      final float buttonWidth = deleteButton.getWidth();

      if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
         // Asegurarse de que el deslizamiento solo sea hacia la izquierda
         if (dX < 0) { // Deslizar hacia la izquierda
            // Mostrar el botón de "Eliminar" si el ítem ha sido deslizado lo suficiente
            deleteButton.setVisibility(View.VISIBLE);

            // Limitar el deslizamiento a la anchura del botón de "Eliminar"
            float newDX = Math.max(dX, -buttonWidth);
            itemView.setTranslationX(newDX);
         } else {
            // Si se desliza hacia la derecha, no hacemos nada
            deleteButton.setVisibility(View.GONE);
            itemView.setTranslationX(0);
         }
      }

      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
   }
}