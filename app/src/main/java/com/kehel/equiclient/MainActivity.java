package com.kehel.equiclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private SharedPreferences sharedpreferences;
    private TextView email_textview,nom_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToolBar
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //sharedPreferences
        sharedpreferences = getSharedPreferences("UserInfos", Context.MODE_PRIVATE);

        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Change navigation header here
        View headerView = navigationView.getHeaderView(0);
        email_textview=headerView.findViewById(R.id.email_textview);
        nom_textview=headerView.findViewById(R.id.nom_textview);
        email_textview.setText(sharedpreferences.getString("email",null));
        nom_textview.setText(sharedpreferences.getString("nom",null));

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fargment_container,new SeanceFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_seances);
        }

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_infos:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fargment_container,new AccountFragment()).commit();
                break;
            }
            case R.id.nav_seances:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fargment_container,new SeanceFragment()).commit();
                break;
            }
            case R.id.nav_notes:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fargment_container,new NoteFragment()).commit();
                break;
            }
            case R.id.nav_logout:
            {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this, Login.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(this,"Deconexion",Toast.LENGTH_SHORT).show();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}