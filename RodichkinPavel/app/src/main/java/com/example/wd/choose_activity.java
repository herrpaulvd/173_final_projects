package com.example.wd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class choose_activity extends AppCompatActivity {
    Button choose_buttonGame;
    Button choose_buttonHS;
    TextView choose_show_login;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose);

        String login_name = getIntent().getStringExtra("login_username");

        choose_buttonGame = (Button) findViewById(R.id.choose_buttonGame);
        choose_buttonHS = (Button) findViewById(R.id.choose_buttonHS);
        choose_show_login = (TextView) findViewById(R.id.choose_show_login);

        choose_show_login.setText("Привет, " + login_name + "!");
        choose_show_login.setTypeface(null, Typeface.BOLD);

        choose_buttonGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(choose_activity.this, game_activity.class);
                game.putExtra("hs_username", login_name);
                startActivity(game);
            }
        });

        choose_buttonHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hs = new Intent(choose_activity.this, showHs_activity.class);
                startActivity(hs);
            }
        });
    }
}
