/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tracker.jam.app.dhbw.com.jamtrackerapp;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Constants used in this sample.
 */

final class Constants {

    private Constants() {
    }

    //Locations
    public static final String DHBW = "DHBW";
    public static final String HOME = "HOME";
    public static final String BHF = "BHF";
    public static final String HOME_ACHERN = "HOME_ACHERN";

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
