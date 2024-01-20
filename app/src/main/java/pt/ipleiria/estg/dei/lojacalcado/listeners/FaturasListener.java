package pt.ipleiria.estg.dei.lojacalcado.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.modelo.Fatura;

public interface FaturasListener {
    void onRefreshListaFaturas(ArrayList<Fatura> listaFaturas);
}
