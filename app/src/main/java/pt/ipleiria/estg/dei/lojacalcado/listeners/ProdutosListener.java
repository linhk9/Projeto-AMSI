package pt.ipleiria.estg.dei.lojacalcado.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.modelo.Produto;

public interface ProdutosListener {
    void onRefreshListaProdutos(ArrayList<Produto> listaProdutos);
}
