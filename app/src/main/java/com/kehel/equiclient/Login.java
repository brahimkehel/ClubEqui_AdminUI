package com.kehel.equiclient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Login extends AppCompatActivity {
    private TextInputLayout email,motdepasse;
    private Button btn;
    private ProgressBar progressBar;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inisializing feilds
        email=findViewById(R.id.email);
        motdepasse=findViewById(R.id.motdepasse);
        progressBar=findViewById(R.id.progressBar);
        btn=findViewById(R.id.button);

        progressBar.setVisibility(View.GONE);
        btn.setOnClickListener(v -> confirmValidation(v));

        sharedpreferences = getSharedPreferences("UserInfos", Context.MODE_PRIVATE);
        if(sharedpreferences.getString("nom",null)!=null)
        {
            Intent mainIntent = new Intent(Login.this,MainActivity.class);
            Login.this.startActivity(mainIntent);
            Login.this.finish();
        }
    }

    private boolean validateEmail()
    {
        String emailInput=email.getEditText().getText().toString().trim();
        if(emailInput.isEmpty())
        {
            email.setError("Il faut remplir le champ");
            return false;
        }
        else
        {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword()
    {
        String passwordInput=motdepasse.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty())
        {
            motdepasse.setError("Il faut remplir le champ");
            return false;
        }
        else
        {
            motdepasse.setError(null);
            motdepasse.setErrorEnabled(false);
            return true;
        }
    }

    private void confirmValidation(View v) {
        if(!validateEmail() | !validatePassword()){
            return;
        }

        try {
            progressBar.setVisibility(View.VISIBLE);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email.getEditText().getText().toString().trim());
            jsonBody.put("motPasse", motdepasse.getEditText().getText().toString());
            JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST, WebService.url+"Clients/SeConnecter",jsonBody,
                    new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(MainActivity.class.getSimpleName(),"json reçu");
                            try {
                                JSONArray jsonArray=response.getJSONArray("clients");
                                Log.d("Working", "onResponse: "+response);

                                Client client=new Client();
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    client.setIdClient(jsonObject.getInt("idClient"));
                                    client.setNom(jsonObject.getString("nom"));
                                    client.setPrenom(jsonObject.getString("prenom"));
                                    client.setDateNais(LocalDateTime.parse(jsonObject.getString("dateNais")));
                                    client.setPhoto(jsonObject.getString("photo"));
                                    client.setIdentityNum(jsonObject.getString("identityNum"));
                                    client.setDateInscription(LocalDateTime.parse(jsonObject.getString("dateInscription")));
                                    client.setValiditeAssurence(LocalDateTime.parse(jsonObject.getString("validiteAssurence")));
                                    client.setEmail(jsonObject.getString("email"));
                                    client.setMotPasse(jsonObject.getString("motPasse"));
                                    client.setTelephone(jsonObject.getInt("telephone"));
                                    client.setActive(jsonObject.getBoolean("isActive"));
                                    client.setNotes(jsonObject.getString("notes"));
                                }
                                //Creating a user session
                                Intent intent = new Intent(Login.this,MainActivity.class);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putInt("id", client.getIdClient());
                                editor.putString("nom", client.getNom());
                                editor.putString("email", client.getEmail());
                                editor.putString("prenom", client.getPrenom());
                                editor.putInt("tel", client.getTelephone());
                                editor.commit();
                                Login.this.startActivity(intent);
                                Login.this.finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("Err",e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try{
                                if(error.networkResponse.statusCode==400)
                                {
                                    motdepasse.setError("Email ou Mot de passe est incorrect");
                                    progressBar.setVisibility(View.GONE);
                                }
                                Log.e("wsrong", error.toString());
                            }
                            catch (NullPointerException ex)
                            {
                                View view=findViewById(R.id.loginActivity);
                                Snackbar.make(view,"Server issue try later",Snackbar.LENGTH_LONG).show();
                                Log.e("wsrong", ex.toString());
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
            );
            VolleySingleton.getInstance(Login.this).addToRequestQueue(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}