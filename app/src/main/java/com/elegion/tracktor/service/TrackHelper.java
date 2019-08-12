package com.elegion.tracktor.service;

import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.elegion.tracktor.App;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.StartTrackEvent;
import com.elegion.tracktor.event.StopTrackEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;
import com.elegion.tracktor.util.StringUtil;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class TrackHelper {
    private Location mLastLocation;
    private LatLng mLastPosition;
    private List<LatLng> mRoute = new ArrayList<>();
    private double mDistance;



    public boolean positionChanged(LatLng newPosition) {
        return mLastLocation.getLongitude() != newPosition.longitude || mLastLocation.getLatitude() != newPosition.latitude;
    }

    public void addPointToRoute(Location lastLocation) {
        mLastLocation = lastLocation;
        mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mRoute.add(mLastPosition);
    }
    public void startTrackEvent(){
        EventBus.getDefault().post(new StartTrackEvent(mLastPosition));
    }

    public void changePosition(LocationResult locationResult) {
        Location newLocation = locationResult.getLastLocation();
        LatLng newPosition = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());

        if (positionChanged(newPosition)) {

            mRoute.add(newPosition);
            LatLng prevPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mDistance += SphericalUtil.computeDistanceBetween(prevPosition, newPosition);
            EventBus.getDefault().post(new AddPositionToRouteEvent(prevPosition, newPosition, mDistance));
        }

        mLastLocation = newLocation;
        mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

    }

    public void updateTimerEvent(long totalSeconds) {
        EventBus.getDefault().post(new UpdateTimerEvent(totalSeconds, mDistance));
    }

    public void stopTrackEvent() {
        EventBus.getDefault().post(new StopTrackEvent(mRoute));
    }

    public String getDistanceText() {
        return StringUtil.getDistanceText(mDistance);
    }

    public void updateRoutEvent() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        String unit = preferences.getString("unit", "");
        EventBus.getDefault().post(new UpdateRouteEvent(mRoute, mDistance, unit));
    }
    public boolean isFirstPoint() {
        return mRoute.size() == 0 && mLastLocation == null && mLastPosition == null;
    }
}
