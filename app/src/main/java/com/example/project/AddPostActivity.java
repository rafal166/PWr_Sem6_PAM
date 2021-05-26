package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private static final String EMPTY_TITLE_ERROR = "Tytuł nie może być pusty";
    private static final String EMPTY_CONTENT_ERROR = "Zawartość wpisu nie może być pusta";
    private static final String SUCCESS_MESSAGE = "Pomyślnie dodano wpis";
    private static final String FAILURE_MESSAGE = "Błąd przy dodawaniu nowego wpisu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Toolbar toolbar = findViewById(R.id.add_post_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dodawanie nowego wpisu");
        actionBar.setDisplayHomeAsUpEnabled(true);

        onSubmitButtonClick();
    }

    private void onSubmitButtonClick() {
        findViewById(R.id.post_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText titleInput = findViewById(R.id.post_title);
                EditText contentInput = findViewById(R.id.post_content);

                String title = titleInput.getText().toString();
                String content = contentInput.getText().toString();

                if (title.isEmpty()) {
                    setToastMessage(EMPTY_TITLE_ERROR);
                    return;
                }
                if (content.isEmpty()) {
                    setToastMessage(EMPTY_CONTENT_ERROR);
                    return;
                }
                LoadingDialog loading = new LoadingDialog(AddPostActivity.this);
                loading.showLoading();

                Map<String, Object> post = new HashMap<>();
                post.put("title", title);
                post.put("content", content);
                post.put("create_time", new Timestamp(new Date()));
                post.put("user_id", Globals.getInstance().getUserId());

                FirebaseFirestore.getInstance()
                        .collection("posts")
                        .add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(SUCCESS_MESSAGE, "DocumentSnapshot written with ID: " + documentReference.getId());
                                setToastMessage(SUCCESS_MESSAGE);
                                startActivity(new Intent(AddPostActivity.this, DashboardActivity.class));
                                loading.hideLoading();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                setToastMessage(FAILURE_MESSAGE);
                                Log.w(FAILURE_MESSAGE, "Error adding document", e);
                                loading.hideLoading();
                            }
                        });
            }
        });
    }

    private void setToastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}