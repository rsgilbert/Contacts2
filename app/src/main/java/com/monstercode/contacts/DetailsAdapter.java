package com.monstercode.contacts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monstercode.contacts.Detail;

import java.util.ArrayList;
import java.util.List;


public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.detailsViewHolder> {
    private Context context;
    private List<Detail> detailsList;
    private List<Detail> detailsListCopy = new ArrayList<>();
    public DetailsAdapter(Context context, List<Detail> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        this.detailsListCopy.addAll(detailsList);
    }
    @NonNull
    @Override
    public detailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_contacts, parent, false);
        return new detailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull detailsViewHolder holder, int position) {
        Detail detail = detailsList.get(position);
        holder.textViewName.setText(detail.getName());
        holder.textViewJob.setText(detail.getJob() +", " + detail.getSitename());
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }


    class detailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName, textViewJob;

        public detailsViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewJob = itemView.findViewById(R.id.textViewJob);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Detail detail = detailsList.get(getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);

        }
    }

    // filter, for redisplaying page while querying
    public void searchFor(String searchWord) {
        this.detailsList.clear();
        if(searchWord.isEmpty()){
            this.detailsList.addAll(detailsListCopy);
        } else {
            searchWord = searchWord.toLowerCase();
            for (Detail detail: detailsListCopy) {
                if(detail.getName().toLowerCase().contains(searchWord)
                        || detail.getJob().toLowerCase().contains(searchWord)
                        || detail.getTel1().toLowerCase().contains(searchWord)
                        || detail.getTel2().toLowerCase().contains(searchWord)) {
                    this.detailsList.add(detail);
                }
            }
        }
        notifyDataSetChanged();
    }


}

