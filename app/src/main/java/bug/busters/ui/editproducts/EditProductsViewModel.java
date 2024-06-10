package bug.busters.ui.editproducts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import bug.busters.orders.Order;
import bug.busters.api.Retrofit;

/**
 * Klasa ViewModel dla widoku zamówień
 */
public class EditProductsViewModel extends ViewModel {

    // Lista zamówień
    private final MutableLiveData<List<Order>> orders = new MutableLiveData<>();

    /**
     * Zwraca listę zamówień jako LiveData
     * @return LiveData<List<Order>>
     */
    public LiveData<List<Order>> getOrders() {
        return orders;
    }

    /**
     * Wczytuje listę zamówień z serwera
     */
    public void loadOrders() {
        Retrofit retrofit = new Retrofit();
        retrofit.getOrders(new Retrofit.OrdersCallback() {
            /**
             * Metoda wywoływana po udanym pobraniu danych z serwera
             * @param orderList lista zamówień
             */
            @Override
            public void onSuccess(List<Order> orderList) {
                orders.postValue(orderList);
            }

            /**
             * Metoda wywoływana w przypadku błędu pobierania danych z serwera
             * @param t wyjątek
             */
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
}
