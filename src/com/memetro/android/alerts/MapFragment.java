/*
 * Copyright 2013 Nytyr [me at nytyr dot me]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.memetro.android.alerts;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.memetro.android.R;

public class MapFragment extends Fragment {

    private static View inflaterMap;
    private final static String TAG = "MapFragment";
    private GoogleMap map;
    private LocationManager locationManager;
    private LatLng latlng = new LatLng(40.415364,-3.707398);
    private FollowMeLocationSource followMeLocationSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (inflaterMap != null) {
            ViewGroup parent = (ViewGroup) inflaterMap.getParent();
            if (parent != null)
                parent.removeView(inflaterMap);
        }
        try {
            inflaterMap = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
            //
        }

        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        followMeLocationSource = new FollowMeLocationSource();

        final Bundle arguments = getArguments();
        if (arguments != null) {
            Log.d("Arguments", "Data > "+arguments);
            if (arguments.getString("lat") != null && arguments.getString("long") != null){
                latlng = new LatLng(
                        Double.valueOf(arguments.getString("lat")),
                        Double.valueOf(arguments.getString("long"))
                );
            }
        }
        map.clear();
        map.setLocationSource(followMeLocationSource);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.setMyLocationEnabled(true);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        Marker marker = map.addMarker(new MarkerOptions().position(latlng));

        return inflaterMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        followMeLocationSource.getBestAvailableProvider();
    }

    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged! " + new LatLng(location.getLatitude(), location.getLongitude()).toString());
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    private class FollowMeLocationSource implements LocationSource, LocationListener {

        private OnLocationChangedListener mListener;

        private final Criteria criteria = new Criteria();
        private String bestAvailableProvider;
        /* Updates are restricted to one every 10 seconds, and only when
         * movement of more than 10 meters has been detected.*/
        private final int minTime = 20000;     // minimum time interval between location updates, in milliseconds
        private final int minDistance = 5;    // minimum distance between location updates, in meters

        private FollowMeLocationSource() {
            Log.d(TAG, "FollowMeLocationSource!");
            // Get reference to Location Manager
            // locationManager = (LocationManager) AppContext.context.getSystemService(Context.LOCATION_SERVICE);

            // Specify Location Provider criteria
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            criteria.setSpeedRequired(true);
            criteria.setCostAllowed(true);
        }

        private void getBestAvailableProvider() {
            Log.d(TAG, "getBestAvailableProvider!");
            /* The preffered way of specifying the location provider (e.g. GPS, NETWORK) to use
             * is to ask the Location Manager for the one that best satisfies our criteria.
             * By passing the 'true' boolean we ask for the best available (enabled) provider. */
            bestAvailableProvider = locationManager.getBestProvider(criteria, true);
        }

        /* Activates this provider. This provider will notify the supplied listener
         * periodically, until you call deactivate().
         * This method is automatically invoked by enabling my-location layer. */
        @Override
        public void activate(LocationSource.OnLocationChangedListener listener) {
            Log.d(TAG, "activate!");
            // We need to keep a reference to my-location layer's listener so we can push forward
            // location updates to it when we receive them from Location Manager.
            mListener = listener;

            // Request location updates from Location Manager
            if (bestAvailableProvider != null) {
                locationManager.requestLocationUpdates(bestAvailableProvider, minTime, minDistance, this);
                if(latlng!= null) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
                }
            }
        }

        /* Deactivates this provider.
         * This method is automatically invoked by disabling my-location layer. */
        @Override
        public void deactivate() {
            Log.d(TAG, "deactivate!");
            // Remove location updates from Location Manager
            locationManager.removeUpdates(this);

            mListener = null;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged! " + new LatLng(location.getLatitude(), location.getLongitude()).toString());
            /* Push location updates to the registered listener..
             * (this ensures that my-location layer will set the blue dot at the new/received location) */
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }

            /* ..and Animate camera to center on that location !
             * (the reason for we created this custom Location Source !) */
            map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.d(TAG, "onStatusChanged!");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d(TAG, "onProviderEnabled!");
            map.setLocationSource(followMeLocationSource);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.d(TAG, "onProviderDisabled!");
            locationManager.removeUpdates(followMeLocationSource);
        }
    }
}
