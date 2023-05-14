package com.thexcoders.mypasswordgenerator.models.database;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thexcoders.mypasswordgenerator.R;
import com.thexcoders.mypasswordgenerator.models.password.Password;

public class EditPassword extends AppCompatActivity {
    private EditText editPwdName, editPwdLogin, editPwdValue;
    private Button btnSave, btnDelete;
    private Password current_password;
    private String current_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        current_name = getIntent().getStringExtra("name");
        DatabaseHelper dbHelper = new DatabaseHelper(EditPassword.this);
        current_password = dbHelper.get_by_name(current_name);

        initViews();
    }
    private void initViews() {
        editPwdName = findViewById(R.id.edit_edit_pwd_name);
        editPwdLogin = findViewById(R.id.edit_edit_pwd_login);
        editPwdValue = findViewById(R.id.edit_edit_pwd_value);

        btnSave = findViewById(R.id.btn_save_edited);
        btnDelete = findViewById(R.id.btn_delete);

        String pwdString = current_password.getContentValues().get("password").toString();
        String loginString = current_password.getContentValues().get("login").toString();

        editPwdName.setText(current_name);
        editPwdLogin.setText(loginString);
        editPwdValue.setText(pwdString);

        btnSave.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(EditPassword.this);
            Password password = new Password(
                    editPwdName.getText().toString(),
                    editPwdLogin.getText().toString(),
                    editPwdValue.getText().toString()
            );

            boolean updated = dbHelper.update(current_name, password);
            if (updated) {
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(EditPassword.this);
            boolean deleted = dbHelper.delete(current_name);
            if (deleted) {
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                onRestart();
                finish();
            }
        });
    }
}
