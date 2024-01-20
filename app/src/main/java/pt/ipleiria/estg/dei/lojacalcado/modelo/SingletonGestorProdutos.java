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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.lojacalcado.listeners.ProdutosListener;
import pt.ipleiria.estg.dei.lojacalcado.listeners.UserListener;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class SingletonGestorProdutos {
    public ArrayList<Produto> produtos;
    private static SingletonGestorProdutos instance = null;
    private static RequestQueue volleyQueue = null;
    private String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    private String savedApiUrl;
    private ProdutosListener produtosListener;


    public static synchronized  SingletonGestorProdutos getInstance(Context context)
    {
        if(instance == null) {
            instance = new SingletonGestorProdutos(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return  instance;
    }
    private  SingletonGestorProdutos(Context context)
    {
        SharedPreferences sharedPreferencesAPI = context.getSharedPreferences("API", Context.MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);
        produtos = new ArrayList<>();
    }

    public void setProdutosListener(ProdutosListener produtosListener) {
        this.produtosListener = produtosListener;
    }

    public Produto getProduto(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id)
                return produto;
        }
        return null;
    }

    public void adicionarProdutoCarrinho(Produto produto, final Context context) {
        if (!LojaJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferencesUser = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
            int savedApiUserdataId = sharedPreferencesUser.getInt("ID_USERDATA", 0);

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, savedApiUrl + "/carrinhos/linha/" + savedApiUserdataId + "/" + produto.getId(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, LojaJsonParser.parserJsonMessage(response), Toast.LENGTH_SHORT).show();
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

    public void getAllProdutos(final Context context) {
        if (!LojaJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, savedApiUrl + "/produtos/comnomecategoria", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    produtos = LojaJsonParser.parserJsonProdutos(response);

                    if (produtosListener != null)
                        produtosListener.onRefreshListaProdutos(produtos);
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

    public int getTotalProdutos() {
        return produtos.size();
    }
}
