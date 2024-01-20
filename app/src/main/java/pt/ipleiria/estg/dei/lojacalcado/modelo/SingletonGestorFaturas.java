package pt.ipleiria.estg.dei.lojacalcado.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.lojacalcado.listeners.FaturasListener;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class SingletonGestorFaturas {
    public ArrayList<Fatura> faturas;
    private static SingletonGestorFaturas instance = null;
    private static RequestQueue volleyQueue = null;
    private String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    private String savedApiUrl;
    private FaturasListener faturasListener;


    public static synchronized SingletonGestorFaturas getInstance(Context context)
    {
        if(instance == null) {
            instance = new SingletonGestorFaturas(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }
    private SingletonGestorFaturas(Context context)
    {
        SharedPreferences sharedPreferencesAPI = context.getSharedPreferences("API", Context.MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);
        faturas = new ArrayList<>();
    }

    public void setFaturasListener(FaturasListener faturasListener) {
        this.faturasListener = faturasListener;
    }

    public Fatura getFatura(int id) {
        for (Fatura fatura : faturas) {
            if (fatura.getId() == id)
                return fatura;
        }
        return null;
    }

    public void getAllFaturas(final Context context) {
        if (!LojaJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferencesUser = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        int savedUserdataId = sharedPreferencesUser.getInt("ID_USERDATA", 0);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, savedApiUrl + "/faturas/userdata/" + savedUserdataId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                faturas = LojaJsonParser.parserJsonFaturas(response);

                if (faturasListener != null)
                    faturasListener.onRefreshListaFaturas(faturas);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                SharedPreferences sharedPreferencesAPI = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
                String savedApiAuth = sharedPreferencesAPI.getString("API_AUTH", "");

                headers.put("Authorization", savedApiAuth);

                return headers;
            }
        };

        volleyQueue.add(req);
    }
}
