package com.kehel.equiclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    private SharedPreferences sharedpreferences;
    private TextView nom_value,email_value,tel_value;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_account,null);

        sharedpreferences =getActivity().getSharedPreferences("UserInfos", Context.MODE_PRIVATE);

        //Initializing fields
        nom_value=v.findViewById(R.id.nom_value);
        email_value=v.findViewById(R.id.email_value);
        tel_value=v.findViewById(R.id.tel_value);

        nom_value.setText(sharedpreferences.getString("nom","")+ " "+sharedpreferences.getString("prenom",""));
        email_value.setText(sharedpreferences.getString("email",""));
        tel_value.setText(String.valueOf(sharedpreferences.getInt("tel",0)));

        return v;
    }
}
