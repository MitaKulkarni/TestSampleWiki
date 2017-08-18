package com.example.samplewiki.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samplewiki.Constants.StringConstants;
import com.example.samplewiki.R;
import com.example.samplewiki.activity.DetailPageActivity;
import com.example.samplewiki.model.SearchResult.SearchQuery.Pages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mita on 8/19/2017.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pages> mSearchList;
    private Activity mActivity;

    public SearchResultsAdapter(Activity activity, ArrayList<Pages> searchList) {
        mSearchList = searchList;
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final Pages pages = mSearchList.get(position);

        viewHolder.titleTv.setText(pages.getTitle());
        viewHolder.descriptionTv.setText(pages.getTerms().getDescription());
        Picasso.with(viewHolder.itemView.getContext()).load(pages.getThumbnail().getSource()).into(viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, DetailPageActivity.class);
                intent.putExtra(StringConstants.pageId, pages.getPageId());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchList != null ? mSearchList.size() : 0;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTv, descriptionTv;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.layout_serach_item_iv);
            titleTv = itemView.findViewById(R.id.layout_serach_item_title_tv);
            descriptionTv = itemView.findViewById(R.id.layout_serach_item_desc_tv);
        }
    }
}

