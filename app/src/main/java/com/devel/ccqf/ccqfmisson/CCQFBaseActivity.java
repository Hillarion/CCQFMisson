package com.devel.ccqf.ccqfmisson;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by thierry on 16-05-31.
 */
public class CCQFBaseActivity extends AppCompatActivity {
//    protected ActionMenuView amvMenu;
    protected ActionBar actionBar;
    protected Menu mainMenu;
    protected static boolean enableMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableMenu = false;
        actionBar = getSupportActionBar();

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("");
        Drawable logo = getResources().getDrawable(R.mipmap.ccqf_logo);
        actionBar.setLogo(logo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mainMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Options réservés au compte admin
        int id = item.getItemId();
        if(enableMenu) {

            switch (id) {
                case R.id.action_settings://Nouveau Survey
                    Intent i = new Intent(this, SurveyCreate.class);
                    startActivity(i);
                    break;
                case R.id.action_result://Consulter résulat du survey
                    Intent i2 = new Intent(this, SurveyResults.class);
                    startActivity(i2);
                    break;
                case R.id.action_newB2b://Nouveau B2B (formulaire quie crée un object Event)
                    Intent b2b = new Intent(this, NewB2B.class);
                    startActivity(b2b);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
