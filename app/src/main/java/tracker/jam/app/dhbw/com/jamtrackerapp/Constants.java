package tracker.jam.app.dhbw.com.jamtrackerapp;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

final class Constants {

    //Geofencing
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = Geofence.NEVER_EXPIRE;
    public static final float GEOFENCE_RADIUS_750_METERS = 750;
    public static final float GEOFENCE_RADIUS_200_METERS = 200;

    //Checkpoints
    public static Checkpoint ENTRANCE = new Checkpoint("CP Startpunkt", new LatLng(49.118115, 8.551332), GEOFENCE_RADIUS_200_METERS, false, JamLevel.NOCAM);
    public static Checkpoint KA_NORD = new Checkpoint("Karlsruhe Nord", new LatLng(49.019773, 8.470468), GEOFENCE_RADIUS_750_METERS, true, JamLevel.UNDEFINED);
    public static Checkpoint KA_DURLACH = new Checkpoint(" Karlsruhe Durlach", new LatLng(49.005600, 8.454503), GEOFENCE_RADIUS_750_METERS, false, JamLevel.NOCAM);
    public static Checkpoint KA_MITTE = new Checkpoint("Karlsruhe Mitte", new LatLng(48.993609, 8.437117), GEOFENCE_RADIUS_750_METERS, true, JamLevel.UNDEFINED);
    public static Checkpoint KA_DREIECK = new Checkpoint("Dreieck Karlsruhe", new LatLng(48.979249, 8.436886), GEOFENCE_RADIUS_750_METERS, false, JamLevel.NOCAM);
    public static Checkpoint ETTLINGEN = new Checkpoint("Ettlingen", new LatLng(48.961417, 8.409667), GEOFENCE_RADIUS_750_METERS, true, JamLevel.UNDEFINED);

    //Density
    public static final String SERVER_URL_DENSITY = "http://my-json-server.typicode.com/crisvnait/jsondummy/test";

    //Maps
    public static LatLngBounds MAPS_LATLNG_BOUNDS = new LatLngBounds(Constants.ETTLINGEN.getLatLng(), Constants.KA_NORD.getLatLng());

    //Lists
    public static List<Checkpoint> EDITABLE_CHECKPOINT_LIST = new LinkedList<>();
    public static List<Checkpoint> UNCHANGEABLE_CHECKPOINT_LIST = new LinkedList<>();
    public static HashMap<String, LatLng> GEOFENCE_MAP = new HashMap<>();

    private Constants() {
    }

    static {
        ETTLINGEN.setLastExit(true);
        populateEditableCheckpointList();
        populateUnchangeableCheckpointList();
        populateGeofenceMap();
    }

    public static void populateEditableCheckpointList() {
        addCheckpointItemsToList(EDITABLE_CHECKPOINT_LIST);
    }

    private static void populateUnchangeableCheckpointList() {
        addCheckpointItemsToList(UNCHANGEABLE_CHECKPOINT_LIST);
    }

    private static void addCheckpointItemsToList(List list) {
        list.clear();
        list.add(ENTRANCE);
        list.add(KA_NORD);
        list.add(KA_DURLACH);
        list.add(KA_MITTE);
        list.add(KA_DREIECK);
        list.add(ETTLINGEN);
    }

    private static void populateGeofenceMap() {
        GEOFENCE_MAP.clear();
        GEOFENCE_MAP.put("Geo Nord-West", new LatLng(49.017426, 8.462032));
        GEOFENCE_MAP.put("Geo Nord-Ost", new LatLng(49.014437, 8.470862));
        GEOFENCE_MAP.put("Geo Durlach-West", new LatLng(49.003622, 8.446075));
        GEOFENCE_MAP.put("Geo Durlach-Ost", new LatLng(49.001944, 8.455401));
        GEOFENCE_MAP.put("Geo Mitte", new LatLng(48.992335, 8.432825));
        GEOFENCE_MAP.put("Geo Dreieck", new LatLng(48.972017, 8.440769));
        GEOFENCE_MAP.put("Geo Ettlingen", new LatLng(48.961821, 8.405975));
    }
}