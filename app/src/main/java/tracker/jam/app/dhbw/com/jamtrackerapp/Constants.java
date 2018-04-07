package tracker.jam.app.dhbw.com.jamtrackerapp;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

final class Constants {

    public static List<MyGeofence> NON_CHANGEABLE_GEOFENCE_LIST = new LinkedList<>();

    public static MyGeofence ENTRANCE = new MyGeofence("Entrance", new LatLng(49.118115, 8.551332), false, false, false);
    public static MyGeofence KA_NORD = new MyGeofence("KA Nord", new LatLng(49.019773, 8.470468), true, true, true);
    public static MyGeofence KA_DURLACH = new MyGeofence(" KA Durlach", new LatLng(49.005600, 8.454503), true, true, false);
    public static MyGeofence KA_MITTE = new MyGeofence("KA Mitte", new LatLng(48.993609, 8.437117), true, true, true);
    public static MyGeofence KA_DREIECK = new MyGeofence("Dreieck KA", new LatLng(48.979249, 8.436886), true, true, false);
    public static MyGeofence ETTLINGEN = new MyGeofence("Ettlingen", new LatLng(48.961417, 8.409667), true, true, true);

    //Density
    public static final String SERVER_URL_DENSITY = "http://my-json-server.typicode.com/crisvnait/jsondummy/test";

    //Geofencing
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = Geofence.NEVER_EXPIRE;
    static final float GEOFENCE_RADIUS_IN_METERS = 1000;
    static final List<MyGeofence> CHANGEABLE_GEOFENCE_LIST = new LinkedList<>();

    //Maps
    public static LatLng MAPS_CENTRUM = new LatLng(48.991579, 8.436256);


    private Constants() {
    }

    static {
        populateNonChangeableGeofenceList();
        populateChangeableGeofenceList();
    }

    public static void populateNonChangeableGeofenceList() {
        NON_CHANGEABLE_GEOFENCE_LIST.clear();
        NON_CHANGEABLE_GEOFENCE_LIST.add(ENTRANCE);
        NON_CHANGEABLE_GEOFENCE_LIST.add(KA_NORD);
        NON_CHANGEABLE_GEOFENCE_LIST.add(KA_DURLACH);
        NON_CHANGEABLE_GEOFENCE_LIST.add(KA_MITTE);
        NON_CHANGEABLE_GEOFENCE_LIST.add(KA_DREIECK);
        NON_CHANGEABLE_GEOFENCE_LIST.add(ETTLINGEN);
    }

    public static void populateChangeableGeofenceList() {
        CHANGEABLE_GEOFENCE_LIST.clear();
        CHANGEABLE_GEOFENCE_LIST.add(ENTRANCE);
        CHANGEABLE_GEOFENCE_LIST.add(KA_NORD);
        CHANGEABLE_GEOFENCE_LIST.add(KA_DURLACH);
        CHANGEABLE_GEOFENCE_LIST.add(KA_MITTE);
        CHANGEABLE_GEOFENCE_LIST.add(KA_DREIECK);
        CHANGEABLE_GEOFENCE_LIST.add(ETTLINGEN);
    }
}
