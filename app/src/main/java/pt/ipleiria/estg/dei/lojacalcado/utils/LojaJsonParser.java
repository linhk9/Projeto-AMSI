package pt.ipleiria.estg.dei.lojacalcado.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.modelo.Produto;
import pt.ipleiria.estg.dei.lojacalcado.modelo.User;

public class LojaJsonParser {
    public static boolean parserJsonLogin(JSONObject response) {
        return response.has("username");
    }

    public static String parserJsonMessage(JSONObject response) {
        String msg = "";

        try {
            msg = response.getString("message");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return msg;
    }

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

    public static ArrayList<Produto> parserJsonProdutos(JSONArray response) {
        ArrayList<Produto> produtos = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject produto = (JSONObject) response.get(i);

                int id = produto.getInt("id");
                int id_categoria = produto.getInt("id_categoria");
                int stock = produto.getInt("stock");
                float preco = (float) produto.getDouble("preco");
                float preco_antigo = produto.has("preco_antigo") ? (float) produto.getDouble("preco_antigo") : 0.0f;
                String nome = produto.getString("nome");
                String categoria = produto.getString("categoria");
                String descricao = produto.getString("descricao");
                String imagem = produto.getString("imagem");
                String marca = produto.getString("marca");
                String tamanho = produto.getString("tamanho");
                String cores = produto.getString("cores");

                produtos.add(new Produto(id, id_categoria, stock, preco, preco_antigo, nome, categoria, descricao, imagem, marca, tamanho, cores));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public static Boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }
}
