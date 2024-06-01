package bug.busters.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import bug.busters.databinding.FragmentOrdersBinding;

/**
 * Klasa fragmenu, który wyświetla listę zamówień użytkownika
 */
public class SlideshowFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private UserOrderAdapter userOrderAdapter;
    private SlideshowViewModel slideshowViewModel;

    /**
     * Tworzy widok
     * @param inflater Obiekt LayoutInflater, którego można użyć do nadmuchania dowolnych widoków we fragmencie
     * @param container Jeśli nie ma wartości null, jest to widok nadrzędny, do którego powinien zostać dołączony interfejs użytkownika fragmentu.
     *                  Fragment nie powinien dodawać samego widoku, ale można go użyć do wygenerowania parametrów LayoutParams widoku
     * @param savedInstanceState Jeśli nie ma wartości null, fragment ten jest rekonstruowany z poprzedniego zapisanego stanu, jak podano tutaj
     *
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicjalizacja ViewModelu z przekazaniem kontekstu oraz dostępu do zalogowanego użytkownika
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        slideshowViewModel.init(requireContext());

        // Obserwowanie danych zamówień
        slideshowViewModel.getUserOrders().observe(getViewLifecycleOwner(), orders -> {
            if (userOrderAdapter == null) {
                userOrderAdapter = new UserOrderAdapter(getContext(), orders);
                recyclerView.setAdapter(userOrderAdapter);
            } else {
                userOrderAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    /**
     * Metoda wywoływana, gdy fragment jest widoczny
     */
    @Override
    public void onResume() {
        super.onResume();
        // Wczytanie zamówień użytkownika za każdym razem, gdy fragment jest widoczny
        slideshowViewModel.loadUserOrders();
    }

    /**
     * Metoda wywoływana, gdy fragment jest usunięty
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
