package com.example.contactapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contactapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CONTACT_REQUEST_CODE = 1;
    private ActivityMainBinding binding;
    private ArrayList<Contact> contactList;
    private ContactsAdapter contactsAdapter;

    private AppDatabase appDatabase;
    private ContactDAO contactDAO;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(contactList);
        binding.rvContacts.setAdapter(contactsAdapter);

        contactList.add(new Contact(1,"Nguyen Quang Truong", "123456789", "abc@gmail.com"));
        contactList.add(new Contact(2,"Nguyen Quang Hai", "123456789", "abc@gmail.com"));
        contactList.add(new Contact(3,"Nguyen Quang Huy", "123456789", "abc@gmail.com"));

        contactsAdapter.notifyDataSetChanged();

        binding.btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE);
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterContacts(query); // Tìm kiếm khi nhấn nút "Search"
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText); // Tìm kiếm khi thay đổi văn bản
                return false;
            }
        });

        AsyncTask.execute(() -> {
            appDatabase = AppDatabase.getInstance(getApplicationContext());
            contactDAO = appDatabase.contactDAO();

            // Tải dữ liệu từ cơ sở dữ liệu
            List<Contact> databaseContacts = contactDAO.getAll();
            runOnUiThread(() -> {
                contactList.clear();
                contactList.addAll(databaseContacts);
                contactsAdapter.notifyDataSetChanged();
            });
        });


    }
    private void filterContacts(String query) {
        ArrayList<Contact> filteredList = new ArrayList<>();

        for (Contact contact : contactList) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(contact);
            }
        }

        contactsAdapter.updateList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            String email = data.getStringExtra("email");

            Contact newContact = new Contact(0, name, phone, email);

            // Thêm vào danh sách hiển thị
            contactList.add(newContact);
            contactsAdapter.notifyItemInserted(contactList.size() - 1);

            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
        }
    }

}