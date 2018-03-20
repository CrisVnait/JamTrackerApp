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
    public static final String BHF = "BHF";
    public static final String HOME_ACHERN = "HOME_ACHERN";
    public static final String A5 = "A5";

    //Density
    public static final String SERVER_URL_DENSITY = "http://my-json-server.typicode.com/crisvnait/jsondummy/test";

    //Geocoding
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "tracker.jam.app.dhbw.com.jamtrackerapp";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    //Geofencing
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = Geofence.NEVER_EXPIRE;
    static final float GEOFENCE_RADIUS_IN_METERS = 100;
    static final HashMap<String, LatLng> GATEWAY_LANDMARKS = new HashMap<>();

    static {
        GATEWAY_LANDMARKS.put(DHBW, new LatLng(49.026676, 8.385947));
        GATEWAY_LANDMARKS.put(HOME, new LatLng(49.00852, 8.39602));
        GATEWAY_LANDMARKS.put(BHF, new LatLng(48.993546, 8.400808));
        GATEWAY_LANDMARKS.put(HOME_ACHERN, new LatLng(48.6272, 8.066246));
    }
}
