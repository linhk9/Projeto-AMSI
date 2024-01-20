package pt.ipleiria.estg.dei.lojacalcado.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.lojacalcado.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.lojacalcado.utils.CarrinhoJsonParser;

public class SingletonGestorCarrinho {

    public ArrayList<Carrinho> carrinho;
    private static SingletonGestorCarrinho instance = null;
    private static com.android.volley.RequestQueue volleyQueue = null;
    private String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    private String savedApiUrl;

    public static synchronized  SingletonGestorCarrinho getInstance(Context context)
    {
        if(instance == null){
            instance = new SingletonGestorCarrinho(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return  instance;
    }
    private SingletonGestorCarrinho(Context context)
    {
        SharedPreferences sharedPreferencesAPI = context.getSharedPreferences("API", Context.MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);
        carrinho = new ArrayList<>();
    }

    public Carrinho getCarrinho(int id) {
        for (Carrinho carrinho : carrinho) {
            if (carrinho.getId() == id)
                return carrinho;
        }
        return null;
    }

    public void getAllCarrinho(final Context context) {
        if (!CarrinhoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, savedApiUrl + "/carrinho/userdata", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    carrinho = CarrinhoJsonParser.parserJsonCarrinho(response);

                    if (CarrinhoListener != null)
                        CarrinhoListener.onRefreshListaCarrinho(carrinho);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void removerCarrinhoLinha(Carrinho carrinhoLinha) {
        this.carrinho.remove(carrinhoLinha);
    }



}
