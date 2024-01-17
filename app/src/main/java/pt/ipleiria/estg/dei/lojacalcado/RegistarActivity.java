package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.lojacalcado.utils.LojaJsonParser;

public class RegistarActivity extends AppCompatActivity {
    private EditText etUsername , etPassword, etEmail, etPrimeiroNome, etUltimoNome, etTelemovel, etMorada;
    private static RequestQueue volleyQueue = null;
    public String savedApiUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        setTitle("Registo");

        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API_URL", MODE_PRIVATE);
        savedApiUrl = sharedPreferencesAPI.getString("API_URL", "");

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etPrimeiroNome = findViewById(R.id.etPrimeiroNome);
        etUltimoNome = findViewById(R.id.etUltimoNome);
        etTelemovel = findViewById(R.id.etTelemovel);
        etMorada = findViewById(R.id.etMorada);
    }

    public void OnClickRegistarUtilizador(View view) {
        if (!LojaJsonParser.isConnectionInternet(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Não tem ligação á internet", Toast.LENGTH_SHORT).show();
        } else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, savedApiUrl + "/users/registo", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), LojaJsonParser.parserJsonRegisto(response), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        jsonBody.put("username", etUsername.getText().toString());
                        jsonBody.put("password", etPassword.getText().toString());
                        jsonBody.put("email", etEmail.getText().toString());
                        jsonBody.put("primeiroNome", etPrimeiroNome.getText().toString());
                        jsonBody.put("ultimoNome", etUltimoNome.getText().toString());
                        jsonBody.put("telemovel", etTelemovel.getText().toString());
                        jsonBody.put("morada", etMorada.getText().toString());

                        final String mRequestBody = jsonBody.toString();

                        return mRequestBody.getBytes("utf-8");
                    } catch (Exception e) {
                        Log.e("RegistarActivity", "Erro ao criar JSON body: " + e.getMessage());
                        return null;
                    }
                }
            };

            volleyQueue.add(req);
        }
    }
}