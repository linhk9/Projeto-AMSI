package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername , etPassword;
    public static final int MIN_CHAR = 6;
    private static RequestQueue volleyQueue = null;
    public String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";
    public String savedApiUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        volleyQueue = Volley.newRequestQueue(getApplicationContext());

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API_URL", MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API_URL", MODE_PRIVATE);
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

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, savedApiUrl + "/users/login", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (LojaJsonParser.parserJsonLogin(response)) {
                            Toast.makeText(getApplicationContext(), "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MenuMainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "Erro ao fazer login";
                        }
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("username", username);
                            jsonBody.put("password", password);

                            final String mRequestBody = jsonBody.toString();

                            return mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException e) {
                            Log.e("LoginActivity", "Encoding não suportado: " + e.getMessage());
                            return null;
                        } catch (JSONException e) {
                            Log.e("LoginActivity", "Erro JSON: " + e.getMessage());
                            return null;
                        }
                    }
                };

                volleyQueue.add(req);
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