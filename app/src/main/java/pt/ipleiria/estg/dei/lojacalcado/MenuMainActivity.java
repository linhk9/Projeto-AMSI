package pt.ipleiria.estg.dei.lojacalcado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonUserManager;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;


public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        carrgarDinamico();
    }

    private void carrgarDinamico() {

        //TODO: Carregar os dados com a API

        LinearLayout llContainer = new LinearLayout(this);
        llContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        llContainer.setOrientation(LinearLayout.VERTICAL);
        llContainer.setGravity(Gravity.CENTER);
        llContainer.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 250, 0, 0);


        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(params);
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(25);
        textView1.setTypeface(null, Typeface.BOLD);
        textView1.setText("Total de Produtos no Carrinho");
        llContainer.addView(textView1);

        TextView ProdutosCarrinho = new TextView(this);
        ProdutosCarrinho.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ProdutosCarrinho.setTextColor(Color.BLACK);
        ProdutosCarrinho.setTextSize(25);
        ProdutosCarrinho.setText("15");
        llContainer.addView(ProdutosCarrinho);

        TextView textView2 = new TextView(this);
        textView2.setLayoutParams(params);
        textView2.setTextColor(Color.BLACK);
        textView2.setTextSize(25);
        textView2.setTypeface(null, Typeface.BOLD);
        textView2.setText("Total de Produtos na Loja" );
        llContainer.addView(textView2);

        TextView ProdutosLoja = new TextView(this);
        ProdutosLoja.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ProdutosLoja.setTextColor(Color.BLACK);
        ProdutosLoja.setTextSize(25);
        ProdutosLoja.setText("50");
        llContainer.addView(ProdutosLoja);

        TextView textView3 = new TextView(this);
        textView3.setLayoutParams(params);
        textView3.setTextColor(Color.BLACK);
        textView3.setTextSize(25);
        textView3.setTypeface(null, Typeface.BOLD);
        textView3.setText("Total de Faturas Emitidas" );
        llContainer.addView(textView3);

        TextView FaturasEmitidas = new TextView(this);
        FaturasEmitidas.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        FaturasEmitidas.setTextColor(Color.BLACK);
        FaturasEmitidas.setTextSize(25);
        FaturasEmitidas.setText("3");
        llContainer.addView(FaturasEmitidas);

        ((ViewGroup) findViewById(R.id.drawerLayout)).addView(llContainer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carrinho, menu);

        MenuItem itemCarrinho = menu.findItem(R.id.itemCarrinho);
        itemCarrinho.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
//                Intent intent = new Intent(MenuMainActivity.this, CarrinhoActivity.class);
//                startActivity(intent);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        boolean isConnectionInternet = LojaJsonParser.isConnectionInternet(getApplicationContext());
        int itemId = item.getItemId();

        if (itemId == R.id.navListaProdutos) {
            if (isConnectionInternet) {
                fragment = new ListaProdutosFragment();
                setTitle(item.getTitle());
            } else {
                Toast.makeText(this, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.navHistorico) {
            Intent intent = new Intent(this, ConfigurarApiActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.navPerfilUtilizador) {
            if (isConnectionInternet) {
                Intent intent = new Intent(this, PerfilActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.navConfiguracoes) {
            Intent intent = new Intent(this, ConfigurarApiActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.navLogout) {
            SingletonUserManager.getInstance(getApplicationContext()).logout(this);
            finish();
        }

        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}