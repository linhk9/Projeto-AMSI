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
import pt.ipleiria.estg.dei.lojacalcado.modelo.Fatura;

public class ListaFaturasAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Fatura> faturas;
    private ArrayList<Fatura> faturasOriginais;

    public ListaFaturasAdaptador(Context context, ArrayList<Fatura> faturas) {
        this.context = context;
        this.faturas = faturas;
        this.faturasOriginais = new ArrayList<>(faturas);
    }

    @Override
    public int getCount() {
        return faturas.size();
    }

    @Override
    public Object getItem(int i) {
        return faturas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return faturas.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.item_lista_fatura, null);

        // otimização
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(faturas.get(position));

        return view;
    }

    private class ViewHolderLista {
        private TextView tvId, tvDataDaCompra, tvTotalProdutosComprados, tvPrecoTotal;

        public ViewHolderLista(View view) {
            tvId = view.findViewById(R.id.tvId);
            tvDataDaCompra = view.findViewById(R.id.tvDataDaCompra);
            tvTotalProdutosComprados = view.findViewById(R.id.tvTotalProdutosComprados);
            tvPrecoTotal = view.findViewById(R.id.tvPrecoTotal);
        }

        public void update(Fatura fatura) {
            tvId.setText(String.valueOf(fatura.getId()));
            tvDataDaCompra.setText(fatura.getData());
            tvTotalProdutosComprados.setText("por definir");
            tvPrecoTotal.setText("por definir");
            tvTotalProdutosComprados.setText(String.valueOf(fatura.getTotalProdutosComprados()));
            tvPrecoTotal.setText(String.format("%.2f €", fatura.getPrecoTotal()));
        }
    }

    public void filtrar(String query) {
        if (query.isEmpty()) {
            this.faturas = new ArrayList<>(faturasOriginais);
        } else {
            ArrayList<Fatura> faturasFiltradas = new ArrayList<>();

            for (Fatura fatura : faturasOriginais) {
                if (fatura.getData().toLowerCase().contains(query.toLowerCase())) {
                    faturasFiltradas.add(fatura);
                }
            }

            this.faturas = faturasFiltradas;
        }

        notifyDataSetChanged();
    }
}
