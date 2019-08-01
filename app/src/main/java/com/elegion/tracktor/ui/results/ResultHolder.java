package com.elegion.tracktor.ui.results;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.ModelsModule;
import com.elegion.tracktor.event.ExpandViewEvent;
import com.elegion.tracktor.event.OpenResultEvent;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;

/**
 * @author Azret Magometov
 */
public class ResultHolder extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mIdText;
    private TextView mDistanceText;
    private TextView mDuration;
    private TextView mDate;
    private LinearLayout mLayout;



    private long mTrackId;

    public ResultHolder(View view) {
        super(view);
        mView = view;
        mIdText = view.findViewById(R.id.tv_id);
        mDistanceText = view.findViewById(R.id.tv_distance);
        mDate = view.findViewById(R.id.tv_date);
        mDuration = view.findViewById(R.id.tv_duration);
        mLayout = view.findViewById(R.id.sub_item);



    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDistanceText.getText() + "'";
    }

    public void bind(Track track) {
        mTrackId = track.getId();
        mIdText.setText(String.valueOf(mTrackId));
        mDistanceText.setText(StringUtil.getDistanceText(track.getDistance()));
        mDate.setText(StringUtil.getDateText(track.getDate()));
        mDuration.setText(StringUtil.getTimeText(track.getDuration()));

        boolean expanded = track.isExpanded();
        mLayout.setVisibility(expanded?View.VISIBLE:View.GONE);
        mView.setOnClickListener(v -> {
            EventBus.getDefault().post(new ExpandViewEvent(track.getId()));
        });

        mView.setOnLongClickListener(v -> {
            EventBus.getDefault().post(new OpenResultEvent(track.getId()));
            return false;
        });

    }




}
