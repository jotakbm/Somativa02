package com.example.somativa02;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<PhotoSave> data;

    Adapter(Context context, List<PhotoSave> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = data.get(position).title;
        String description = data.get(position).description;
        if (data.get(position).uri != null)
            holder.image.setImageURI(data.get(position).uri);
        holder.textTitle.setText(title);
        holder.textDescription.setText(description);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView textTitle;
        TextView textDescription;
        TextView textDeep;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView_CardImage);
            textTitle = itemView.findViewById(R.id.textView_CardTitle);
            textDescription = itemView.findViewById(R.id.textView_CardDescription);
            textDeep = itemView.findViewById(R.id.editText_DeepText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PhotoScreen.actualInfo = PhotoManager.getInstance().GetPhoto(textTitle.getText().toString());
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), PhotoScreen.class));
                }
            });
        }
    }
}
