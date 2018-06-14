package test.com.potherdabai.potherdabimain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlacesMap = new HashMap();
        String place_name = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        try {
            if (!googlePlaceJson.isNull("name")) {
                place_name = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = googlePlaceJson.getString("reference");
                googlePlacesMap.put("place_name", place_name);
                googlePlacesMap.put("vicinity", vicinity);
                googlePlacesMap.put("lat", latitude);
                googlePlacesMap.put("lng", longitude);
                googlePlacesMap.put("reference", reference);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlacesMap;
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int count = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList();
        int i = 0;
        while (i < count) {
            try {
                placesList.add(getPlace((JSONObject) jsonArray.get(i)));
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONObject(jsonData).getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}

