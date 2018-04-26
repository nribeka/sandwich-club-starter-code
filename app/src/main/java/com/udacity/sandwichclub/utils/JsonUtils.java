package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) throws JSONException {
        JSONObject jsonSandwich = new JSONObject(json);
        Sandwich sandwich = new Sandwich();

        JSONObject nameObject = jsonSandwich.getJSONObject("name");
        sandwich.setMainName(nameObject.getString("mainName"));
        sandwich.setAlsoKnownAs(convertToList(nameObject.getJSONArray("alsoKnownAs")));

        sandwich.setDescription(jsonSandwich.getString("description"));
        sandwich.setImage(jsonSandwich.getString("image"));
        sandwich.setIngredients(convertToList(jsonSandwich.getJSONArray("ingredients")));
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
