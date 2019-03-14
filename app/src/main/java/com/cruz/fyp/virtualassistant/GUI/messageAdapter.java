package com.cruz.fyp.virtualassistant.GUI;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cruz.fyp.virtualassistant.Message;
import com.cruz.fyp.virtualassistant.R;

import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.PersonViewHolder>{
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;

    @SuppressLint("StaticFieldLeak")

    static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView personName;

        PersonViewHolder(View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.person_name);
        }
    }

    private List<Message> messages;

    public messageAdapter(List<Message> messages){
        this.messages = messages;
    }
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getType() == Message.MessageType.ONE_ITEM) {
            return TYPE_ONE;
        } else if (message.getType() == Message.MessageType.TWO_ITEM) {
            return TYPE_TWO;
        } else {
            return -1;
        }
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_ONE) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
            return new PersonViewHolder(v);
        }
        else if (i == TYPE_TWO) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout2, viewGroup, false);
            return new PersonViewHolder(v);
        }
        else {
            throw new RuntimeException("The type has to be sent or received");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder personViewHolder, int i) {
        switch (personViewHolder.getItemViewType()) {
            case TYPE_ONE:
                initLayoutOne(personViewHolder, i);
                break;
            case TYPE_TWO:
                initLayoutTwo(personViewHolder, i);
                break;
            default:
                break;
        }
    }


    private void initLayoutOne(PersonViewHolder holder, int pos) {
        holder.personName.setText(messages.get(pos).getMsg());
    }

    private void initLayoutTwo(PersonViewHolder holder, int pos) {
        holder.personName.setText(messages.get(pos).getMsg());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }






}