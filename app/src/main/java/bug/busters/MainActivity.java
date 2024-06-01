package bug.busters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import bug.busters.activities.LoginActivity;
import bug.busters.databinding.ActivityMainBinding;
import bug.busters.utils.SessionManager;

//TODO połącznie z serwerem -> Done
//TODO baza danych na serwerze -> Done
//TODO wyświetlenie produktów w HomeFragment -> Done
//TODO poprawić zdjęcia (na koniec) -> Probably done
//TODO dodawanie do koszyka -> Done
//TODO wybór przerwy (Na koniec) -> Do prezentacji (w planach rozwojowych aplikacji)
//TODO wyświetlanie koszyka -> Done
//TODO usuwanie z koszyka -> Done
//TODO składanie zamówienia -> Done
//TODO logowanie -> Done
//TODO logowanie z rolą dla klienta -> Done
//TODO logowanie z rolą dla sklepikarza -> Done
//TODO logowanie z rolą -> Done
//TODO widok sklepikarza -> Done
//TODO wyświetlenie zamówień w HomeFragment -> Done
//TODO modyfikacja statusu zamówień (w trakcie, zrealizowane, anulowane) -> Done
//TODO sesja -> Done
//TODO twoje zamówienia -> Done
//TODO powiadomienia (w aplikacji) -> Do prezentacji (w planach rozwojowych aplikacji)
//TODO odświeżanie koszyka zamówień po zmianie statusu -> Do prezentacji (w planach rozwojowych aplikacji)
//TODO usuwanie rekordów zamówień codziennie o półnicy -> Do prezentacji (w planach rozwojowych aplikacji)
//TODO zmniejszanie ilości produktów w bazie danych po dodaniu do zamówienia
//TODO front end -> Done
//TODO komentarze -> Done
//TODO javadoc -> In progress

/**
 * Główna klasa i głowna aktywność aplikacji.
 * @author Kryspin Dziarek i Jakub Rybczyński
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    /**
     * Metoda wywoływana po utworzeniu aktywności.
     * @param savedInstanceState Instancja
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Sprawdzanie, czy użytkownik jest zalogowany
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();

        Log.d("ID", String.valueOf(userId));

        if (userId == -1) {
            // Jeśli użytkownik nie jest zalogowany, przekieruj do logowania
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Pobierz rolę
        char userRole = getIntent().getCharExtra("userRole", 'K');

        if (userRole == 'S') {
            // Przenieś do widoku sklepikarza
            navController.navigate(R.id.nav_orders);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Wyłącz przycisk cofania
        } else {
            // Przenieś do widoku klienta
            navController.navigate(R.id.nav_home);
        }
    }

    /**
     * Tworzenie menu.
     * @param menu Menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Obsługa kliknięcia przycisku cofania.
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Metoda wywoływana po usunięciu aktywności.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}