package com.example.wd;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.MessageFormat;

public class register_activity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    Button register_buttonRegister;

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

        setContentView(R.layout.activity_register);

        register_buttonRegister = (Button) findViewById(R.id.register_buttonRegister);

        register_buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText register_text_login = (EditText) findViewById(R.id.register_enterLogin);
                String register_text_login_string =
                        (String) register_text_login.getText().toString();

                EditText register_text_password = (EditText) findViewById(R.id.register_enterPassword);
                String register_text_password_string =
                        (String) register_text_password.getText().toString();

                String check_login = MessageFormat.format("SELECT * FROM users " +
                        "WHERE username = {0}", "'" + register_text_login_string + "'");
                error_code = 0;

                try {
                    StringBuilder login_from_db = new StringBuilder();

                    Cursor cursor = mDB.rawQuery(check_login, null);
                    cursor.moveToFirst();
                    int rowsCnt = cursor.getCount();
                    if (rowsCnt != 0) {
                        error_code = 1;
                    }
                    cursor.close();
                } catch (SQLException e) {
                    error_code = 1;
                    throw e;
                }

                if (register_text_login_string.length() == 0) {
                    error_code = 2;
                }

                if (register_text_password_string.length() < 8) {
                    error_code = 3;
                }

                if (register_text_password_string.length() > 64 ) {
                    error_code = 4;
                }

                if (error_code == 1) {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка, такой пользователь уже существует",
                            Toast.LENGTH_LONG).show();
                }
                else if (error_code == 2) {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка, логин не может быть пустым",
                            Toast.LENGTH_LONG).show();
                }
                else if (error_code == 3) {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка, пароль не может быть короче 8 символов",
                            Toast.LENGTH_LONG).show();
                }
                else if (error_code == 4) {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка, пароль не может быть длинее 64 символов",
                            Toast.LENGTH_LONG).show();
                    }
                else {
                    try {
                        String date_to_db_string =
                                MessageFormat.format("INSERT INTO users " +
                                "VALUES  (0, {0}, {1}, 0, 0)", "'" +
                                        register_text_login_string + "'",
                                        "'" + register_text_password_string + "'");
                        mDB.execSQL(date_to_db_string);
                    } catch (SQLException e) {
                        error_code = 5;
                        throw e;
                    }
                }

                if (error_code == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Успешная регистрация",
                            Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

    }

}
