package pt.ipleiria.estg.dei.lojacalcado.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.lojacalcado.R;
import pt.ipleiria.estg.dei.lojacalcado.modelo.Produto;

public class ListaProdutosAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Produto> produtos;

    public ListaProdutosAdaptador(Context context, ArrayList<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int i) {
        return produtos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return produtos.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.item_lista_produto, null);

        // otimização
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(produtos.get(position));

        return view;
    }

    private class ViewHolderLista {
        private TextView tvNome, tvCategoria, tvMarca, tvPreco;
        private ImageView imgCapa;

        public ViewHolderLista(View view) {
            tvNome = view.findViewById(R.id.tvNome);
            tvCategoria = view.findViewById(R.id.tvCategoria);
            tvMarca = view.findViewById(R.id.tvMarca);
            tvPreco = view.findViewById(R.id.tvPreco);
            imgCapa = view.findViewById(R.id.imgCapa);
        }

        public void update(Produto produto) {
            tvNome.setText(produto.getNome());
            tvCategoria.setText(produto.getCategoria());
            tvMarca.setText(produto.getMarca());
            if (produto.getPreco_antigo() == 0.0f)
                tvPreco.setText(String.format("%.2f €", produto.getPreco()));
            else
                tvPreco.setText(String.format("%.2f € (Antes: %.2f €)", produto.getPreco(), produto.getPreco_antigo()));
            Glide.with(context)
                    .load(produto.getImagem())
                    .placeholder(R.drawable.logo_produto)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
        }
    }
}
