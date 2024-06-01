package bug.busters.cart;

import java.util.ArrayList;
import java.util.List;

import bug.busters.products.Products;

/**
 * Klasa zarządzająca koszykiem
 */
public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;
    private OnCartChangeListener cartChangeListener;

    /**
     * Interfejs na nasłuchiwanie zmian w koszyku
     */
    public interface OnCartChangeListener {
        void onCartChanged();
    }

    /**
     * Prywatny konstruktor, aby zabronić tworzeniu instancji klasy
     */
    private CartManager() {
        cartItems = new ArrayList<>();
    }

    /**
     * Metoda do pobierania instancji klasy
     * @return Instancja klasy CartManager
     */
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    /**
     * Metoda do pobierania listy produktów w koszyku
     * @return Lista produktów w koszyku
     */
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    /**
     * Metoda do ustawiania nasłuchnika na zmiany w koszyku
     * @param listener Nasłuchuje zmiany w koszyku
     */
    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    /**
     * Metoda do dodawania produktu do koszyka
     * @param product Produkt do dodania
     */
    public void addToCart(Products product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId_product() == product.getId_product()) {
                item.setQuantity(item.getQuantity() + 1);
                notifyCartChanged();
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
        notifyCartChanged();
    }

    /**
     * Metoda do usuwania produktu z koszyka
     * @param product Produkt do usunięcia
     */
    public void removeFromCart(Products product) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if (item.getProduct().getId_product() == product.getId_product()) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    cartItems.remove(i);
                }
                notifyCartChanged();
                return;
            }
        }
    }

    /**
     * Metoda do wywołania nasłuchiwania zmian w koszyku
     */
    private void notifyCartChanged() {
        if (cartChangeListener != null) {
            cartChangeListener.onCartChanged();
        }
    }

    /**
     * Metoda do czyszczenia koszyka
     */
    public void clearCart() {
        cartItems.clear();
        notifyCartChanged();
    }
}
