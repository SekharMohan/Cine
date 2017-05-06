package com.cine.views.widgets;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cine.R;
import com.cine.service.model.UserWallModel;
import com.cine.views.activity.MyWallActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DELL on 01-05-2017.
 */

public class UserWallAdapter extends RecyclerView.Adapter<UserWallAdapter.MyViewHolder>  {

    ArrayList<UserWallModel> usersWallList;
    Context mContext;

    public UserWallAdapter(ArrayList<UserWallModel> usersWallList, Context mContext) {
        this.usersWallList = usersWallList;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_wall_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserWallAdapter.MyViewHolder holder, final int position) {
        final UserWallModel userWallModel = usersWallList.get(position);
        holder.uwUserName.setText(!TextUtils.isEmpty(userWallModel.getUserfullname()) ? userWallModel.getUserfullname() : "");
        holder.uwMainCategory.setText(!TextUtils.isEmpty(userWallModel.getUsermaincat()) ? userWallModel.getUsermaincat() : "");
        holder.uwSubCategory.setText(!TextUtils.isEmpty(userWallModel.getUsersubcat()) ? userWallModel.getUsersubcat() : "");
        Picasso.with(mContext).load("http://www.buyarecaplates.com/" + userWallModel.getUserprofilepic()).into(holder.uwImage);
        holder.uwUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wallIntent = new Intent(mContext, MyWallActivity.class);
                wallIntent.putExtra("username", usersWallList.get(position).getUsername());
                mContext.startActivity(wallIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersWallList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView uwImage;

        public AppCompatTextView uwUserName, uwMainCategory, uwSubCategory;


        public MyViewHolder(View view) {
            super(view);
            uwImage = (ImageView) view.findViewById(R.id.uwImageView);

            uwUserName = (AppCompatTextView)view.findViewById(R.id.uwFullName);
            uwMainCategory = (AppCompatTextView)view.findViewById(R.id.uwMainCategory);
            uwSubCategory = (AppCompatTextView)view.findViewById(R.id.uwSubCategory);
        }
    }
}
