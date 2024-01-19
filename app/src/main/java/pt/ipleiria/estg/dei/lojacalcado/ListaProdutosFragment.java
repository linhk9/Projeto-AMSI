package pt.ipleiria.estg.dei.lojacalcado;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

import pt.ipleiria.estg.dei.lojacalcado.adaptadores.ListaProdutosAdaptador;
import pt.ipleiria.estg.dei.lojacalcado.listeners.ProdutosListener;
import pt.ipleiria.estg.dei.lojacalcado.modelo.Produto;
import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorProdutos;

public class ListaProdutosFragment extends Fragment implements ProdutosListener {
    private ListView lvProdutos;
    private ArrayList<Produto> produtos;
    private SearchView searchView;

    public ListaProdutosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_produtos, container, false);

        setHasOptionsMenu(true);

        lvProdutos = view.findViewById(R.id.lvProdutos);
        SingletonGestorProdutos.getInstance(getContext()).setProdutosListener(this);
        SingletonGestorProdutos.getInstance(getContext()).getAllProdutos(getContext());

        // Click num item da lista
        lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesProdutoActivity.class);
                intent.putExtra(DetalhesProdutoActivity.ID_Produto, (int) id);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);

        // TODO: Arranjar a pesquisa, está a crashar a app

        MenuItem itemPesquisa = menu.findItem(R.id.itemPesquisa);
        searchView = (SearchView) itemPesquisa.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Quando o utilizador submete a pesquisa
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Produto> tempListaProdutos = new ArrayList<>();
                for (Produto p : produtos) {
                    if (p.getNome().toLowerCase().contains(newText.toLowerCase())) {
                        tempListaProdutos.add(p);
                    }
                }
                lvProdutos.setAdapter(new ListaProdutosAdaptador(getContext(), tempListaProdutos));
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefreshListaProdutos(ArrayList<Produto> listaProdutos) {
        if (listaProdutos != null)
            lvProdutos.setAdapter(new ListaProdutosAdaptador(getContext(), listaProdutos));
    }
}