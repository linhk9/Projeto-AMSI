package pt.ipleiria.estg.dei.lojacalcado.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.modelo.User;

public class LojaJsonParser {
    public static User parserJsonUser(JSONObject response) {
        User user = null;

        try {
            int id = response.getInt("id");
            int telemovel = response.getInt("telemovel");
            String username = response.getString("username");
            String email = response.getString("email");
            String primeiroNome = response.getString("primeiroNome");
            String ultimoNome = response.getString("ultimoNome");
            String morada = response.getString("morada");

            user = new User(id, username, email, primeiroNome, ultimoNome, telemovel, morada);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static boolean parserJsonLogin(JSONObject response) {
        return response.has("username");
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
