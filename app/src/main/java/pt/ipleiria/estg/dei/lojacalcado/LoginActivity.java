package pt.ipleiria.estg.dei.lojacalcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername , etPassword;
    public static final int MIN_CHAR = 4;
    public  static final  String USERNAME = "USERNAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
    }

    private boolean isUsernameValido(String username)
    {
        if(username == null)
            return false;
        return username.length() >= MIN_CHAR;
    }

    private boolean isPasswordValida(String pass)
    {
        if(pass == null)
            return false;
        return pass.length() >= MIN_CHAR;
    }

    public void OnClickLogin(View view) {

        String username = etUsername.getText().toString();
        String pass = etPassword.getText().toString();

        if(!isUsernameValido(username)) {
            etUsername.setError("Username inválido");
            return;
        }
        if(!isPasswordValida(pass)) {
            etPassword.setError("Password inválida");
            return;
        }

        Intent intent = new Intent(this, MenuMainActivity.class);
        intent.putExtra(USERNAME, username);
        startActivity(intent);
        finish();
    }

    public void OnClickRegistar(View view) {
        Intent intent = new Intent(this, RegistarActivity.class);
        startActivity(intent);
        finish();
    }

    public void OnClickConfigurarAPI(View view) {
        Intent intent = new Intent(this, ConfigurarApiActivity.class);
        startActivity(intent);
        finish();
    }
}