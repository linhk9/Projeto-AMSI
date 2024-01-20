package pt.ipleiria.estg.dei.lojacalcado;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.adaptadores.ListaCarrinhoAdaptador;
import pt.ipleiria.estg.dei.lojacalcado.adaptadores.ListaFaturasAdaptador;
import pt.ipleiria.estg.dei.lojacalcado.listeners.CarrinhoListener;
import pt.ipleiria.estg.dei.lojacalcado.listeners.FaturasListener;
import pt.ipleiria.estg.dei.lojacalcado.modelo.Carrinho;
import pt.ipleiria.estg.dei.lojacalcado.modelo.CarrinhoLinha;
import pt.ipleiria.estg.dei.lojacalcado.modelo.Fatura;
import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorCarrinho;
import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorFaturas;
import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorProdutos;

public class ListaCarrinhoFragment extends Fragment implements CarrinhoListener {
    private ListView lvCarrinho;
    private ArrayList<Carrinho> carrinho;
    private SearchView searchView;
    private FloatingActionButton fabCheckout;

    public ListaCarrinhoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_carrinho, container, false);

        setHasOptionsMenu(true);

        lvCarrinho = view.findViewById(R.id.lvCarrinho);
        SingletonGestorCarrinho.getInstance(getContext()).setCarrinhoListener(this);
        SingletonGestorCarrinho.getInstance(getContext()).getCarrinho(getContext());

        // Click num item da lista
        lvCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dialogRemover(getContext(), id);
            }
        });

        fabCheckout = view.findViewById(R.id.fabCheckout);
        fabCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingletonGestorCarrinho.getInstance(getContext()).checkout(getContext());
            }
        });

        return view;
    }

    private void dialogRemover(Context context, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Remover Produto do carrinho")
                .setMessage("Tem a certeza que pretendes remover o produto?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SingletonGestorCarrinho.getInstance(context).removerProduto(context, id);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    @Override
    public void onRefreshListaCarrinho(ArrayList<Carrinho> listaCarrinho) {
        if (lvCarrinho != null) {
            ArrayList<CarrinhoLinha> carrinhoLinhas = SingletonGestorCarrinho.getInstance(getContext()).getCarrinhoLinhas(listaCarrinho);
            lvCarrinho.setAdapter(new ListaCarrinhoAdaptador(getContext(), carrinhoLinhas));

            TextView tvTotal = getView().findViewById(R.id.tvTotal);

            double precoTotal = 0;
            for (CarrinhoLinha carrinhoLinha : carrinhoLinhas) {
                precoTotal += carrinhoLinha.getPreco() * carrinhoLinha.getQuantidade();
            }

            tvTotal.setText(String.format("Total: %.2f €", precoTotal));
        }
    }
}