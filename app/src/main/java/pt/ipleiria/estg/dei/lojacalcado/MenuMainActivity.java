package pt.ipleiria.estg.dei.lojacalcado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonUserManager;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;


public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawer;


    // TODO: Adicionar botão de carrinho de compras no optionsMenu

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean isConnectionInternet = LojaJsonParser.isConnectionInternet(getApplicationContext());
        int itemId = item.getItemId();

        if (itemId == R.id.navListaProdutos) {
            if (isConnectionInternet) {
                Intent intent = new Intent(this, ConfigurarApiActivity.class);
                startActivity(intent);
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

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}