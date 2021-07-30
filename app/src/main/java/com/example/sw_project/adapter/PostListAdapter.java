package com.example.sw_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sw_project.Activity.ViewPostActivity;
import com.example.sw_project.R;
import com.example.sw_project.WriteInfo;

import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.postViewHolder> {

    private ArrayList<WriteInfo> arrayList;
    private Activity activity;
    private static final String TAG="postAdapter"; //디버그때 Log에 쓰일 TAG


    public PostListAdapter(ArrayList<WriteInfo> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list, parent, false);
        final postViewHolder holder = new postViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ViewPostActivity.class);
                intent.putExtra("writeInfo", arrayList.get(holder.getAdapterPosition()));
                activity.startActivity(intent);

            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.postViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Glide.with(holder.itemView.getContext()).load(arrayList.get(position).getImgUrl()).into(holder.poster);
        holder.title.setText(arrayList.get(position).getTitle());
        holder.writer.setText(arrayList.get(position).getWriterName());
        holder.scrapNum.setText(String.valueOf(arrayList.get(position).getScrapNum()));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class postViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView title;
        TextView writer;
        TextView scrapNum;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);
            this.poster = itemView.findViewById(R.id.poster);
            this.title = itemView.findViewById(R.id.titleView);
            this.writer = itemView.findViewById(R.id.writerView);
            this.scrapNum = itemView.findViewById(R.id.scrapNumView);
        }}
}

