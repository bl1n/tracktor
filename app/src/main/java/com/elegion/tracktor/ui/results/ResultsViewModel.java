package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.util.Log;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.RepositoryModule;
import com.elegion.tracktor.event.DeleteTrackEvent;
import com.elegion.tracktor.event.ExpandViewEvent;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;

/**
 * @author Azret Magometov
 */
public class ResultsViewModel extends ViewModel {

    @Inject
    RealmRepository mRepository;

    private MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();

    private MutableLiveData<Track> mTrack = new MutableLiveData<>();

    private MutableLiveData<Boolean> deleted = new MutableLiveData<>();

    private MutableLiveData<String> mEnergy = new MutableLiveData<>();

    private final Scope mScope;

    private int mField = 1;


    public ResultsViewModel() {
        EventBus.getDefault().register(this);
        mScope = Toothpick.openScopes(App.class, this);
        mScope.installModules(new RepositoryModule());
        Toothpick.inject(this, mScope);
        deleted.postValue(false);
    }

    //tracks
    public void loadTracks() {
        if (mTracks.getValue() == null || mTracks.getValue().isEmpty()) {
            mTracks.postValue(mRepository.getAll());
        }
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }

    //track
    public void loadTrack(long trackId) {
        final Track track = mRepository.getItem(trackId);
        mTrack.postValue(track);
    }

    public MutableLiveData<Track> getTrack() {
        return mTrack;
    }

    //delete
    public MutableLiveData<Boolean> isDeleted() {
        return deleted;
    }

    public void deleteTrack(long id) {
        mRepository.deleteItem(id);
        deleted.postValue(true);
    }

    //energy
    public MutableLiveData<String> getEnergy() {
        return mEnergy;
    }

    public void loadEnergy(long trackId, int activityType, SharedPreferences preferences) {
        Track track = mRepository.getItem(trackId);
        double weight = Double.parseDouble(preferences.getString("weight", "1"));
        double energy = track.getDuration() * (activityType + 1) * weight;
        mEnergy.postValue(StringUtil.getEnergyText(energy));
    }

    public void updateTrackComment(long trackId, String string) {
        Track track = mRepository.getItem(trackId);
        track.setComment(string);
        mRepository.updateItem(track);
        mTrack.postValue(track);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExpandedStateChange(ExpandViewEvent event) {
        Track track = mRepository.getItem(event.getTrackId());
        track.setExpanded(!track.isExpanded());
        mRepository.updateItem(track);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteTrack(DeleteTrackEvent event) {
        mRepository.deleteItem(event.getTrackId());
    }


    public void sortTracks() {
        Collections.sort(mTracks.getValue(), (o1, o2) -> o1.getDistance().compareTo(o2.getDistance()));
    }

    @Override
    protected void onCleared() {
        EventBus.getDefault().unregister(this);
        Toothpick.closeScope(mScope);
        super.onCleared();
    }


}
//        if (mField != 3) {
//            mField++;
//        } else {
//            mField = 1;
//        }
//        List<Track> tracks = event.getTracks();
//        switch (mField) {
//            case 1: {
//                Collections.sort(tracks, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
//                break;
//            }
//
//            case 3: {
//                Collections.sort(tracks, (o1, o2) -> o1.getDistance().compareTo(o2.getDistance()));
//                break;
//            }
//
//            case 2: {
//                Collections.sort(tracks, new DurationComparator());
//                break;
//            }
//        }