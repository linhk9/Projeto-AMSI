package pt.ipleiria.estg.dei.lojacalcado.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.modelo.Carrinho;

public interface CarrinhoListener {
     void onRefreshListaCarrinho(ArrayList<Carrinho> listaCarrinho);
}
