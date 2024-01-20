package pt.ipleiria.estg.dei.lojacalcado.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.modelo.Carrinho;

public class CarrinhoJsonParser {
    public static ArrayList<Carrinho> parserJsonCarrinho(JSONArray response) {
        ArrayList<Carrinho> carrinhos = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject carrinhoJson = (JSONObject) response.get(i);

                int id = carrinhoJson.getInt("id");
                int id_userdata = carrinhoJson.getInt("id_userdata");
                String data = carrinhoJson.getString("data");

                ArrayList<Carrinho.CarrinhoLinha> carrinhoLinhas = new ArrayList<>();
                JSONArray carrinhoLinhasJson = carrinhoJson.getJSONArray("carrinhoLinhas");
                for (int j = 0; j < carrinhoLinhasJson.length(); j++) {
                    JSONObject carrinhoLinhaJson = carrinhoLinhasJson.getJSONObject(j);

                    int idLinha = carrinhoLinhaJson.getInt("id");
                    int id_carrinho = carrinhoLinhaJson.getInt("id_carrinho");
                    int id_produto = carrinhoLinhaJson.getInt("id_produto");
                    int quantidade = carrinhoLinhaJson.getInt("quantidade");
                    float preco = (float) carrinhoLinhaJson.getDouble("preco");

                    carrinhoLinhas.add(new Carrinho.CarrinhoLinha(idLinha, id_carrinho, id_produto, quantidade, preco));
                }

                Carrinho carrinho = new Carrinho(id, id_userdata, data);
                carrinho.carrinhoLinhas = carrinhoLinhas;
                carrinhos.add(carrinho);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return carrinhos;
    }

    public static Boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }
}