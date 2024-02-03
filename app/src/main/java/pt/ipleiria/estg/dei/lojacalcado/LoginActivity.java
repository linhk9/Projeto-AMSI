package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorLoja;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername , etPassword;
    public static final int MIN_CHAR = 6;
    public String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    public String savedApiUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API", MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);

        SharedPreferences sharedPreferencesUser = getSharedPreferences("DADOS_USER", MODE_PRIVATE);
        boolean isAuthenticated = sharedPreferencesUser.getBoolean("AUTENTICADO", false);

        // Se estiver autenticado, vai para o menu principal
        if (isAuthenticated) {
            Intent intent = new Intent(this, MenuMainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API", MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);
    }

    private void validarPassword(String password) {
        boolean passwordValida = password.length() >= MIN_CHAR;
        if (!passwordValida) {
            etPassword.setError("Password inválida");
            throw new IllegalArgumentException("Password inválida");
        }
    }

    private void validarCampo(String value, EditText editText) {
        if (value.isEmpty()) {
            editText.setError("Este campo precisa de ser preenchido!");
            throw new IllegalArgumentException("Este campo precisa de ser preenchido!");
        }
    }

    public void OnClickLogin(View view) {
        if (!savedApiUrl.isEmpty()) {
            if (!LojaJsonParser.isConnectionInternet(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
            } else {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                try {
                    validarCampo(username, etUsername);
                    validarPassword(password);
                } catch (IllegalArgumentException e) {
                    return;
                }

                SingletonGestorLoja.getInstance(getApplicationContext()).login(username, password, this);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Precisas de definir o URL da API", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickRegistar(View view) {
        if (!savedApiUrl.isEmpty() && LojaJsonParser.isConnectionInternet(getApplicationContext())) {
            Intent intent = new Intent(this, RegistarActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Precisas de definir o URL da API", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickConfigurarAPI(View view) {
        Intent intent = new Intent(this, ConfigurarApiActivity.class);
        startActivity(intent);
    }
}