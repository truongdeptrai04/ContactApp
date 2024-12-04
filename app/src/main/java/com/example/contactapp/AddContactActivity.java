package com.example.contactapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.databinding.AddContactBinding;

public class AddContactActivity extends AppCompatActivity {
    private AddContactBinding biding;
    private AppDatabase appDatabase;
    private ContactDAO contactDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = AddContactBinding.inflate(getLayoutInflater());
        setContentView(biding.getRoot());

        appDatabase = AppDatabase.getInstance(getApplicationContext());
        contactDAO = appDatabase.contactDAO();

        biding.btnBack.setOnClickListener(view -> finish());

        biding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = biding.etName.getText().toString().trim();
                String phone = biding.etPhone.getText().toString().trim();
                String email = biding.etGmail.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(AddContactActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thêm contact vào Room Database
                AsyncTask.execute(() -> {
                    Contact newContact = new Contact(0, name, phone, email);
                    contactDAO.insert(newContact);

                    // Trả dữ liệu về MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("phone", phone);
                    resultIntent.putExtra("email", email);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                });
            }
        });
    }
}
