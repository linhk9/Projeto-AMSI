package pt.ipleiria.estg.dei.lojacalcado.modelo;

import android.content.Context;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ipleiria.estg.dei.lojacalcado.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class SingletonGestorCarrinho {
    public ArrayList<Carrinho> carrinho;
    private static SingletonGestorCarrinho instance = null;
    private static RequestQueue volleyQueue = null;
    private String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    private String savedApiUrl;
    private CarrinhoListener carrinhoListener;

    public static synchronized SingletonGestorCarrinho getInstance(Context context)
    {
        if(instance == null) {
            instance = new SingletonGestorCarrinho(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }
    private SingletonGestorCarrinho(Context context)
    {
        SharedPreferences sharedPreferencesAPI = context.getSharedPreferences("API", Context.MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);
        carrinho = new ArrayList<>();
    }

    public void setCarrinhoListener(CarrinhoListener carrinhoListener) {
        this.carrinhoListener = carrinhoListener;
    }

    public Carrinho getCarrinhoId(int id) {
        for (Carrinho c : carrinho) {
            if (c.getId() == id)
                return c;
        }
        return null;
    }

    public ArrayList<CarrinhoLinha> getCarrinhoLinhas(ArrayList<Carrinho> listaCarrinho) {
        ArrayList<CarrinhoLinha> carrinhoLinhas = new ArrayList<>();
        for (Carrinho carrinho : listaCarrinho) {
            carrinhoLinhas.addAll(carrinho.getCarrinhoLinhas());
        }
        return carrinhoLinhas;
    }

    public void getCarrinho(final Context context) {
        if (!LojaJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferencesUser = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        int savedUserdataId = sharedPreferencesUser.getInt("ID_USERDATA", 0);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, savedApiUrl + "/carrinhos/userdata/" + savedUserdataId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                carrinho = LojaJsonParser.parserJsonCarrinho(response);

                if (carrinhoListener != null)
                    carrinhoListener.onRefreshListaCarrinho(carrinho);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    public void removerProduto(Context context, final long id) {
        if (!LojaJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, savedApiUrl + "/carrinhos/linha/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Produto removido do carrinho com sucesso", Toast.LENGTH_SHORT).show();
                for (Carrinho c : carrinho) {
                    List<CarrinhoLinha> carrinhoLinhas = c.getCarrinhoLinhas();
                    for (int i = 0; i < carrinhoLinhas.size(); i++) {
                        if (carrinhoLinhas.get(i).getId() == id) {
                            carrinhoLinhas.remove(i);
                            break;
                        }
                    }
                }
                // Notify the adapter about the data change
                if (carrinhoListener != null)
                    carrinhoListener.onRefreshListaCarrinho(carrinho);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    public void checkout(Context context) {
        if (!LojaJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferencesUser = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        int savedUserdataId = sharedPreferencesUser.getInt("ID_USERDATA", 0);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, savedApiUrl + "/carrinhos/checkout/" + savedUserdataId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Checkout efetuado com sucesso", Toast.LENGTH_SHORT).show();
                carrinho.clear();
                if (carrinhoListener != null)
                    carrinhoListener.onRefreshListaCarrinho(carrinho);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
