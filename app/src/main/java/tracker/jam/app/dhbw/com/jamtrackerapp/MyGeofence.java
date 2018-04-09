package tracker.jam.app.dhbw.com.jamtrackerapp;

import com.google.android.gms.maps.model.LatLng;

public class MyGeofence {
    private String name;
    private LatLng latLng;
    private boolean isExit;
    private boolean sendSuggestion;
    private boolean drawCircleInMap;
    private boolean camAvailable;
    private JamLevel jamLevel;

    public MyGeofence(String name, LatLng latLng, boolean isExit, boolean sendSuggestion, boolean drawCircleInMap, boolean camAvailable, JamLevel jamLevel) {
        this.name = name;
        this.latLng = latLng;
        this.isExit = isExit;
        this.sendSuggestion = sendSuggestion;
        this.drawCircleInMap = drawCircleInMap;
        this.camAvailable = camAvailable;
        this.jamLevel = jamLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public boolean isSendSuggestion() {
        return sendSuggestion;
    }

    public void setSendSuggestion(boolean sendSuggestion) {
        this.sendSuggestion = sendSuggestion;
    }

    public boolean isDrawCircleInMap() {
        return drawCircleInMap;
    }

    public void setDrawCircleInMap(boolean drawCircleInMap) {
        this.drawCircleInMap = drawCircleInMap;
    }

    public boolean isCamAvailable() {
        return camAvailable;
    }

    public void setCamAvailable(boolean camAvailable) {
        this.camAvailable = camAvailable;
    }

    public JamLevel getJamLevel() {
        return jamLevel;
    }

    public void setJamLevel(JamLevel jamLevel) {
        this.jamLevel = jamLevel;
    }
}
