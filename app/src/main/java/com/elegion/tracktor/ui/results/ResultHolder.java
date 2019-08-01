package com.elegion.tracktor.ui.results;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.event.OpenResultEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Azret Magometov
 */
public class ResultHolder extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mDateText;
    private TextView mDistanceText;
    private long mTrackId;

    public ResultHolder(View view) {
        super(view);
        mView = view;
        mDateText = view.findViewById(R.id.tv_date);
        mDistanceText = view.findViewById(R.id.tv_distance);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDistanceText.getText() + "'";
    }

    public void bind(Track track) {
        mTrackId = track.getId();
        mDateText.setText(String.valueOf(mTrackId));
        mDistanceText.setText(String.valueOf(track.getDistance()));
        mView.setOnClickListener(v -> {
            EventBus.getDefault().post(new OpenResultEvent(track.getId()));
        });
    }




}
