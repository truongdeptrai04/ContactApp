package com.example.contactapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private ArrayList<Contact> contactList;

    public ContactsAdapter(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(contactList.get(position).getName());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Contact> newList) {
        contactList = newList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public ViewHolder(View view){
            super(view);

            tvName = (TextView)view.findViewById(R.id.iv_name);
        }
    }
}
