package test.com.potherdabai.potherdabimain;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsJsonParser {
    public List<List<List<HashMap<String, String>>>> parse(JSONObject jObject) {
        List<List<List<HashMap<String, String>>>> routes1 = new ArrayList();
        try {
            JSONArray jRoutes = jObject.getJSONArray("routes");
            for (int i = 0; i < jRoutes.length(); i++) {
                JSONArray jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList();
                List path1 = new ArrayList();
                for (int j = 0; j < jLegs.length(); j++) {
                    JSONArray jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        List<LatLng> list = decodePoly((String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points"));
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    path1.add(path);
                }
                routes1.add(path1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
        }
        return routes1;
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList();
        int index = 0;
        int len = encoded.length();
        int lat = 0;
        int lng = 0;
        while (index < len) {
            int index2;
            int shift = 0;
            int result = 0;
            while (true) {
                index2 = index + 1;
                int b = encoded.charAt(index) - 63;
                result |= (b & 31) << shift;
                shift += 5;
                if (b < 32) {
                    break;
                }
                index = index2;
            }
            lat += (result & 1) != 0 ? (result >> 1) ^ -1 : result >> 1;
            shift = 0;
            result = 0;
            index = index2;
            while (true) {
                index2 = index + 1;
                int b = encoded.charAt(index) - 63;
                result |= (b & 31) << shift;
                shift += 5;
                if (b < 32) {
                    break;
                }
                index = index2;
            }
            lng += (result & 1) != 0 ? (result >> 1) ^ -1 : result >> 1;
            poly.add(new LatLng(((double) lat) / 100000.0d, ((double) lng) / 100000.0d));
            index = index2;
        }
        return poly;
    }
}

