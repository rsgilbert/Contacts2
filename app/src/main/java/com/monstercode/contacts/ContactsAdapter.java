package com.monstercode.contacts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private Context context;
    private List<Contact> contactsList;
    private List<Contact> contactsListCopy = new ArrayList<>();
    public ContactsAdapter(Context context, List<Contact> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
        this.contactsListCopy.addAll(contactsList);
    }
    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_contacts, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Contact contact = contactsList.get(position);
        holder.textViewFirstName.setText(contact.getFirstname());
        holder.textViewLastName.setText(contact.getLastname());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }


    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewFirstName, textViewLastName;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            textViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            textViewLastName = itemView.findViewById(R.id.textViewLastName);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Contact contact = contactsList.get(getAdapterPosition());
            Intent intent = new Intent(context, AddContactActivity.class);
            context.startActivity(intent);

        }
    }

    // filter, for redisplaying page while querying
    public void searchFor(String searchWord) {
        this.contactsList.clear();
        if(searchWord.isEmpty()){
            this.contactsList.addAll(contactsListCopy);
        } else {
            searchWord = searchWord.toLowerCase();
            for (Contact contact: contactsListCopy) {
                if(contact.getFirstname().toLowerCase().contains(searchWord)
                        || contact.getLastname().toLowerCase().contains(searchWord)
                        || contact.getDesignation().toLowerCase().contains(searchWord)
                        || contact.getTel().toLowerCase().contains(searchWord)) {
                    this.contactsList.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }


}

