package pt.ipleiria.estg.dei.lojacalcado;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorProdutos;


public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        setHasOptionsMenu(true);

        // TODO: Carregar os dados com a API

        LinearLayout llContainer = new LinearLayout(container.getContext());
        llContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        llContainer.setOrientation(LinearLayout.VERTICAL);
        llContainer.setGravity(Gravity.CENTER);
        llContainer.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 250, 0, 0);


        TextView textView1 = new TextView(container.getContext());
        textView1.setLayoutParams(params);
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(25);
        textView1.setTypeface(null, Typeface.BOLD);
        textView1.setText("Total de Produtos no Carrinho");
        llContainer.addView(textView1);

        TextView ProdutosCarrinho = new TextView(container.getContext());
        ProdutosCarrinho.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ProdutosCarrinho.setTextColor(Color.BLACK);
        ProdutosCarrinho.setTextSize(25);
        ProdutosCarrinho.setText("15");
        llContainer.addView(ProdutosCarrinho);

        TextView textView2 = new TextView(container.getContext());
        textView2.setLayoutParams(params);
        textView2.setTextColor(Color.BLACK);
        textView2.setTextSize(25);
        textView2.setTypeface(null, Typeface.BOLD);
        textView2.setText("Total de Produtos na Loja" );
        llContainer.addView(textView2);

        TextView ProdutosLoja = new TextView(container.getContext());
        ProdutosLoja.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ProdutosLoja.setTextColor(Color.BLACK);
        ProdutosLoja.setTextSize(25);
        ProdutosLoja.setText("50");
        llContainer.addView(ProdutosLoja);

        TextView textView3 = new TextView(container.getContext());
        textView3.setLayoutParams(params);
        textView3.setTextColor(Color.BLACK);
        textView3.setTextSize(25);
        textView3.setTypeface(null, Typeface.BOLD);
        textView3.setText("Total de Faturas Emitidas" );
        llContainer.addView(textView3);

        TextView FaturasEmitidas = new TextView(container.getContext());
        FaturasEmitidas.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        FaturasEmitidas.setTextColor(Color.BLACK);
        FaturasEmitidas.setTextSize(25);
        FaturasEmitidas.setText("3");
        llContainer.addView(FaturasEmitidas);

        FrameLayout frameLayout = view.findViewById(R.id.dashboard_frame);
        frameLayout.addView(llContainer);

        return view;
    }
}