package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private ArrayList<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();
    private RecyclerView listItemsContainer;
    private TextView userDataCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Twoje wpisy");
        onAddPosterButtonClick();
        listItemsContainer = findViewById(R.id.recycler_view);
        userDataCont = findViewById(R.id.user_data_cont);

        Globals g = Globals.getInstance();
        userDataCont.setText(g.getUserName()+" "+g.getUserSurname());

        loadPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    /**
     * Metoda umożliwia obsługę górnego menu użytkownika
     * @param item - opcja menu, która została wciśnięta
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refreshButton:
                loadPosts();
                break;
            case R.id.logoutButton:
                Globals.getInstance().wipeUserData();
                Intent login = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(login);
                break;
                case R.id.aboutAuthor:
                Intent aboutAuthor = new Intent(DashboardActivity.this, AboutAuthorActivity.class);
                startActivity(aboutAuthor);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Metoda umożliwia załadowanie wpisów obecnie zalogowanego użytkownika
     */
    private void loadPosts() {
        LoadingDialog loading = new LoadingDialog(DashboardActivity.this);
        loading.showLoading();

        FirebaseFirestore.getInstance()
                .collection("posts")
                .whereEqualTo("user_id", Globals.getInstance().getUserId())
                .orderBy("create_time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documentsList = task.getResult().getDocuments();
                            ArrayList<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();

                            for (DocumentSnapshot document : documentsList) {
                                contentList.add(document.getData());
                            }
                            Log.d("ITEMS FROM DASHBOARD", contentList.toString() + "");

                            listItemsContainer.setAdapter(new DashboardListAdapter(contentList));
                            listItemsContainer.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                        } else {
                            Toast.makeText(getApplicationContext(), "Wystąpił nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                            Log.w("Error reading posts data", "Error getting documents.", task.getException());
                        }
                        loading.hideLoading();
                    }
                });
    }

    /**
     * Metoda uruchamia event listener na klik przycisku przenoszącego do formularza dodawania nowego wpisu
     */
    private void onAddPosterButtonClick() {
        findViewById(R.id.add_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPost = new Intent(DashboardActivity.this, AddPostActivity.class);
                startActivity(addPost);
            }
        });
    }
}