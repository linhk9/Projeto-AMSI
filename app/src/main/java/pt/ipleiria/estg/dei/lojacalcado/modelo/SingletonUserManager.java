package pt.ipleiria.estg.dei.lojacalcado.modelo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.lojacalcado.LoginActivity;
import pt.ipleiria.estg.dei.lojacalcado.MenuMainActivity;
import pt.ipleiria.estg.dei.lojacalcado.RegistarActivity;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class SingletonUserManager {
    private static SingletonUserManager instance = null;
    private static RequestQueue volleyQueue = null;
    private String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    private String savedApiUrl;

    private SingletonUserManager(Context context) {
        volleyQueue = Volley.newRequestQueue(context);
        SharedPreferences sharedPreferencesAPI = context.getSharedPreferences("API_URL", Context.MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);
    }

    public static synchronized SingletonUserManager getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonUserManager(context);
        }
        return instance;
    }

    public void login(String username, String password, LoginActivity activity) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, savedApiUrl + "/users/login", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (LojaJsonParser.parserJsonLogin(response)) {
                    Toast.makeText(activity, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, MenuMainActivity.class);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                if (errorMessage == null) {
                    errorMessage = "Erro ao fazer login";
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("username", username);
                    jsonBody.put("password", password);

                    final String mRequestBody = jsonBody.toString();

                    return mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("LoginActivity", "Encoding não suportado: " + e.getMessage());
                    return null;
                } catch (JSONException e) {
                    Log.e("LoginActivity", "Erro JSON: " + e.getMessage());
                    return null;
                }
            }
        };

        volleyQueue.add(req);
    }

    public void registo(String username, String password, String email, String primeiroNome, String ultimoNome, String telemovel, String morada, RegistarActivity activity) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, savedApiUrl + "/users/registo", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(activity, LojaJsonParser.parserJsonRegisto(response), Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                if (errorMessage == null) {
                    errorMessage = "Erro ao registar utilizador";
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("username", username);
                    jsonBody.put("password", password);
                    jsonBody.put("email", email);
                    jsonBody.put("primeiroNome", primeiroNome);
                    jsonBody.put("ultimoNome", ultimoNome);
                    jsonBody.put("telemovel", telemovel);
                    jsonBody.put("morada", morada);

                    final String mRequestBody = jsonBody.toString();

                    return mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("RegistarActivity", "Encoding não suportado: " + e.getMessage());
                    return null;
                } catch (JSONException e) {
                    Log.e("RegistarActivity", "Erro JSON: " + e.getMessage());
                    return null;
                }
            }
        };

        volleyQueue.add(req);
    }
}
