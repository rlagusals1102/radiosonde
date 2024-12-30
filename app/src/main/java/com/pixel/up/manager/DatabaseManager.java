package com.pixel.up.manager;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseManager {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://pixel-up-831c2-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private static final DatabaseReference locationRef = database.getReference("location");

    private static final FirebaseStorage storage = FirebaseStorage.getInstance("gs://pixel-up-831c2.appspot.com");

    public static void saveLocation(double latitude, double longitude, double altitude) {
        locationRef.child("latitude").setValue(latitude);
        locationRef.child("longitude").setValue(longitude);
        locationRef.child("altitude").setValue(altitude);
        locationRef.child("timestamp").setValue(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
    }

    public static void saveVideo(String path) throws IOException {
        StorageReference imageRef = storage.getReference(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));

        InputStream stream = new FileInputStream(new File(path));
        UploadTask uploadTask = imageRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                try {
                    File file = new File(path);
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
