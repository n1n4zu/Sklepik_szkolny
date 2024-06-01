package bug.busters.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Klasa zarządzająca sesją użytkownika.
 */
public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "userId";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    /**
     * Konstruktor klasy SessionManager.
     * @param context obiekt kontekstu aplikacji
     */
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Zapisuje ID użytkownika do pamięci podręcznej.
     * @param userId ID użytkownika
     */
    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    /**
     * Pobiera ID użytkownika z pamięci podręcznej.
     * @return ID użytkownika
     */
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1); // Default value -1 indicates user not logged in
    }
}

