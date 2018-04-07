package tracker.jam.app.dhbw.com.jamtrackerapp;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

final class Constants {

    private Constants() {
    }

    //Density
    public static final String SERVER_URL_DENSITY = "http://my-json-server.typicode.com/crisvnait/jsondummy/test";

    //Geofencing
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = Geofence.NEVER_EXPIRE;
    static final float GEOFENCE_RADIUS_IN_METERS = 1000;
    static final HashMap<String, LatLng> GATEWAY_LANDMARKS = new HashMap<>();

    static {
        GATEWAY_LANDMARKS.put("KA Nord", new LatLng(49.019773, 8.470468));
        GATEWAY_LANDMARKS.put("KA Durlach", new LatLng(49.005600, 8.454503));
        GATEWAY_LANDMARKS.put("KA Mitte", new LatLng(48.993609, 8.437117));
        GATEWAY_LANDMARKS.put("Dreieck KA", new LatLng(48.979249, 8.436886));
        GATEWAY_LANDMARKS.put("Ettlingen", new LatLng(48.961417, 8.409667));
    }

    //Maps
    public static LatLng MAPS_CENTRUM = new LatLng(48.991579, 8.436256);

}
