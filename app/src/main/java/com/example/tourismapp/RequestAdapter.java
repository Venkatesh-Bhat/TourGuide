package com.example.tourismapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Request> RequestList;
    String mUID;

    public RequestAdapter(Context context, ArrayList<Request> requestList, String mUID) {
        this.context = context;
        RequestList = requestList;
        this.mUID = mUID;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card,parent,false);
        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
            holder.name.setText(RequestList.get(position).getHotelName());
            holder.location.setText(RequestList.get(position).getLocation());
            holder.status.setText("Status: "+RequestList.get(position).getStatus());


            
            holder.delete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure? Your request will be permanently deleted!!");
                builder.setPositiveButton("No", (dialog, which) -> {

                });
                builder.setNegativeButton("Yes",((dialog, which) -> {
                    DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference().child("Request").child(mUID).child(RequestList.get(position).getHotelName());
                    reqRef.removeValue();
                    Toast.makeText(context, "Permanently Deleted!!!", Toast.LENGTH_SHORT).show();
                }));
                AlertDialog dialog = builder.create();
                dialog.show();
            });
    }

    @Override
    public int getItemCount() {
        return RequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView location;
        TextView status;
        NeumorphCardView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.hotelName);
            location = itemView.findViewById(R.id.location);
            status = itemView.findViewById(R.id.status);
            delete = itemView.findViewById(R.id.delete);
        }
        
    }
}
