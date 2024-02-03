package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.ipleiria.estg.dei.lojacalcado.modelo.Produto;
import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorLoja;

public class DetalhesProdutoActivity extends AppCompatActivity {
    public static final String ID_Produto = "ID_Produto";
    private TextView etNome, etPreco, etDescricao, etCategoria, etMarca, etTamanho, etCores, etStock;
    private FloatingActionButton fabAdicionar;
    private ImageView imgCapa;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        etNome = findViewById(R.id.etNome);
        etPreco = findViewById(R.id.etPreco);
        etDescricao = findViewById(R.id.etDescricao);
        etCategoria = findViewById(R.id.etCategoria);
        etMarca = findViewById(R.id.etMarca);
        etTamanho = findViewById(R.id.etTamanho);
        etCores = findViewById(R.id.etCores);
        etStock = findViewById(R.id.etStock);
        imgCapa = findViewById(R.id.imgCapa);
        fabAdicionar = findViewById(R.id.fabAdicionar);

        int id = getIntent().getIntExtra(ID_Produto, 0);
        if (id != 0) {
            produto = SingletonGestorLoja.getInstance(getApplicationContext()).getProduto(id);
            if (produto != null) {
                carregarInfoProduto();
                fabAdicionar.setImageResource(R.drawable.ic_action_adicionar);
            } else
                finish();
        }

        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (produto != null) {
                    SingletonGestorLoja.getInstance(getApplicationContext()).adicionarProdutoCarrinho(produto, getApplicationContext());
                }
            }
        });
    }

    private void carregarInfoProduto() {
        setTitle("Detalhes: " + produto.getNome());

        etNome.setText("Nome: " + produto.getNome());
        etDescricao.setText(produto.getDescricao());
        etStock.setText("Stock: " + produto.getStock() + "");
        etCategoria.setText("Categoria: " + produto.getCategoria());
        etMarca.setText("Marca: " + produto.getMarca());
        if (produto.getPreco_antigo() == 0.0f) {
            etPreco.setText(String.format("Preço: %.2f €", produto.getPreco()));
        } else {
            etPreco.setText(String.format("Preço: %.2f € (Antes: %.2f €)", produto.getPreco(), produto.getPreco_antigo()));
        }
        etTamanho.setText("Tamanho: " + produto.getTamanho());
        etCores.setText("Cor: " + produto.getCores());

        Glide.with(getApplicationContext())
                .load(produto.getImagem())
                .placeholder(R.drawable.logo_produto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCapa);
    }
}