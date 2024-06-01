package bug.busters.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import bug.busters.cart.CartManager;
import bug.busters.cart.CartItem;

/**
 * Klasa ViewModel dla widoku koszyka
 */
public class GalleryViewModel extends ViewModel {

    // Lista produktów w koszyku
    private final MutableLiveData<List<CartItem>> cartItems;

    /**
     * Konstruktor klasy
     */
    public GalleryViewModel() {
        cartItems = new MutableLiveData<>();
        loadCartItems();
    }

    /**
     * Zwraca listę produktów w koszyku
     * @return Lista produktów w koszyku
     */
    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    /**
     * Ładuje produkty z koszyka
     */
    private void loadCartItems() {
        // Ładuje produkty z koszyka
        cartItems.setValue(CartManager.getInstance().getCartItems());
    }
}
