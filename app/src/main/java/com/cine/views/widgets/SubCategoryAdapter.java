package com.cine.views.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cine.R;
import com.cine.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by DELL on 28-04-2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    private List<String> subCategoryList;
    private Context context;
    private ItemClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public AppCompatTextView subCategoryTextView;

        public MyViewHolder(View view) {
            super(view);
            subCategoryTextView = (AppCompatTextView) view.findViewById(R.id.subCategoryTextView);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }


    public SubCategoryAdapter(Context context, List<String> subCategoryList) {
        this.subCategoryList = subCategoryList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_cat_recycler_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String subCategory = subCategoryList.get(position);
        holder.subCategoryTextView.setText((!TextUtils.isEmpty(subCategory)) ? subCategory : "");
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
