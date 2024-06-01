package bug.busters.ui.orders;

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
 * Klasa fragmentu zamówień
 */
public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private OrdersAdapter ordersAdapter;
    private OrdersViewModel ordersViewModel;

    /**
     * Tworzy widok fragmentu zamówień
     * @param inflater Obiekt LayoutInflater, którego można użyć do nadmuchania dowolnych widoków we fragmencie
     * @param container Jeśli nie ma wartości null, jest to widok nadrzędny, do którego powinien zostać dołączony interfejs użytkownika fragmentu.
     *                  Fragment nie powinien dodawać samego widoku, ale można go użyć do wygenerowania parametrów LayoutParams widoku
     * @param savedInstanceState Jeśli nie ma wartości null, fragment ten jest rekonstruowany z poprzedniego zapisanego stanu, jak podano tutaj
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ordersViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ordersViewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (ordersAdapter == null) {
                ordersAdapter = new OrdersAdapter(getContext(), orders);
                recyclerView.setAdapter(ordersAdapter);
            } else {
                ordersAdapter.notifyDataSetChanged();
            }
        });

        ordersViewModel.loadOrders(); // Załaduj zamówienia

        return root;
    }

    /**
     * Usuwa widok fragmentu zamówień
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
