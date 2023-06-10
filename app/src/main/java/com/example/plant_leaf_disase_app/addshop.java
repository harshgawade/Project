package com.example.plant_leaf_disase_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addshop extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase ;
    DatabaseReference mref;
    FirebaseStorage firebaseStorage;
    ImageButton imageButton ;
    EditText edfirst,edlast,edprice;
    Button btninsert;
    private static final int code=1;
    Uri imageurl = null;


    EditText edname,edaddress,edcontact,editem1,editem1price,editem2,editem2price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addshop);

        imageButton = findViewById(R.id.imageButton2);
        edname = findViewById(R.id.edname);
        edaddress = findViewById(R.id.edaddress);
        edcontact = findViewById(R.id.edmobile);
        editem1 = findViewById(R.id.editem1);

        btninsert = findViewById(R.id.btninsert);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference().child("place");
        firebaseStorage = FirebaseStorage.getInstance();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == code && resultCode == RESULT_OK)
        {

            imageurl = data.getData();
            imageButton.setImageURI(imageurl);
        }

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edname.getText().toString().trim();
                String place1 = edaddress.getText().toString().trim();
                String des1 = edcontact.getText().toString().trim();
                String place2 = editem1.getText().toString().trim();

                StorageReference filepath =firebaseStorage.getReference().child("imagepost").child(imageurl.getLastPathSegment());
                filepath.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> downloadurl =taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                String t =task.getResult().toString();
                                DatabaseReference newpost =mref.push();

                                newpost.child("name").setValue(name);
                                newpost.child("address").setValue(place1);
                                newpost.child("contactno").setValue(des1);
                                newpost.child("product").setValue(place2);

                                newpost.child("imageurl").setValue(task.getResult().toString());
                                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
}