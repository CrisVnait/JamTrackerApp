package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeofenceTransitionsJobIntentService extends JobIntentService {

    private static final String CHANNEL_ID = "channel_01";
    private static final int JOB_ID = 573;
    private TextToSpeech textToSpeech;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, GeofenceTransitionsJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            speak("Fehler Geofencing");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER &&
                Constants.EDITABLE_CHECKPOINT_LIST.get(0) != null) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            Checkpoint checkpoint = Constants.EDITABLE_CHECKPOINT_LIST.get(0);

            for (Geofence geofence : triggeringGeofences) {
                if (geofence.getRequestId().equals(checkpoint.getName())) {
                    // Get the transition details as a String.
                    String triggeringGeofencesId = getTriggeringGeofencesIdsString(geofenceTransition, triggeringGeofences);
                    // Send notification and log the transition details.
                    String exitSuggestion = MainActivity.exitSuggestion.getName();
                    if (triggeringGeofencesId.equals(exitSuggestion)) {
                        speak("Empfohlen wird, die Autobahn an der nächsten Ausfahrt " + triggeringGeofencesId + " zu verlassen.");
                    } else if (triggeringGeofencesId.equals(Constants.ENTRANCE.getName())) {
                        speak("Empfohlen wird, die Autobahn an der Ausfahrt " + exitSuggestion + " zu verlassen.");
                    } else {
                        speak("Bleiben Sie an der nächsten Ausfahrt auf der Autobahn. Empfohlen wird, die Autobahn an der Ausfahrt " + exitSuggestion + " zu verlassen.");
                    }
                    Constants.EDITABLE_CHECKPOINT_LIST.remove(0);
                } else {
                    for (String key : Constants.GEOFENCE_MAP.keySet()) {
                        if (geofence.getRequestId().equals(key)) {
                            Constants.populateEditableCheckpointList();
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition  The ID of the geofence transition.
     * @param triggeringGeofences The geofence(s) triggered.
     * @return The transition details formatted as String.
     */
    private String getTriggeringGeofencesIdsString(int geofenceTransition, List<Geofence> triggeringGeofences) {

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return triggeringGeofencesIdsString;
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType A transition type constant defined in Geofence
     * @return A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }

    private void speak(final String toSay) {

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.GERMAN);
                    textToSpeech.speak(toSay, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

}
