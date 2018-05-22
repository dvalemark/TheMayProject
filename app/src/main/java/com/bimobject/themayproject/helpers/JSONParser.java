package com.bimobject.themayproject.helpers;

import com.bimobject.themayproject.dto.Product;
import com.bimobject.themayproject.dto.ProductDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//TODO: Implement better solution for parsing generic element
public abstract class JSONParser {

    private static Gson gson = new GsonBuilder().create();

    public static ArrayList<Product> parseToProductList(JSONObject response) throws JSONException{
            JSONArray data = (JSONArray) response.get("data");
            Type listType = new TypeToken<List<Product>>(){}.getType();
            return gson.fromJson(data.toString(), listType);
    }

    public static ProductDetails parseToProductDetails(JSONObject response) throws JSONException{
            JSONObject data = (JSONObject) response.get("data");
            return gson.fromJson(data.toString(), ProductDetails.class);
    }
}