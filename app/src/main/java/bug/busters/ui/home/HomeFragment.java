package bug.busters.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bug.busters.products.Products;
import bug.busters.api.Retrofit;
import bug.busters.databinding.FragmentHomeBinding;

/**
 * Klasa fragmentu produktów
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductAdapter productAdapter;

    /**
     * Tworzy widok fragmentu
     * @param inflater Obiekt LayoutInflater, którego można użyć do nadmuchania dowolnych widoków we fragmencie
     * @param container Jeśli nie ma wartości null, jest to widok nadrzędny, do którego powinien zostać dołączony interfejs użytkownika fragmentu
     *                  Fragment nie powinien dodawać samego widoku, ale można go wykorzystać do wygenerowania parametrów LayoutParams widoku
     * @param savedInstanceState Jeśli nie ma wartości null, fragment ten jest rekonstruowany z poprzedniego zapisanego stanu, jak podano tutaj
     * @return root
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicjalizacja adaptera produktów
        productAdapter = new ProductAdapter(getContext(), List.of());
        recyclerView.setAdapter(productAdapter);

        // Załadowanie produktów
        loadProducts();

        return root;
    }

    /**
     * Załadowanie produktów z serwera
     */
    private void loadProducts() {
        Retrofit retrofit = new Retrofit();
        retrofit.getProducts(new Retrofit.ProductCallback() {
            /**
             * Metoda wywoływana po udanym pobraniu produktów z serwera
             * @param products Lista produktów
             */
            @Override
            public void onSuccess(List<Products> products) {
                // Przed uzyskaniem dostępu do RecyclerView upewnij się, że powiązanie nie ma wartości null
                if (binding != null) {
                    productAdapter.updateProducts(products);
                }
            }

            /**
             * Metoda wywoływana w przypadku błędu pobierania produktów z serwera
             * @param t Wyjątek
             */
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    /**
     * Usuwanie widoku fragmentu
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
