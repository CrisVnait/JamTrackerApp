package tracker.jam.app.dhbw.com.jamtrackerapp;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

final class Constants {

    private Constants() {
    }

    //Locations
    public static final String DHBW = "DHBW";
    public static final String HOME = "HOME";
    public static final String HOME_ACHERN = "HOME_ACHERN";
    public static final String CORRECT_STREET = "E35";

    //Density
    public static final String SERVER_URL_DENSITY = "http://my-json-server.typicode.com/crisvnait/jsondummy/test";

    //Geofencing
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = Geofence.NEVER_EXPIRE;
    static final float GEOFENCE_RADIUS_IN_METERS = 1000;
    static final HashMap<String, LatLng> GATEWAY_LANDMARKS = new HashMap<>();

    static {
        GATEWAY_LANDMARKS.put(DHBW, new LatLng(49.026676, 8.385947));
        GATEWAY_LANDMARKS.put(HOME, new LatLng(49.00852, 8.39602));
        GATEWAY_LANDMARKS.put(HOME_ACHERN, new LatLng(48.6272, 8.066246));
    }

    //Maps
    public static LatLng MAPS_CENTRUM = new LatLng(48.987868, 8.417231);

}
