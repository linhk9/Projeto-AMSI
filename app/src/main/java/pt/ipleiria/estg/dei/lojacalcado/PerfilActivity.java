package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pt.ipleiria.estg.dei.lojacalcado.listeners.UserListener;
import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorLoja;
import pt.ipleiria.estg.dei.lojacalcado.modelo.User;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class PerfilActivity extends AppCompatActivity implements UserListener {
    private EditText etUsername, etEmail, etPrimeiroNome, etUltimoNome, etTelemovel, etMorada;
    public String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    public String savedApiUrl;
    private static final int MIN_CHAR = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        setTitle("Perfil do Utilizador");

        SingletonGestorLoja.getInstance(getApplicationContext()).setUserListener(this);

        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API_URL", MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPrimeiroNome = findViewById(R.id.etPrimeiroNome);
        etUltimoNome = findViewById(R.id.etUltimoNome);
        etTelemovel = findViewById(R.id.etTelemovel);
        etMorada = findViewById(R.id.etMorada);

        SingletonGestorLoja.getInstance(getApplicationContext()).carregarPerfil(this);
    }

    private void validarEmail(String email) {
        boolean emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!emailValido) {
            etEmail.setError("Email inválido");
            throw new IllegalArgumentException("Email inválido");
        }
    }

    private void validarCampo(String value, EditText editText) {
        if (value.isEmpty()) {
            editText.setError("Este campo precisa de ser preenchido!");
            throw new IllegalArgumentException("Este campo precisa de ser preenchido!");
        }
    }

    public void OnClickGuardarEdicao(View view) {
        if (!LojaJsonParser.isConnectionInternet(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
        } else {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String primeiroNome = etPrimeiroNome.getText().toString();
            String ultimoNome = etUltimoNome.getText().toString();
            String telemovel = etTelemovel.getText().toString();
            String morada = etMorada.getText().toString();

            try {
                validarEmail(email);
                validarCampo(username, etUsername);
                validarCampo(primeiroNome, etPrimeiroNome);
                validarCampo(ultimoNome, etUltimoNome);
                validarCampo(telemovel, etTelemovel);
                validarCampo(morada, etMorada);
            } catch (IllegalArgumentException e) {
                return;
            }

            SingletonGestorLoja.getInstance(getApplicationContext()).editarPerfil(email, primeiroNome, ultimoNome, telemovel, morada, this);
        }
    }

    @Override
    public void onRefreshDados(User user) {
        if (user != null) {
            etUsername.setText(user.getUsername());
            etEmail.setText(user.getEmail());
            etPrimeiroNome.setText(user.getPrimeiroNome());
            etUltimoNome.setText(user.getUltimoNome());
            etTelemovel.setText(user.getTelemovel() + "");
            etMorada.setText(user.getMorada());
        } else {
            Toast.makeText(getApplicationContext(), "Erro ao carregar perfil", Toast.LENGTH_SHORT).show();
        }
    }
}