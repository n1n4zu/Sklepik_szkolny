package bug.busters.cart;

import java.util.ArrayList;
import java.util.List;

import bug.busters.api.MyApi;
import bug.busters.products.Products;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public interface AddToCartCallback {
        void onSuccess();
        void onFailure(Throwable t);
    }


    /**
     * Metoda do dodawania produktu do koszyka
     * @param product Produkt do dodania
     */
    public void addToCart(Products product, AddToCartCallback callback) {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.0.48:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi myApi = retrofit.create(MyApi.class);

        // Pobranie listy produktów z bazy danych
        Call<List<Products>> call = myApi.getProducts();
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    List<Products> productList = response.body();
                    int availableQuantity = 0;

                    // Sprawdzenie dostępności produktu w bazie danych
                    for (Products p : productList) {
                        if (p.getId_product() == product.getId_product()) {
                            availableQuantity = p.getIlosc();
                            break;
                        }
                    }

                    // Sprawdzenie ilości produktu w koszyku
                    int currentQuantityInCart = getCurrentQuantityInCart(product);

                    // Sprawdzenie, czy ilość produktu w koszyku nie przekracza dostępnej ilości w bazie danych
                    if (currentQuantityInCart < availableQuantity) {
                        // Produkt dostępny w bazie danych i ilość w koszyku nie przekracza dostępnej ilości w bazie danych
                        addOrUpdateCartItem(product);
                        callback.onSuccess();
                    } else {
                        // Produkt niedostępny w bazie danych lub ilość w koszyku przekracza dostępną ilość w bazie danych
                        callback.onFailure(new Exception("Nie można dodać więcej sztuk tego produktu do koszyka"));
                    }
                } else {
                    callback.onFailure(new Exception("Response not successful"));
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    /**
     * Metoda do dodania lub aktualizacji elementu w koszyku
     * @param product Produkt do dodania
     */
    private void addOrUpdateCartItem(Products product) {
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
     * Metoda do pobierania aktualnej ilości produktu w koszyku
     * @param product Produkt
     * @return Aktualna ilość produktu w koszyku
     */
    private int getCurrentQuantityInCart(Products product) {
        int currentQuantityInCart = 0;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId_product() == product.getId_product()) {
                currentQuantityInCart += item.getQuantity();
            }
        }
        return currentQuantityInCart;
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
