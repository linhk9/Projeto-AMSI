package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfigurarApiActivity extends AppCompatActivity {
    private EditText etApiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_api);

        setTitle("Configurar API");

        etApiUrl = findViewById(R.id.etApiUrl);

        // Carregar API URL guardada de SharedPreferences
        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API_URL", MODE_PRIVATE);
        String savedApiUrl = sharedPreferencesAPI.getString("API_URL", "");

        // Se a URL da API existir, carregar para o EditText
        if (!savedApiUrl.isEmpty()) {
            etApiUrl.setText(savedApiUrl);
        }
    }

    public void OnClickGuardar(View view) {
        String apiText = etApiUrl.getText().toString();

        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API_URL", MODE_PRIVATE);
        SharedPreferences.Editor editorAPI = sharedPreferencesAPI.edit();

        editorAPI.putString("API_URL", apiText);
        editorAPI.apply();

        finish();
    }
}