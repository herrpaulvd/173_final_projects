package com.example.wd;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import java.io.IOException;
import java.text.MessageFormat;

public class fscreen_activity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    Button fscreen_buttonLogin;
    Button fscreen_buttonRegister;
    private int error_code;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

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

        setContentView(R.layout.activity_fscreen);

        fscreen_buttonLogin = (Button) findViewById(R.id.fscreen_buttonLogin);

        fscreen_buttonLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText fscreen_text_login = (EditText) findViewById(R.id.fscreen_enterLogin);
            String fscreen_text_login_string =
                    (String) fscreen_text_login.getText().toString();

            EditText fscreen_text_password = (EditText) findViewById(R.id.fscreen_enterPassword);
            String fscreen_text_password_string =
                    (String) fscreen_text_password.getText().toString();

            String login = MessageFormat.format("SELECT * FROM users " +
                    "WHERE username = {0}", "'" + fscreen_text_login_string + "'");
            String password = MessageFormat.format("SELECT * FROM users " +
                    "WHERE password = {0}",  "'" + fscreen_text_password_string + "'");

            error_code = 0;
            try {
                StringBuilder login_from_db = new StringBuilder();

                Cursor cursor = mDB.rawQuery(login, null);
                cursor.moveToFirst();
                int rowsCnt = cursor.getCount();
                if (rowsCnt != 0) {
                    login_from_db.append(cursor.getString(0));
                }
                else {
                    error_code = 1;
                }
                cursor.close();
            } catch (SQLException e) {
                error_code = 1;
                throw e;
            }
            if (error_code == 0) {
                try {
                    StringBuilder password_from_db = new StringBuilder();

                    Cursor cursor = mDB.rawQuery(password, null);
                    cursor.moveToFirst();
                    int rowsCnt = cursor.getCount();
                    if (rowsCnt != 0) {
                        password_from_db.append(cursor.getString(0));
                    }
                    else {
                        error_code = 2;
                    }
                    cursor.close();
                } catch (SQLException e) {
                    error_code = 2;
                    throw e;
                }
            }

            if (error_code == 1) {
                Toast.makeText(getApplicationContext(),
                        "Ошибка, такого логина нет", Toast.LENGTH_LONG).show();
            } else if (error_code == 2) {
                Toast.makeText(getApplicationContext(),
                        "Ошибка, неверный пароль", Toast.LENGTH_LONG).show();
            }

            if (error_code == 0) {
                Toast.makeText(getApplicationContext(),
                        "Успешный логин!", Toast.LENGTH_LONG).show();
                Intent s_login =
                        new Intent(fscreen_activity.this, choose_activity.class);
                s_login.putExtra("login_username", fscreen_text_login_string);
                startActivity(s_login);
            }


        }
        });

        fscreen_buttonRegister = (Button) findViewById(R.id.fscreen_buttonRegister);

        fscreen_buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_register =
                        new Intent(fscreen_activity.this, register_activity.class);
                startActivity(new_register);
            }

        });
    }
}
