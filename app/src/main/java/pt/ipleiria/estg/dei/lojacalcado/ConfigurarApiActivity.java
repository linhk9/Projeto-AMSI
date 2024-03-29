package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfigurarApiActivity extends AppCompatActivity {
    private EditText etApiUrl;
    public String defaultApiUrl= "http://172.22.21.214/Projeto-SIS-PSI/backend/web/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_api);

        setTitle("Configurar API");

        etApiUrl = findViewById(R.id.etApiUrl);

        // Carregar API URL guardada de SharedPreferences
        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API", MODE_PRIVATE);
        String savedApiUrl = sharedPreferencesAPI.getString("API_URL", defaultApiUrl);

        // Se a URL da API existir, carregar para o EditText
        if (!savedApiUrl.isEmpty()) {
            etApiUrl.setText(savedApiUrl);
        }
    }

    public void OnClickGuardar(View view) {
        String apiText = etApiUrl.getText().toString();

        SharedPreferences sharedPreferencesAPI = getSharedPreferences("API", MODE_PRIVATE);
        SharedPreferences.Editor editorAPI = sharedPreferencesAPI.edit();

        editorAPI.putString("API_URL", apiText);
        editorAPI.apply();

        finish();
    }
}