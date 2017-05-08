package com.cine.views.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cine.R;
import com.cine.service.model.SearchModel;
import com.cine.utils.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DELL on 08-05-2017.
 */

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.MyViewHolder> {

    ArrayList<SearchModel> searchUsersList;
    Context mContext;
    private ItemClickListener clickListener;



    public SearchUsersAdapter(ArrayList<SearchModel> searchUsersList, Context mContext) {
        this.searchUsersList = searchUsersList;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_recycler_row, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SearchModel searchModel = searchUsersList.get(position);
        holder.searchUserFullName.setText(!TextUtils.isEmpty(searchModel.getSearch_userfullname()) ? searchModel.getSearch_userfullname() : "");
        holder.searchUserCategory.setText(!TextUtils.isEmpty(searchModel.getSearch_usermaincat()) ? searchModel.getSearch_usermaincat() + "," : "");
        holder.searchUserSubCat.setText(!TextUtils.isEmpty(searchModel.getSearch_usersubcat()) ? searchModel.getSearch_usersubcat() : "");
        Picasso.with(mContext).load("http://www.buyarecaplates.com/" + searchModel.getSearch_userprofilepic()).into(holder.searchUserPic);
    }

    @Override
    public int getItemCount() {
        return searchUsersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AppCompatTextView searchUserFullName, searchUserCategory, searchUserSubCat;
        CircularImageView searchUserPic;


        public MyViewHolder(View view) {
            super(view);

            searchUserFullName = (AppCompatTextView)view.findViewById(R.id.searchUserFullName);
            searchUserCategory = (AppCompatTextView)view.findViewById(R.id.searchCategory);
            searchUserSubCat = (AppCompatTextView)view.findViewById(R.id.searchSubCategory);
            searchUserPic = (CircularImageView)view.findViewById(R.id.searchUserPic);
            view.setTag(view);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }

    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
