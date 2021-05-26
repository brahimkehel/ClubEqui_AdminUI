package com.kehel.equiclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ClubEqui_Client";
    private static final String TABLE_Seance = "Seance";
    private static final String TABLE_Remarque = "Remarque";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Seance_TABLE = "CREATE TABLE " + TABLE_Seance + "("
                +"idSeance INTEGER PRIMARY KEY,idClient TEXT,idMoniteur TEXT," +
                "dateDebut DATETIME,dureeMinutes INTEGER,isDone BOOLEAN,idPayement INTEGER,commentaires TEXT" + ")";
        db.execSQL(CREATE_Seance_TABLE);

        String CREATE_REMARQUE_TABLE="CREATE TABLE "+TABLE_Remarque+"(id INTEGER PRIMARY KEY AUTOINCREMENT,contenue TEXT,date_changement DATETIME)";
        db.execSQL(CREATE_REMARQUE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Seance);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Remarque);
        // Create tables again
        onCreate(db);
    }


    // code to add the new seance
    void addSeance(Seance seance) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+TABLE_Seance+" WHERE idSeance=?",new String[]{String.valueOf(seance.getIdSeance())});
        Log.d("ppp",String.valueOf(c.moveToFirst()));
        if (!(c.moveToFirst()) || c.getCount() ==0){
            ContentValues values = new ContentValues();
            values.put("idSeance", seance.getIdSeance());
            values.put("idClient",String.valueOf(seance.getIdClient()));
            values.put("idMoniteur",String.valueOf(seance.getIdMoniteur()));
            values.put("idPayement",seance.getIdPayement());
            values.put("commentaires",seance.getCommentaires());
            values.put("dureeMinutes",seance.getDureeMinutes());
            values.put("dateDebut",String.valueOf(seance.getDateDebut()));
            values.put("isDone",String.valueOf(seance.getDone()));

            // Inserting Row
            db.insert(TABLE_Seance, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        }
        else{
            //cursor is empty
            Log.d("ppp","seance exist");
        }
    }

    //code to get all seances
    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Seance> getSeances()
    {
        List<Seance> seanceList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_Seance,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            //cursor is empty
            Log.d("ppp","seance not found");
            db.close();
            return null;
        }
        else
        {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Seance s=new Seance();
                    s.setIdSeance(cursor.getInt(0));
                    s.setIdClient(String.valueOf(cursor.getString(1)));
                    s.setIdMoniteur(String.valueOf(cursor.getString(2)));
                    s.setDateDebut(LocalDateTime.parse(cursor.getString(3)));
                    s.setDureeMinutes(cursor.getInt(4));
                    s.setDone(Boolean.parseBoolean(cursor.getString(5)));
                    s.setIdPayement(cursor.getInt(6));
                    s.setCommentaires(String.valueOf(cursor.getString(7)));
                    seanceList.add(s);
                    cursor.moveToNext();
                }
            }

            db.close();
            return seanceList;
        }
    }

    //Update seance(id) isDone
    public void updateSeance(int id,boolean isDone) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE "+TABLE_Seance+" SET isDone ='"+isDone+"' WHERE idSeance = "+id);
            db.close();
            Log.d("jes","updated seance succesfuly");
        }catch (Exception ex)
        {
            Log.d("wsrong",ex.getMessage());
        }
    }

    //Delete seance(id)
    public void deleteSeance(int id) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM "+TABLE_Seance+" WHERE idSeance = "+id);
            db.close();
            Log.d("jes","deleted seance succesfuly");
        }catch (Exception ex)
        {
            Log.d("wsrong",ex.getMessage());
        }
    }

    // code to add the new remarque
    void addRemarque(Remarque remarque) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("contenue", String.valueOf(remarque.getContenue()));
            values.put("date_changement", String.valueOf(remarque.getDate_changement()));
            // Inserting Row
            db.insert(TABLE_Remarque, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        }catch (Exception ex)
        {
            //cursor is empty
            db.close();
            Log.d("ppp","remarque exist");
        }
    }
    // code to update the new remarque
    void updateRemarque(int id,String contenue) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_Remarque+" SET contenue ='"+contenue+"' WHERE id = "+id);
        db.close();
    }
    // code to delete the new remarque
    void deleteRemarque(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_Remarque+" WHERE id = "+id);
        db.close();
    }
    //code to get all remarques
    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Remarque> getRemarques()
    {
        List<Remarque> remarqueList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_Remarque,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            //cursor is empty
            Log.d("ppp","remarque not found");
            db.close();
            return null;
        }
        else
        {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Remarque r=new Remarque();
                    r.setId(cursor.getInt(0));
                    r.setContenue(String.valueOf(cursor.getString(1)));
                    r.setDate_changement(LocalDateTime.parse(cursor.getString(2)));
                    remarqueList.add(r);
                    cursor.moveToNext();
                }
            }

            db.close();
            return remarqueList;
        }
    }

    //code to get remarque by id
    @RequiresApi(api = Build.VERSION_CODES.O)
    Remarque getRemarque(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_Remarque+" WHERE id=?",new String[]{String.valueOf(id)});

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            //cursor is empty
            Log.d("ppp","remarque not found");
            db.close();
            return null;
        }
        else
        {
            Remarque r=new Remarque();
            r.setId(cursor.getInt(0));
            r.setContenue(cursor.getString(1));
            r.setDate_changement(LocalDateTime.parse(cursor.getString(2)));
            db.close();
            return r;
        }
    }
}

