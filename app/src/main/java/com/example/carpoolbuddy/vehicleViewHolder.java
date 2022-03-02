package com.example.carpoolbuddy;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class vehicleViewHolder extends RecyclerView.ViewHolder {

    protected TextView ownerText;
    protected TextView capacityText;
    ConstraintLayout parentLayout;
    Context mContext;

    public vehicleViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ownerText = itemView.findViewById(R.id.ownerTextView);
        capacityText = itemView.findViewById(R.id.capacityTextView);
        parentLayout = itemView.findViewById(R.id.constraintLayout);
    }
}

