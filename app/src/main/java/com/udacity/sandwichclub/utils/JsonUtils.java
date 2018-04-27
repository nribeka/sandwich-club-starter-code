package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String IMAGE_PROPERTY = "image";

    private static final String NAME_PROPERTY = "name";
    private static final String MAIN_NAME_PROPERTY = "mainName";
    private static final String ALSO_KNOWN_AS_PROPERTY = "alsoKnownAs";
    
    private static final String DESCRIPTION_PROPERTY = "description";
    private static final String PLACE_OF_ORIGIN_PROPERTY = "placeOfOrigin";
    private static final String INGREDIENTS_PROPERTY = "ingredients";

    public static Sandwich parseSandwichJson(String json) throws JSONException {
        Sandwich sandwich = new Sandwich();
        JSONObject jsonSandwich = new JSONObject(json);
        sandwich.setImage(jsonSandwich.getString(IMAGE_PROPERTY));

        JSONObject nameObject = jsonSandwich.getJSONObject(NAME_PROPERTY);
        sandwich.setMainName(nameObject.getString(MAIN_NAME_PROPERTY));
        sandwich.setAlsoKnownAs(convertToList(nameObject.getJSONArray(ALSO_KNOWN_AS_PROPERTY)));

        sandwich.setDescription(jsonSandwich.getString(DESCRIPTION_PROPERTY));
        sandwich.setPlaceOfOrigin(jsonSandwich.getString(PLACE_OF_ORIGIN_PROPERTY));
        sandwich.setIngredients(convertToList(jsonSandwich.getJSONArray(INGREDIENTS_PROPERTY)));
        return sandwich;
    }

    private static List<String> convertToList(JSONArray array) throws JSONException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }
}
