package com.joe.index.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.joe.base.bean.GirlsData;
import com.joe.index.R;
import com.joe.index.databinding.IndexItemGirlsBinding;

import java.util.List;


/**
 * Created by dxx on 2017/11/10.
 */

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.GirlsViewHolder> {

    List<GirlsData.ResultsBean> girlsList;

    public void setGirlsList(final List<GirlsData.ResultsBean> list) {
        if (girlsList == null) {
            girlsList = list;
            notifyItemRangeInserted(0, girlsList.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return girlsList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    GirlsData.ResultsBean oldData = girlsList.get(oldItemPosition);
                    GirlsData.ResultsBean newData = list.get(newItemPosition);
                    return oldData.get_id() == newData.get_id();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    GirlsData.ResultsBean oldData = girlsList.get(oldItemPosition);
                    GirlsData.ResultsBean newData = list.get(newItemPosition);
                    return oldData.get_id() == newData.get_id()
                            && oldData.getCreatedAt() == newData.getCreatedAt()
                            && oldData.getPublishedAt() == newData.getPublishedAt()
                            && oldData.getSource() == newData.getSource();
                }
            });
            girlsList = list;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public GirlsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IndexItemGirlsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.index_item_girls,
                        parent, false);
        return new GirlsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GirlsViewHolder holder, int position) {
        holder.binding.setGirlItem(girlsList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return girlsList == null ? 0 : girlsList.size();
    }

    static class GirlsViewHolder extends RecyclerView.ViewHolder {
        IndexItemGirlsBinding binding;

        public GirlsViewHolder(IndexItemGirlsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
