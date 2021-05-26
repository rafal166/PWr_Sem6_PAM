package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Twój dziennik");

        onLoginButtonClick();
    }

    private void onLoginButtonClick() {
        findViewById(R.id.signin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText loginInput = findViewById(R.id.signin_login);
                EditText passwordInput = findViewById(R.id.signin_password);

                String login = loginInput.getText().toString();
                String password = passwordInput.getText().toString();

                LoadingDialog loading = new LoadingDialog(LoginActivity.this);
                loading.showLoading();
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .whereEqualTo("login", login)
                        .whereEqualTo("password", password)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Globals g = Globals.getInstance();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        g.setUserData(document.getId(), document.getData());

                                        if (g.isUserLoggedIn()) {
                                            Toast.makeText(getApplicationContext(), "Zalogowano pomyślnie", Toast.LENGTH_LONG).show();

                                            Intent dashboard = new Intent(LoginActivity.this, DashboardActivity.class);
                                            startActivity(dashboard);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Błędny login lub hasło", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Wystąpił nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                                    Log.w("Error reading users data", "Error getting documents.", task.getException());
                                }
                                loading.hideLoading();
                            }
                        });
            }
        });
    }

}