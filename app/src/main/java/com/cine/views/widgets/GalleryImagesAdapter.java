package com.cine.views.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cine.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijayarvind.j on 11-05-2017.
 */

public class GalleryImagesAdapter extends RecyclerView.Adapter<GalleryImagesAdapter.MyViewHolder> {

    private List<String> imagesList;
    private Context context;

    public GalleryImagesAdapter(List<String> imagesList, Context mContext) {
        this.imagesList = imagesList;
        this.context = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iamge_gallery_row, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(!TextUtils.isEmpty(imagesList.get(position))) {
            Picasso.with(context).load("http://www.buyarecaplates.com/vpb-wall-photos/" + imagesList.get(position)).into(holder.galleryImageView);
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView galleryImageView;


        public MyViewHolder(View view) {
            super(view);
            galleryImageView = (ImageView) view.findViewById(R.id.mwGalleryImages);

        }
    }
}
