package com.example.projectkurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkurs.Help.Articles;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public  class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Articles> articles = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public Adapter(OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null)
            throw new NullPointerException("OnItemClickListener mustn't be null");

        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Articles article = articles.get(position);
        if (article != null) holder.bind(article, onItemClickListener);
    }

    //Когда ViewHolder переиспользуется, некоторые ресурсы необходимо очищать
    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.onViewRecycled();
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void submitList(List<Articles> articles) {
        this.articles.clear();
        this.articles.addAll(articles);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView newsTitle, newsDate;
        private ImageView imageView;
        private CardView cardView;

        private OnItemClickListener onItemClickListener;
        private Articles articles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsDate = itemView.findViewById(R.id.newsDate);

            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null && articles != null)
                        onItemClickListener.onItemClicked(articles);
                }
            });
        }

        public void bind(Articles articles, OnItemClickListener onItemClickListener) {
            this.articles = articles;
            this.onItemClickListener = onItemClickListener;

            newsTitle.setText(articles.getTitle());
            newsDate.setText(articles.getPublishedAt());

            String imageUrl = articles.getUrlToImage();
            Picasso.get().load(imageUrl).into(imageView);
        }

        void onViewRecycled() {
            articles = null;
            onItemClickListener = null;
        }
    }

    interface OnItemClickListener {
        void onItemClicked(Articles articles);
    }
}
