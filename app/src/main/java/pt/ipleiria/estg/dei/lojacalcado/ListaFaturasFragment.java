package pt.ipleiria.estg.dei.lojacalcado;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.adaptadores.ListaFaturasAdaptador;
import pt.ipleiria.estg.dei.lojacalcado.adaptadores.ListaProdutosAdaptador;
import pt.ipleiria.estg.dei.lojacalcado.listeners.FaturasListener;
import pt.ipleiria.estg.dei.lojacalcado.modelo.Fatura;
import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorFaturas;

public class ListaFaturasFragment extends Fragment implements FaturasListener {
    private ListView lvFaturas;
    private ArrayList<Fatura> faturas;
    private SearchView searchView;


    public ListaFaturasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_faturas, container, false);

        setHasOptionsMenu(true);

        lvFaturas = view.findViewById(R.id.lvFaturas);
        SingletonGestorFaturas.getInstance(getContext()).setFaturasListener(this);
        SingletonGestorFaturas.getInstance(getContext()).getAllFaturas(getContext());

        // Click num item da lista
        lvFaturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Intent intent = new Intent(getContext(), DetalhesProdutoActivity.class);
//                intent.putExtra(DetalhesProdutoActivity.ID_Fatura, (int) id);
//                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);

        MenuItem itemPesquisa = menu.findItem(R.id.itemPesquisa);
        searchView = (SearchView) itemPesquisa.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((ListaFaturasAdaptador) lvFaturas.getAdapter()).filtrar(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((ListaFaturasAdaptador) lvFaturas.getAdapter()).filtrar(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefreshListaFaturas(ArrayList<Fatura> listaFaturas) {
        if (listaFaturas != null)
            lvFaturas.setAdapter(new ListaFaturasAdaptador(getContext(), listaFaturas));
    }
}