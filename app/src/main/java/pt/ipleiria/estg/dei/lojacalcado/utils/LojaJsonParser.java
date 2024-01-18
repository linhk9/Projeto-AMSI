package pt.ipleiria.estg.dei.lojacalcado.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LojaJsonParser {
    public static boolean parserJsonLogin(JSONObject response) {
        if(response.has("username")) {
            return true;
        }

        return false;
    }

    public static String parserJsonRegisto(JSONObject response) {
        String msg = "";

        try {
            msg = response.getString("message");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return msg;
    }

    public static Boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }
}
