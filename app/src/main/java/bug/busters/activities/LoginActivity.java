package bug.busters.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import bug.busters.MainActivity;
import bug.busters.R;
import bug.busters.api.Retrofit;
import bug.busters.users.Users;
import bug.busters.utils.SessionManager;

/**
 * Aktwyność logowania
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    /**
     * Tworzenie widoku
     * @param savedInstanceState Instancja
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextEmail = findViewById(R.id.EmailEdit);
        editTextPassword = findViewById(R.id.HasloEdit);
        buttonLogin = findViewById(R.id.ButtonLogin);

        // Obsługa kliknięcia przycisku logowania
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            /**
             * Obsługa kliknięcia przycisku logowania
             * @param v Widok
             */
            @Override
            public void onClick(View v) {
                // Pobranie wprowadzonych danych
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Walidacja pól
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Wprowadź adres e-mail");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Wprowadź hasło");
                    return;
                }

                Retrofit retrofit = new Retrofit();

                retrofit.getUsers(email, password, new Retrofit.UserCallback() {
                    /**
                     * Obsługa udanego logowania
                     * @param users Lista użytkowników
                     */
                    @Override
                    public void onSuccess(List<Users> users) {
                        if (!users.isEmpty()) {
                            Users user = users.get(0);
                            // Zapisuje ID zalogowanego użytkownika do SharedPreferences
                            SessionManager sessionManager = new SessionManager(LoginActivity.this);
                            sessionManager.saveUserId(user.getId()); // Tutaj zapisujemy ID zalogowanego użytkownika
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userRole", user.getRola());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Nieprawidłowy e-mail lub hasło", Toast.LENGTH_SHORT).show();
                        }
                    }

                    /**
                     * Obsługa błędu logowania
                     * @param t Błąd
                     */
                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(LoginActivity.this, "Błąd logowania", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
