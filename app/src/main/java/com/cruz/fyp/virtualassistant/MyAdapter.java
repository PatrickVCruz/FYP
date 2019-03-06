package com.cruz.fyp.virtualassistant;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PersonViewHolder>{
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

    private List<Person> persons;

    MyAdapter(List<Person> persons){
        this.persons = persons;
    }
    @Override
    public int getItemViewType(int position) {
        Person person = persons.get(position);
        if (person.getType() == Person.PersonType.ONE_ITEM) {
            return TYPE_ONE;
        } else if (person.getType() == Person.PersonType.TWO_ITEM) {
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
            throw new RuntimeException("The type has to be ONE or TWO");
        }
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
//        personViewHolder.personName.setText(persons.get(i).name);
        switch (personViewHolder.getItemViewType()) {
            case TYPE_ONE:
                initLayoutOne((PersonViewHolder)personViewHolder, i);
                break;
            case TYPE_TWO:
                initLayoutTwo((PersonViewHolder) personViewHolder, i);
                break;
            default:
                break;
        }
    }


    private void initLayoutOne(PersonViewHolder holder, int pos) {
        holder.personName.setText(persons.get(pos).getName());
    }

    private void initLayoutTwo(PersonViewHolder holder, int pos) {
        holder.personName.setText(persons.get(pos).getName());
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }






}