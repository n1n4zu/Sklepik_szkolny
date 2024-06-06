package bug.busters.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bug.busters.cart.CartItem;
import bug.busters.cart.CartManager;
import bug.busters.api.Retrofit;
import bug.busters.databinding.FragmentGalleryBinding;
import bug.busters.utils.SessionManager;

/**
 * Klasa fragmentu koszyka
 */
public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private CartAdapter cartAdapter;

    /**
     * Tworzy nowy obiekt fragmentu koszyka
     * @param inflater Obiekt LayoutInflater, którego można użyć do uzupełnienia dowolnych widoków we fragmencie
     * @param container Jeśli nie ma wartości null, jest to widok nadrzędny, do którego powinien zostać dołączony interfejs użytkownika fragmentu
     *                  Fragment nie powinien dodawać samego widoku, ale można go wykorzystać do wygenerowania parametrów LayoutParams widoku
     * @param savedInstanceState Jeśli nie ma wartości null, fragment ten jest rekonstruowany z poprzedniego zapisanego stanu, jak podano tutaj
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        galleryViewModel.getCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            if (cartAdapter == null) {
                cartAdapter = new CartAdapter(getContext(), cartItems);
                cartAdapter.setOnDeleteClickListener(position -> {
                    // Usuwa produkt z koszyka
                    CartManager.getInstance().removeFromCart(cartItems.get(position).getProduct());
                });
                recyclerView.setAdapter(cartAdapter);
            } else {
                cartAdapter.notifyDataSetChanged();
            }

            // Przelicza cenę
            double totalPrice = calculateTotalPrice(cartItems);
            binding.tvTotalPrice.setText("Cena całkowita: " + totalPrice + " zł");
        });

        // Dodaje guzik do zamówienia
        binding.btnPlaceOrder.setOnClickListener(v -> {
            List<CartItem> cartItems = CartManager.getInstance().getCartItems();
            if (cartItems != null && !cartItems.isEmpty()) {
                Retrofit retrofit = new Retrofit();
                SessionManager sessionManager = new SessionManager(requireContext());
                int userId = sessionManager.getUserId();
                retrofit.placeOrder(cartItems, userId, new Retrofit.OrderCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Zamówienie zostało złożone", Toast.LENGTH_SHORT).show();
                        // Czyści koszyk po złożeniu zamówienia
                        CartManager.getInstance().clearCart();
                        updateCartItems(); // Aktualizuje koszyk po złożeniu zamówienia
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (t.getMessage().contains("Nieprawidłowe zapytanie")) {
                            Toast.makeText(getContext(), "Nie ma wystarczającej ilości produktów w sklepiku", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Nie udało się złożyć zamówienia", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Koszyk jest pusty", Toast.LENGTH_SHORT).show();
            }
        });

        CartManager.getInstance().setOnCartChangeListener(this::updateCartItems);

        return root;
    }

    /**
     * Aktualizuje koszyk po onResume
     */
    @Override
    public void onResume() {
        super.onResume();
        updateCartItems();
    }

    /**
     * Aktualizuje koszyk
     */
    private void updateCartItems() {
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        if (cartItems != null) {
            if (cartAdapter == null) {
                cartAdapter = new CartAdapter(getContext(), cartItems);
                cartAdapter.setOnDeleteClickListener(position -> {
                    // Usuń produkt z koszyka
                    CartManager.getInstance().removeFromCart(cartItems.get(position).getProduct());
                });
                RecyclerView recyclerView = binding.recyclerViewCart;
                recyclerView.setAdapter(cartAdapter);
            } else {
                cartAdapter.notifyDataSetChanged();
            }

            // Obliczanie ceny
            double totalPrice = calculateTotalPrice(cartItems);
            binding.tvTotalPrice.setText("Cena całkowita: " + totalPrice + " zł");
        }
    }

    /**
     * Oblicza cenę
     * @param cartItems Lista produktów w koszyku
     * @return Cena całkowita
     */
    private double calculateTotalPrice(List<CartItem> cartItems) {
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getProduct().getCena() * cartItem.getQuantity();
        }
        return totalPrice;
    }

    /**
     * Usuwa koszyk
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
