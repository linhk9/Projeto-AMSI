package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pt.ipleiria.estg.dei.lojacalcado.modelo.SingletonGestorLoja;
import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class RegistarActivity extends AppCompatActivity {
    private EditText etUsername , etPassword, etEmail, etPrimeiroNome, etUltimoNome, etTelemovel, etMorada;
    public String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    public String savedApiUrl;
    private static final int MIN_CHAR = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        setTitle("Registo");

        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API", MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etPrimeiroNome = findViewById(R.id.etPrimeiroNome);
        etUltimoNome = findViewById(R.id.etUltimoNome);
        etTelemovel = findViewById(R.id.etTelemovel);
        etMorada = findViewById(R.id.etMorada);
    }

    private void validarEmail(String email) {
        boolean emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!emailValido) {
            etEmail.setError("Email inválido");
            throw new IllegalArgumentException("Email inválido");
        }
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

    public void OnClickRegistarUtilizador(View view) {
        if (!LojaJsonParser.isConnectionInternet(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
        } else {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();
            String primeiroNome = etPrimeiroNome.getText().toString();
            String ultimoNome = etUltimoNome.getText().toString();
            String telemovel = etTelemovel.getText().toString();
            String morada = etMorada.getText().toString();

            try {
                validarEmail(email);
                validarPassword(password);
                validarCampo(username, etUsername);
                validarCampo(primeiroNome, etPrimeiroNome);
                validarCampo(ultimoNome, etUltimoNome);
                validarCampo(telemovel, etTelemovel);
                validarCampo(morada, etMorada);
            } catch (IllegalArgumentException e) {
                return;
            }

            SingletonGestorLoja.getInstance(getApplicationContext()).registo(username, password, email, primeiroNome, ultimoNome, telemovel, morada, this);
        }
    }
}