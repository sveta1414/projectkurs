package com.example.projectkurs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projectkurs.Help.Articles;
import com.example.projectkurs.Help.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;

    final String API_KEY = "bf2fa820f2d547dea1df1445e6e8f3fa";
    Button refeshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refeshButton = findViewById(R.id.refresh);
        refeshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchJson(getCountry(), API_KEY);
            }
        });

        adapter = new Adapter(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Articles articles) {
                Intent intent = new Intent(MainActivity.this, NewsInDetails.class);
                intent.putExtra("url", articles.getUrl());
                MainActivity.this.startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        final String country = getCountry();
        fetchJson(country,API_KEY);
}

    private void fetchJson(String country, String api_key) {
        Call<Headlines> call=Client.getInstance().getApi().getHeadlines(country,api_key);
        call.enqueue((new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                    adapter.submitList(response.body().getArticles());
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private String getCountry() {
        Locale locale=Locale.getDefault();
        String country=locale.getCountry();
        return country.toLowerCase();
    }
    }
