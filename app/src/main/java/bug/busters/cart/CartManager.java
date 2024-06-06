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

        Call<List<Products>> call = myApi.getProducts(); // Pobranie listy produktów z bazy danych
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    List<Products> productList = response.body();
                    // Sprawdzenie dostępności produktu w bazie danych
                    boolean productAvailable = false;
                    for (Products p : productList) {
                        if (p.getId_product() == product.getId_product() && p.getIlosc() > 0) {
                            productAvailable = true;
                            break;
                        }
                    }
                    if (productAvailable) {
                        // Produkt dostępny w bazie danych, można dodać do koszyka
                        for (CartItem item : cartItems) {
                            if (item.getProduct().getId_product() == product.getId_product()) {
                                item.setQuantity(item.getQuantity() + 1);
                                notifyCartChanged();
                                callback.onSuccess();
                                return;
                            }
                        }
                        cartItems.add(new CartItem(product, 1));
                        notifyCartChanged();
                        callback.onSuccess();
                    } else {
                        // Produkt niedostępny w bazie danych
                        callback.onFailure(new Exception("Produkt niedostępny w wystarczającej ilości"));
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
