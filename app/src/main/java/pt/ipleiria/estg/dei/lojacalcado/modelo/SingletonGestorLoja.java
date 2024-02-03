package pt.ipleiria.estg.dei.lojacalcado.modelo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ipleiria.estg.dei.lojacalcado.LoginActivity;
import pt.ipleiria.estg.dei.lojacalcado.MenuMainActivity;
import pt.ipleiria.estg.dei.lojacalcado.PerfilActivity;
import pt.ipleiria.estg.dei.lojacalcado.RegistarActivity;
import pt.ipleiria.estg.dei.lojacalcado.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.lojacalcado.listeners.FaturasListener;
import pt.ipleiria.estg.dei.lojacalcado.listeners.ProdutosListener;
import pt.ipleiria.estg.dei.lojacalcado.listeners.UserListener;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class SingletonGestorLoja {
    private FaturaBDHelper faturaBDHelper;
    private static SingletonGestorLoja instance = null;
    private static RequestQueue volleyQueue = null;
    private String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    private String savedApiUrl;
    private UserListener userListener;
    private ProdutosListener produtosListener;
    private CarrinhoListener carrinhoListener;
    private FaturasListener faturasListener;
    public ArrayList<Produto> produtos;
    public ArrayList<Carrinho> carrinho;
    public ArrayList<Fatura> faturas;

    public static synchronized SingletonGestorLoja getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorLoja(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private SingletonGestorLoja(Context context) {
        SharedPreferences sharedPreferencesAPI = context.getSharedPreferences("API", Context.MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);

        // Produtos
        produtos = new ArrayList<>();

        // Carrinho
        carrinho = new ArrayList<>();

        // Faturas
        faturas = new ArrayList<>();
        faturaBDHelper = new FaturaBDHelper(context);
    }

    //region User
    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }

    public void login(String username, String password, LoginActivity activity) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, savedApiUrl + "/users/login", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (LojaJsonParser.parserJsonLogin(response)) {
                    Intent intent = new Intent(activity, MenuMainActivity.class);
                    activity.startActivity(intent);

                    String credenciais = username + ":" + password;
                    String auth = "Basic " + Base64.encodeToString(credenciais.getBytes(), Base64.NO_WRAP);

                    SharedPreferences sharedPreferencesUser = activity.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
                    editorUser.putBoolean("AUTENTICADO", true);
                    editorUser.putInt("ID_USER", response.optInt("id"));
                    editorUser.putInt("ID_USERDATA", response.optInt("id_userdata"));
                    editorUser.putString("API_AUTH", auth);
                    editorUser.apply();

                    Toast.makeText(activity, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
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

                    return mRequestBody.getBytes(StandardCharsets.UTF_8);
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
                Toast.makeText(activity, LojaJsonParser.parserJsonMessage(response), Toast.LENGTH_SHORT).show();
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

                    return mRequestBody.getBytes(StandardCharsets.UTF_8);
                } catch (JSONException e) {
                    Log.e("RegistarActivity", "Erro JSON: " + e.getMessage());
                    return null;
                }
            }
        };

        volleyQueue.add(req);
    }

    public void carregarPerfil(PerfilActivity activity) {
        SharedPreferences sharedPreferencesUser = activity.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        int savedUserId = sharedPreferencesUser.getInt("ID_USER", 0);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, savedApiUrl + "/users/" + savedUserId + "/userdata", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User user = LojaJsonParser.parserJsonUser(response);

                if (userListener != null)
                    userListener.onRefreshDados(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                if (errorMessage == null) {
                    errorMessage = "Erro ao carregar utilizador";
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

        volleyQueue.add(req);
    }

    public void editarPerfil(String email, String primeiroNome, String ultimoNome, String telemovel, String morada, PerfilActivity activity) {
        SharedPreferences sharedPreferencesUser = activity.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        int savedUserId = sharedPreferencesUser.getInt("ID_USER", 0);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, savedApiUrl + "/users/" + savedUserId + "/atualizar", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(activity, "Perfil editado com sucesso!", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                if (errorMessage == null) {
                    errorMessage = "Erro ao editar utilizador";
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
                    jsonBody.put("email", email);
                    jsonBody.put("primeiroNome", primeiroNome);
                    jsonBody.put("ultimoNome", ultimoNome);
                    jsonBody.put("telemovel", telemovel);
                    jsonBody.put("morada", morada);

                    final String mRequestBody = jsonBody.toString();

                    return mRequestBody.getBytes(StandardCharsets.UTF_8);
                } catch (JSONException e) {
                    Log.e("PerfilActivity", "Erro JSON: " + e.getMessage());
                    return null;
                }
            }
        };

        volleyQueue.add(req);
    }

    public void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    //endregion

    //region Produtos
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
                    String errorMessage = error.getMessage();
                    if (errorMessage != null) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                    }
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
                    String errorMessage = error.getMessage();
                    if (errorMessage != null) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                    }
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
    //endregion

    //region Carrinho
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
                String errorMessage = error.getMessage();
                if (errorMessage != null) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }
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
                String errorMessage = error.getMessage();
                if (errorMessage != null) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }
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
                String errorMessage = error.getMessage();
                if (errorMessage != null) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }
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

    public int getTotalProdutosCarrinho() {
        int totalProdutosCarrinho = 0;
        for (Carrinho c : carrinho) {
            for (CarrinhoLinha cl : c.getCarrinhoLinhas()) {
                totalProdutosCarrinho += cl.getQuantidade();
            }
        }
        return totalProdutosCarrinho;
    }
    //endregion

    //region Faturas
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

            faturas = (ArrayList<Fatura>) faturaBDHelper.getAllFaturasBD();

            if (faturasListener != null)
                faturasListener.onRefreshListaFaturas(faturas);

            return;
        }

        SharedPreferences sharedPreferencesUser = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        int savedUserdataId = sharedPreferencesUser.getInt("ID_USERDATA", 0);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, savedApiUrl + "/faturas/userdata/" + savedUserdataId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                faturas = LojaJsonParser.parserJsonFaturas(response);
                faturaBDHelper.removerAllFaturasBD();

                for (Fatura fatura : faturas) {
                    faturaBDHelper.adicionarFaturaBD(fatura);
                    for (FaturaLinha faturaLinha : fatura.getFaturaLinhas()) {
                        faturaBDHelper.adicionarFaturaLinhaBD(faturaLinha);
                    }
                }

                if (faturasListener != null)
                    faturasListener.onRefreshListaFaturas(faturas);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                if (errorMessage != null) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }
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

    public int getTotalFaturas() {
        return faturas.size();
    }
    //endregion
}
