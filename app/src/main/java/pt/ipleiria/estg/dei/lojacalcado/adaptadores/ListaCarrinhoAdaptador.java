package pt.ipleiria.estg.dei.lojacalcado.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.R;
import pt.ipleiria.estg.dei.lojacalcado.modelo.CarrinhoLinha;
import pt.ipleiria.estg.dei.lojacalcado.modelo.Fatura;

public class ListaCarrinhoAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CarrinhoLinha> carrinhoLinhas;

    public ListaCarrinhoAdaptador(Context context, ArrayList<CarrinhoLinha> carrinhoLinhas) {
        this.context = context;
        this.carrinhoLinhas = carrinhoLinhas;
    }

    @Override
    public int getCount() {
        return carrinhoLinhas.size();
    }

    @Override
    public Object getItem(int i) {
        return carrinhoLinhas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return carrinhoLinhas.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.item_lista_carrinho, null);

        // otimização
        ListaCarrinhoAdaptador.ViewHolderLista viewHolder = (ListaCarrinhoAdaptador.ViewHolderLista) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ListaCarrinhoAdaptador.ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(carrinhoLinhas.get(position));

        return view;
    }

    private class ViewHolderLista {
        private TextView tvIdProduto, tvQuantidade, tvPreco;

        public ViewHolderLista(View view) {
            tvIdProduto = view.findViewById(R.id.tvIdProduto);
            tvQuantidade = view.findViewById(R.id.tvQuantidade);
            tvPreco = view.findViewById(R.id.tvPreco);
        }

        public void update(CarrinhoLinha carrinhoLinhas) {
            tvIdProduto.setText(carrinhoLinhas.getId_produto() + "");
            tvQuantidade.setText(carrinhoLinhas.getQuantidade() + "");
            tvPreco.setText(String.format("%.2f €", carrinhoLinhas.getPreco()));
        }
    }
}