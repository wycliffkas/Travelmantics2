package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TravelDealAdapter extends RecyclerView.Adapter<TravelDealAdapter.DealViewHolder> {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    ArrayList<TravelDeal> deals;
    
    public TravelDealAdapter(){
//        FirebaseUtil.openReference("traveldeals", this);
        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;
        
        deals = FirebaseUtil.deals;
        
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size()-1);
            
            }
        
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            
            }
        
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            
            }
        
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            
            }
        
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }
    
    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new DealViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
      TravelDeal deal = deals.get(position);
      holder.title.setText(deal.getTitle());
      holder.price.setText(deal.getPrice());
      holder.description.setText(deal.getDescription());
    }
    
    @Override
    public int getItemCount() {
        return deals.size();
    }
    
    
    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        TextView price;
    
        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.dealTitle);
            description = itemView.findViewById(R.id.dealdescription);
            price = itemView.findViewById(R.id.dealPrice);
            itemView.setOnClickListener(this);
        }
    
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("Deal", selectedDeal);
            view.getContext().startActivity(intent);
            
        }
    }

}
