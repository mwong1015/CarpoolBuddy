package com.example.carpoolbuddy;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class vehicleRecyclerViewAdapter extends RecyclerView.Adapter<vehicleViewHolder> {

    ArrayList<Vehicle> vehicles;
    private Context mContext;
    String ownerString;

    public vehicleRecyclerViewAdapter(ArrayList<Vehicle> vehicles, Context mContext, String ownerString) {
        this.vehicles = vehicles;
        this.mContext = mContext;
        this.ownerString = ownerString;
    }

    @NonNull
    @Override
    public vehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_vehicle_row_view, parent, false);
        vehicleViewHolder holder = new vehicleViewHolder(myView);
        mContext = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull vehicleViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.ownerText.setText("User: " + vehicles.get(position).getOwner());
        holder.capacityText.setText(vehicles.get(position).getCapacity() + " seats Left");
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, vehicleProfileActivity.class);
                intent.putExtra("Vehicle", (Serializable) vehicles.get(position));
                intent.putExtra("owner", ownerString);
                mContext.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }
}
