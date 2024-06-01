package bug.busters.ui.slideshow;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import bug.busters.orders.Order;
import bug.busters.api.Retrofit;
import bug.busters.utils.SessionManager;

/**
 * ViewModel dla SlideshowFragment
 */
public class SlideshowViewModel extends ViewModel {

    // Lista zamówień
    private final MutableLiveData<List<Order>> orders = new MutableLiveData<>();
    private Context context;

    /**
     * Konstruktor ViewModel
     */
    public SlideshowViewModel() {
    }

    /**
     * Inicjalizacja ViewModel
     * @param context kontekst aplikacji
     */
    public void init(Context context) {
        this.context = context;
    }

    /**
     * Pobranie listy zamówień
     * @return lista zamówień
     */
    public LiveData<List<Order>> getUserOrders() {
        return orders;
    }

    /**
     * Wczytanie listy zamówień z API Retrofit
     */
    public void loadUserOrders() {
        // Pobranie ID zalogowanego użytkownika z sesji
        SessionManager sessionManager = new SessionManager(context);
        int id = sessionManager.getUserId();

        // Pobranie zamówień z API Retrofit
        Retrofit retrofit = new Retrofit();
        retrofit.getUserOrders(id, new Retrofit.UserOrdersCallback() {
            /**
             * Pobranie listy zamówień z API Retrofit
             * @param orderList lista zamówień
             */
            @Override
            public void onSuccess(List<Order> orderList) {
                orders.postValue(orderList);
            }

            /**
             * Obsługa błędu
             * @param t błąd
             */
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
}
