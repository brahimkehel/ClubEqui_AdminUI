package com.kehel.equiclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class SeanceFragment extends Fragment {
    CardView card_view_1,card_view_2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_seance,null);

        card_view_1=v.findViewById(R.id.card_view_1);
        card_view_2=v.findViewById(R.id.card_view_2);

        card_view_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),Seance_per_week.class);
                getActivity().startActivity(i);
            }
        });

        card_view_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),Seance_per_date.class);
                getActivity().startActivity(i);
            }
        });

        return  v;
    }
}
