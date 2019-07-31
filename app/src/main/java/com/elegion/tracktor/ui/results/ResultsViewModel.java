package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.RepositoryModule;

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

    public MutableLiveData<Track> getTrack() {
        return mTrack;
    }

    public ResultsViewModel() {
        final Scope scope = Toothpick.openScopes(App.class, this);
        scope.installModules(new RepositoryModule());
        Toothpick.inject(this, scope);
        deleted.postValue(false);
    }

    public void loadTracks() {
        if (mTracks.getValue() == null || mTracks.getValue().isEmpty()) {
            mTracks.postValue(mRepository.getAll());
        }
    }

    public void loadTrack(long trackId){
        final Track track = mRepository.getItem(trackId);
        mTrack.postValue(track);
    }

    public MutableLiveData<Boolean> isDeleted() {
        return deleted;
    }

    public void deleteTrack(long id){
        mRepository.deleteItem(id);
        deleted.postValue(true);
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }
}
