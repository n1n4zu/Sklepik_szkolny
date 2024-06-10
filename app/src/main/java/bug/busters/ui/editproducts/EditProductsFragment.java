package bug.busters.ui.editproducts;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bug.busters.api.Retrofit;
import bug.busters.products.Products;
import bug.busters.R;

public class EditProductsFragment extends Fragment implements EditProductAdapter.ProductClickListener {

    private EditProductAdapter productAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_prod, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicjalizacja adaptera produktów
        productAdapter = new EditProductAdapter(getContext(), this);
        recyclerView.setAdapter(productAdapter);

        // Załadowanie produktów
        loadProducts();

        return root;
    }

    private void loadProducts() {
        Retrofit retrofit = new Retrofit();
        retrofit.getProducts(new Retrofit.ProductCallback() {
            @Override
            public void onSuccess(List<Products> products) {
                productAdapter.updateProducts(products);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onProductClick(Products product) {
        // Wyświetlenie okna dialogowego do edycji produktu
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_products, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.editProductName);
        EditText editPrice = dialogView.findViewById(R.id.editProductPrice);
        EditText editQuantity = dialogView.findViewById(R.id.editProductQuantity);

        editName.setText(product.getNazwa());
        editPrice.setText(String.valueOf(product.getCena()));
        editQuantity.setText(String.valueOf(product.getIlosc()));

        AlertDialog dialog = builder.create();
        dialog.show();

        // Obsługa przycisku "Zapisz"
        Button saveButton = dialogView.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(view -> {
            String newName = editName.getText().toString().trim();
            String newPriceStr = editPrice.getText().toString().trim();
            String newQuantityStr = editQuantity.getText().toString().trim();

            if (newName.isEmpty() || newPriceStr.isEmpty() || newQuantityStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double newPrice = Double.parseDouble(newPriceStr);
            int newQuantity = Integer.parseInt(newQuantityStr);

            Products updatedProduct = new Products(product.getId_product(), newName, newPrice, newQuantity);

            // Wywołanie metody do aktualizacji produktu na serwerze
            updateProduct(updatedProduct);
            dialog.dismiss();
        });
    }

    private void updateProduct(Products product) {
        Retrofit retrofit = new Retrofit();
        retrofit.editProduct(product, new Retrofit.OrderCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
                // Ponowne załadowanie produktów po aktualizacji
                loadProducts();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Failed to update product", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
