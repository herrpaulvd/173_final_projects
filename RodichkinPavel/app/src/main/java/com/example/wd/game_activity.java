package com.example.wd;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

import net.objecthunter.exp4j.*;


public class game_activity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    TextView game_expression;
    EditText game_answer;
    Button game_checkAnswer;

    private int a = (int) (Math.random() * 101);
    private int b = (int) (Math.random() * 100) + 1;
    private int c = (int) (Math.random() * 4);
    private String[] signs = {"+", "-", "*", "/"};
    private String d = signs[c];
    private String exp = a + d + b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String username = getIntent().getStringExtra("hs_username");

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

        Cursor cursor = mDB.rawQuery("select current_score," +
                "highscore from USERS where username = '" + username + "'", null);
        cursor.moveToFirst();

        final int[] curr_hs = {Integer.parseInt(cursor.getString(0))};
        final int max_hs = Integer.parseInt(cursor.getString(1));

        cursor.close();

        setContentView(R.layout.activity_game);

        game_expression = (TextView) findViewById(R.id.game_expression);
        game_answer = (EditText) findViewById(R.id.game_answer);
        game_checkAnswer = (Button) findViewById(R.id.game_checkAnswer);

        game_expression.setText(exp);

        game_checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Expression calc =
                        new ExpressionBuilder(exp).build();
                double true_res = calc.evaluate();

                if (Integer.parseInt(game_answer.getText().toString()) == ((int) true_res)) {

                    curr_hs[0] += 1;

                    Toast.makeText(getApplicationContext(),
                            String.valueOf(curr_hs[0]),
                            Toast.LENGTH_LONG).show();

                    String query = MessageFormat.format(
                            "update USERS set current_score = {0} where username = {1}",
                            curr_hs[0], "'" + username + "'");
//                    Cursor upd = mDB.rawQuery(query, null);
//                    upd.close();
                    mDB.execSQL(query);

                    finish();
                    startActivity(getIntent());

                }
                else {
                    if (curr_hs[0] > max_hs){
                        String query = MessageFormat.format(
                                "update USERS set highscore = {0} where username = {1}",
                                "'" + curr_hs[0] + "'", "'" + username + "'");
                        // upd = mDB.rawQuery(query, null);
                        mDB.execSQL(query);
                    }

                    String query = MessageFormat.format(
                            "update USERS set current_score = 0 " +
                                    "where username = {0}", "'" + username + "'");
//                    Cursor upd = mDB.rawQuery(query, null);
                    mDB.execSQL(query);
//                    upd.close();

                    String lost = "Ты проиграл, твой результат: " + curr_hs[0];

                    Toast.makeText(getApplicationContext(),
                            lost, Toast.LENGTH_LONG).show();

                    Intent game_to_hs =
                            new Intent(game_activity.this, showHs_activity.class);
                    startActivity(game_to_hs);
                }
            }
        });

    }
}
