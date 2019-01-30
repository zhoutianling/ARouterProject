package com.joe.discovery.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.joe.base.bean.NewsData;
import com.joe.discovery.R;
import com.joe.discovery.databinding.DiscoveryNewsItemBinding;

import java.util.List;


/**
 * Created by dxx on 2017/11/10.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.GirlsViewHolder> {

    List<NewsData.ResultsBean> newsList;

    public void setNewsList(final List<NewsData.ResultsBean> list) {
        if (newsList == null) {
            newsList = list;
            notifyItemRangeInserted(0, newsList.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return newsList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    NewsData.ResultsBean oldData = newsList.get(oldItemPosition);
                    NewsData.ResultsBean newData = list.get(newItemPosition);
                    return oldData.get_id() == newData.get_id();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    NewsData.ResultsBean oldData = newsList.get(oldItemPosition);
                    NewsData.ResultsBean newData = list.get(newItemPosition);
                    return oldData.get_id() == newData.get_id()
                            && oldData.getCreatedAt() == newData.getCreatedAt()
                            && oldData.getPublishedAt() == newData.getPublishedAt()
                            && oldData.getSource() == newData.getSource();
                }
            });
            newsList = list;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public GirlsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DiscoveryNewsItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.discovery_news_item,
                        parent, false);
        return new GirlsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GirlsViewHolder holder, int position) {
        holder.binding.setNewsItem(newsList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    static class GirlsViewHolder extends RecyclerView.ViewHolder {
        DiscoveryNewsItemBinding binding;

        public GirlsViewHolder(DiscoveryNewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
