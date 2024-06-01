package bug.busters.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import bug.busters.R;

/**
 * Aktywność przenosząca do widoku klienta
 */
public class MainActivityHome extends AppCompatActivity {


    /**
     * Tworzenie widoku klienta
     * @param savedInstanceState Instancja
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
    }
}
