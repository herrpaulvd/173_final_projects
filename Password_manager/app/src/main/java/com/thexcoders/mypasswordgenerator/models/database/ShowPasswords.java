package com.thexcoders.mypasswordgenerator.models.database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thexcoders.mypasswordgenerator.MainActivity;
import com.thexcoders.mypasswordgenerator.R;
import com.thexcoders.mypasswordgenerator.models.password.Password;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowPasswords extends AppCompatActivity {

    private ListView list_view;
    private List<Password> passwords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        list_view = findViewById(R.id.scroll_view);

        DatabaseHelper db = new DatabaseHelper(ShowPasswords.this);
        ArrayList<String> names = new ArrayList<>(db.getNamesList());
        if (names.size() == 0){
            Toast.makeText(this, "No saved passwords!", Toast.LENGTH_SHORT).show();
            finish();
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.simple_row, names);

        list_view.setAdapter(listAdapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                String currName = names.get(position);
                Intent intent = new Intent(ShowPasswords.this, EditPassword.class);
                intent.putExtra("name", currName);
                startActivity(intent);
            }

        });

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
