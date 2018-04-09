package tracker.jam.app.dhbw.com.jamtrackerapp;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.LinkedList;
import java.util.List;

final class Constants {

    public static List<MyGeofence> NON_CHANGEABLE_GEOFENCE_LIST = new LinkedList<>();

    public static MyGeofence ENTRANCE = new MyGeofence("Entrance", new LatLng(49.118115, 8.551332), false, false, false, false, JamLevel.NOCAM);
    public static MyGeofence KA_NORD = new MyGeofence("KA Nord", new LatLng(49.019773, 8.470468), true, true, true, true, JamLevel.UNDEFINED);
    public static MyGeofence KA_DURLACH = new MyGeofence(" KA Durlach", new LatLng(49.005600, 8.454503), true, true, true, false, JamLevel.NOCAM);
    public static MyGeofence KA_MITTE = new MyGeofence("KA Mitte", new LatLng(48.993609, 8.437117), true, true, true, true, JamLevel.UNDEFINED);
    public static MyGeofence KA_DREIECK = new MyGeofence("Dreieck KA", new LatLng(48.979249, 8.436886), true, true, true, false, JamLevel.NOCAM);
    public static MyGeofence ETTLINGEN = new MyGeofence("Ettlingen", new LatLng(48.961417, 8.409667), true, true, true, true, JamLevel.UNDEFINED);

    //Density
    public static final String SERVER_URL_DENSITY = "http://my-json-server.typicode.com/crisvnait/jsondummy/test";

    //Geofencing
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = Geofence.NEVER_EXPIRE;
    static final float GEOFENCE_RADIUS_IN_METERS = 1000;
    static final List<MyGeofence> CHANGEABLE_GEOFENCE_LIST = new LinkedList<>();

    //Maps
    public static LatLngBounds mapsLatLngBounds = new LatLngBounds(Constants.ETTLINGEN.getLatLng(), Constants.KA_NORD.getLatLng());


    private Constants() {
    }

    static {
        addElementsToList(NON_CHANGEABLE_GEOFENCE_LIST);
        populateChangeableGeofenceList();
    }

    public static void populateChangeableGeofenceList() {
        addElementsToList(CHANGEABLE_GEOFENCE_LIST);
    }

    private static void addElementsToList(List list) {
        list.clear();
        list.add(ENTRANCE);
        list.add(KA_NORD);
        list.add(KA_DURLACH);
        list.add(KA_MITTE);
        list.add(KA_DREIECK);
        list.add(ETTLINGEN);
    }
}