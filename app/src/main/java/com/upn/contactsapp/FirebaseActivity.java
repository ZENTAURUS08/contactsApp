package com.upn.contactsapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upn.contactsapp.entities.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FirebaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        Button btn = findViewById(R.id.btnCreateOnFirebase);
        EditText etName = findViewById(R.id.etName);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference contactsRef = database.getReference("N00285348").child("contacts");

        btn.setOnClickListener(v -> {
            Contact c1 = new Contact("Diego", "12345678");
            c1.uuid = UUID.randomUUID().toString();

            Contact c2 = new Contact("Carlos", "123456");
            c2.uuid = UUID.randomUUID().toString();

            String name = etName.getText().toString();

            Contact c3 = new Contact(name, "123456");
            c3.uuid = UUID.randomUUID().toString();

            List<Contact> cs = List.of(c1, c2);
            contactsRef.setValue(cs);

            contactsRef.child(c3.uuid).setValue(c3);

            etName.setText("");
        });

        List<Contact> contacts = new ArrayList<>();
        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Contact c = child.getValue(Contact.class);
                    if (c != null) {
                        Log.i("MAIN_APP", "Contact UUID: " + c.uuid + ", Name: " + c.name + ", Phone: " + c.phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MAIN_APP", "Failed to read data", error.toException());
            }
        });
    }
}
