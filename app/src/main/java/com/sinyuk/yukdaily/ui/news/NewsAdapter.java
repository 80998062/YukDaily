package com.sinyuk.yukdaily.ui.news;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.NewsItemBinding;
import com.sinyuk.yukdaily.entity.news.Story;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinyuk on 16.1.4.
 * 有header和footer的recycleView
 */
public class NewsAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    public static final String TAG = "NewsAdapter";
    private static final int HEADER_VIEW_TYPE = Integer.MAX_VALUE;
    private static final int HEADER_VIEW_ID = Integer.MAX_VALUE;
    private List<Story> stories = new ArrayList<>();
    private ViewDataBinding headerBinding = null;



    private boolean hasHeader() {
        return headerBinding != null && headerBinding.getRoot() != null;
    }

    private boolean isHeader(int position) {
        return hasHeader() && position == 0;
    }

    public void addHeaderBinding(ViewDataBinding binding) {
        if (headerBinding == null) {
            headerBinding = binding;
            notifyItemInserted(0);
        } else {
            headerBinding = binding;
            notifyItemChanged(0);
        }

    }


    private int itemPositionInData(int rvPosition) {
        return rvPosition - (hasHeader() ? 1 : 0);
    }

    private int itemPositionInRV(int dataPosition) {
        return dataPosition + (hasHeader() ? 1 : 0);
    }

    @Override
    public long getItemId(int position) {
        if (isHeader(position)) {
            return HEADER_VIEW_ID;
        }
        if (stories.get(itemPositionInData(position)) != null) {
            return stories.get(itemPositionInData(position)).getId();
        }
        return RecyclerView.NO_ID;
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return HEADER_VIEW_TYPE;
        }
        return super.getItemViewType(itemPositionInData(position));
    }

    public void setData(List<Story> data) {
        if (stories.isEmpty()) {
            stories.addAll(data);
            notifyItemRangeInserted(itemPositionInRV(0), data.size());
        } else {
            stories.clear();
            stories.addAll(data);
            notifyItemRangeChanged(itemPositionInRV(0), data.size());
        }
    }

    public void appendData(List<Story> data) {
        final int start = stories.size();
        stories.addAll(data);
        notifyItemRangeInserted(itemPositionInRV(start), data.size());
    }


    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            return new BindingViewHolder<>(headerBinding);
        } else {
            final NewsItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.news_item, parent, false);
            return new BindingViewHolder<>(binding);
        }
    }


    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (!isHeader(position)) {
            if (stories.get(itemPositionInData(position)) != null) {
                holder.getBinding().setVariable(BR.adapter, this);
                holder.getBinding().setVariable(BR.story, stories.get(itemPositionInData(position)));
            }
        }
    }

    public void onItemClick(View view, int id) {
        BrowserActivity.start(view.getContext(), id);
    }

    private int getDataItemCount() {
        return stories == null ? 0 : stories.size();
    }


    @Override
    public int getItemCount() {
        return getDataItemCount() + (hasHeader() ? 1 : 0);
    }
}



