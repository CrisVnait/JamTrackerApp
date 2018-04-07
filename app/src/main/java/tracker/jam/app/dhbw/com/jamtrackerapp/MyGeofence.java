package tracker.jam.app.dhbw.com.jamtrackerapp;

import com.google.android.gms.maps.model.LatLng;

public class MyGeofence {
    private String name;
    private LatLng latLng;
    private boolean sendSuggestion;
    private boolean drawCircleInMap;
    private boolean camAvailable;

    public MyGeofence(String name, LatLng latLng, boolean sendSuggestion, boolean drawCircleInMap, boolean camAvailable) {
        this.name = name;
        this.latLng = latLng;
        this.sendSuggestion = sendSuggestion;
        this.drawCircleInMap = drawCircleInMap;
        this.camAvailable = camAvailable;
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
}
