package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;

import java.util.List;

public class Feed extends CCQFBaseActivity/*AppCompatActivity*/ {

    private Button btnSend;
    private EditText inputMsg;
    private ListView listViewMessages;
    private List<MessagePacket> listMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        btnSend = (Button)findViewById(R.id.btnSend);
        inputMsg = (EditText)findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);

    }
}
