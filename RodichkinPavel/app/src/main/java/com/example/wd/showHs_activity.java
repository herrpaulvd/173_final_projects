package com.example.wd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class showHs_activity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showhs);

        Button showhs_button_to_choose;
        showhs_button_to_choose = (Button) findViewById(R.id.showhs_button_to_choose);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException ex) {
            throw new Error("Невозможно обновить БД");
        }

        try {
            mDB = mDBHelper.getWritableDatabase();

        } catch (SQLException e) {
            throw e;
        }

        ArrayList<HashMap<String, Object>> users = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> user;

        Cursor cursor = mDB.rawQuery("SELECT * FROM users", null);
        cursor.moveToFirst();

        user = new HashMap<String, Object>();
        user.put("Username", cursor.getString(1));
        user.put("Highscore", cursor.getString(4));
        String user_to_choose = cursor.getString(1);
        users.add(user);
        cursor.close();

        String[] from = {"Username", "Highscore"};
        int[] to = {R.id.hsTextviewusername, R.id.hsTextviewhighscore};

        SimpleAdapter hsAdapter = new SimpleAdapter(this, users,
                R.layout.adapter_item, from, to);
        ListView hsListView = (ListView) findViewById(R.id.showhs_hslist);

        hsListView.setAdapter(hsAdapter);

        showhs_button_to_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(showHs_activity.this, choose_activity.class);
                intent.putExtra("login_username", user_to_choose);
                startActivity(intent);
            }
        });
    }
}
