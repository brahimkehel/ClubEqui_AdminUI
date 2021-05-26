package com.kehel.equiclient;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.List;

public class RecyclerViewAdapter_seance extends RecyclerView.Adapter<RecyclerViewAdapter_seance.ViewHolder> {
    List<Seance> seanceList;
    Context context;
    LayoutInflater inflater;
    private DataBaseHelper db;

    public RecyclerViewAdapter_seance(Context c,List<Seance> list)
    {
        this.seanceList=list;
        this.context=c;
        this.inflater=LayoutInflater.from(context);
        db = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.grid_item_seance,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try  {
            holder.client.setText(seanceList.get(position).getIdClient().split("-")[1]);
            holder.moniteur.setText(seanceList.get(position).getIdMoniteur().split("-")[1]);
            holder.duration.setText(String.valueOf(seanceList.get(position).getDureeMinutes()));
            holder.date_debut.setText(String.valueOf(seanceList.get(position).getDateDebut()).split("T")[1]);
            holder.commentaire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Seance description:");
                    builder.setMessage(seanceList.get(position).getCommentaires());
                    builder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            if(seanceList.get(position).getDone())
            {
                holder.state_imageView.setImageResource(R.drawable.ic_check_mark);
            }
            else
            {
                holder.state_imageView.setImageResource(R.drawable.ic_remove);
            }



        }catch (Exception ex)
        {
            Log.d("wsrong",ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return seanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView client,moniteur,duration,date_debut,commentaire;
        private ImageView state_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            client=itemView.findViewById(R.id.client);
            moniteur=itemView.findViewById(R.id.moniteur);
            duration=itemView.findViewById(R.id.duree);
            state_imageView=itemView.findViewById(R.id.state_imageView);
            date_debut=itemView.findViewById(R.id.date_debut);
            commentaire=itemView.findViewById(R.id.commentaire);
        }
    }
}

