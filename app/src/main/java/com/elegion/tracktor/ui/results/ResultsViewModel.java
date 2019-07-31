package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.RepositoryModule;
import com.elegion.tracktor.util.StringUtil;

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

    public ResultsViewModel() {
        final Scope scope = Toothpick.openScopes(App.class, this);
        scope.installModules(new RepositoryModule());
        Toothpick.inject(this, scope);
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

        double energy = track.getDuration() * (activityType+1) * (weight / 100);
        mEnergy.postValue(StringUtil.getEnergyText(energy));


    }
}
