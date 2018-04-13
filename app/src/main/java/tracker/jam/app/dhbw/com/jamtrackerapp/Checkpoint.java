package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class Checkpoint {
    private String name;
    private LatLng latLng;
    private float geofenceRadius;
    private boolean drawCircleInMap;
    private JamLevel jamLevel;
    private TextView textViewName;
    private TextView textViewJamLevel;
    private boolean isLastExit;

    public Checkpoint(String name, LatLng latLng, float geofenceRadius, boolean drawCircleInMap, JamLevel jamLevel) {
        this.name = name;
        this.latLng = latLng;
        this.geofenceRadius = geofenceRadius;
        this.drawCircleInMap = drawCircleInMap;
        this.jamLevel = jamLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (textViewName != null) {
            textViewName.setText(name);
        }
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getGeofenceRadius() {
        return geofenceRadius;
    }

    public void setGeofenceRadius(float geofenceRadius) {
        this.geofenceRadius = geofenceRadius;
    }

    public boolean isDrawCircleInMap() {
        return drawCircleInMap;
    }

    public void setDrawCircleInMap(boolean drawCircleInMap) {
        this.drawCircleInMap = drawCircleInMap;
    }

    public JamLevel getJamLevel() {
        return jamLevel;
    }

    public void setJamLevel(JamLevel jamLevel) {
        this.jamLevel = jamLevel;
    }

    public TextView getTextViewName() {
        return textViewName;
    }

    public void setTextViewName(TextView textViewName) {
        this.textViewName = textViewName;
        this.textViewName.setText(name);
    }

    public TextView getTextViewJamLevel() {
        return textViewJamLevel;
    }

    public void setTextViewJamLevel(TextView textViewJamLevel) {
        this.textViewJamLevel = textViewJamLevel;
    }

    public boolean isLastExit() {
        return isLastExit;
    }

    public void setLastExit(boolean lastExit) {
        isLastExit = lastExit;
    }
}
