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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

        carregarFragmentoInicial();
    }

    private void carregarFragmentoInicial() {
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        onNavigationItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carrinho, menu);

        MenuItem itemCarrinho = menu.findItem(R.id.itemCarrinho);
        itemCarrinho.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent intent = new Intent(MenuMainActivity.this, CarrinhoActivity.class);
                startActivity(intent);
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

        if (itemId == R.id.navDashboard) {
            if (isConnectionInternet) {
                fragment = new DashboardFragment();
                setTitle(item.getTitle());
            } else {
                Toast.makeText(this, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.navListaProdutos) {
            if (isConnectionInternet) {
                fragment = new ListaProdutosFragment();
                setTitle(item.getTitle());
            } else {
                Toast.makeText(this, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.navHistorico) {
            fragment = new ListaFaturasFragment();
            setTitle(item.getTitle());
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