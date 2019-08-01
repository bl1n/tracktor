package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.util.Log;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.RepositoryModule;
import com.elegion.tracktor.event.ExpandViewEvent;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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



    public ResultsViewModel() {
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
        double energy = track.getDuration() * (activityType+1) *weight;
        mEnergy.postValue(StringUtil.getEnergyText(energy));
    }

    public void updateTrackComment(long trackId, String string){
        Track track = mRepository.getItem(trackId);
        track.setComment(string);
        mRepository.updateItem(track);
        mTrack.postValue(track);
    }


    public void onExpandedStateChange(ExpandViewEvent event){
        Track track = mRepository.getItem(event.getTrackId());
        track.setExpanded(!track.isExpanded());
        mRepository.updateItem(track);
        Log.d("Debug", "onExpandedStateChange: " + track.isExpanded());
    }


    @Override
    protected void onCleared() {
        Toothpick.closeScope(mScope);
        super.onCleared();
    }
    public void createRandomTrack(){
        mRepository.createAndInsertTrackFrom(123,123.00,"");

    }
}
