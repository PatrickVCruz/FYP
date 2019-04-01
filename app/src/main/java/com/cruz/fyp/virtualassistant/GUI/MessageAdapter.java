package com.cruz.fyp.virtualassistant.GUI;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cruz.fyp.virtualassistant.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.PersonViewHolder>{
    private static final int SENT = 1;
    private static final int RECEIVED = 2;

    @SuppressLint("StaticFieldLeak")

    static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView messageBubble;

        PersonViewHolder(View itemView) {
            super(itemView);
            messageBubble = itemView.findViewById(R.id.messageBubble);
        }
    }

    private List<Message> messages;

    public MessageAdapter(List<Message> messages){
        this.messages = messages;
    }
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getType() == Message.MessageType.SENT) {
            return SENT;
        } else if (message.getType() == Message.MessageType.RECEIVED) {
            return RECEIVED;
        } else {
            return -1;
        }
    }



    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == SENT) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout_sent, viewGroup, false);
            return new PersonViewHolder(v);
        }
        else if (i == RECEIVED) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout_receive, viewGroup, false);
            return new PersonViewHolder(v);
        }
        else {
            throw new RuntimeException("The type has to be sent or received");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder personViewHolder, int i) {
        switch (personViewHolder.getItemViewType()) {
            case SENT:
                initLayoutOne(personViewHolder, i);
                break;
            case RECEIVED:
                initLayoutTwo(personViewHolder, i);
                break;
            default:
                break;
        }
    }


    private void initLayoutOne(PersonViewHolder holder, int pos) {
        holder.messageBubble.setText(messages.get(pos).getMsg());
    }

    private void initLayoutTwo(PersonViewHolder holder, int pos) {
        holder.messageBubble.setText(messages.get(pos).getMsg());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }






}