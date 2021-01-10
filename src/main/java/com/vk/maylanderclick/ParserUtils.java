package com.vk.maylanderclick;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class ParserUtils {

    private ParserUtils(){}

    public static String getGifURL(String jsonStr) throws ParseException {
        return ((JSONObject)
                ((JSONObject)
                        ((JSONObject)
                                ((JSONArray)
                                        ((JSONObject)new JSONParser().parse(jsonStr))
                                                .get("data"))
                                        .get(0))
                                .get("images"))
                        .get("original"))
                .get("url").toString();
    }

    public static Double getRate(String json, String currencyId) throws ParseException {
        JSONObject obj = (JSONObject) new JSONParser().parse(json);
        return (Double) ((JSONObject) obj.get("rates")).get(currencyId);
    }

}
