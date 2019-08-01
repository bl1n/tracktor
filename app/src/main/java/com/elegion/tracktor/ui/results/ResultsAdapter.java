package com.elegion.tracktor.ui.results;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.event.ExpandViewEvent;

import org.greenrobot.eventbus.EventBus;

public class ResultsAdapter extends ListAdapter<Track, ResultHolder> {


    private static final DiffUtil.ItemCallback<Track> DIFF_CALLBACK = new DiffUtil.ItemCallback<Track>() {

        @Override
        public boolean areItemsTheSame(Track oldItem, Track newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Track oldItem, Track newItem) {
            return oldItem.equals(newItem);
        }
    };

    ResultsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_track, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        Track track = getItem(position);
        holder.bind(track);
        holder.itemView.setOnClickListener(v->{
            EventBus.getDefault().post(new ExpandViewEvent(track.getId()));
            notifyItemChanged(position);
        });
    }

}
