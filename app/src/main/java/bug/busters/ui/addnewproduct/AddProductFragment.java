package bug.busters.ui.addnewproduct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import bug.busters.R;
import bug.busters.api.Retrofit;
import bug.busters.products.Products;

public class AddProductFragment extends Fragment {

    private EditText productNameEditText;
    private EditText productQuantityEditText;
    private EditText productPriceEditText;
    private EditText productImageNameEditText;
    private Button addProductButton;

    private AddProductViewModel viewModel;

    // Inicjalizacja fragmentu
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_product, container, false);

        productNameEditText = view.findViewById(R.id.productName);
        productQuantityEditText = view.findViewById(R.id.productQuantity);
        productPriceEditText = view.findViewById(R.id.productPrice);
        productImageNameEditText = view.findViewById(R.id.productImageName);
        addProductButton = view.findViewById(R.id.addProductButton);

        viewModel = new ViewModelProvider(this).get(AddProductViewModel.class);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pobierz dane wprowadzone przez użytkownika
                String productName = productNameEditText.getText().toString();
                int productQuantity = Integer.parseInt(productQuantityEditText.getText().toString());
                double productPrice = Double.parseDouble(productPriceEditText.getText().toString());
                String productImageName = productImageNameEditText.getText().toString();

                // Sprawdź, czy został podany obraz
                if (productImageName.isEmpty()) {
                    // Wyświetl komunikat użytkownikowi o konieczności podania obrazu
                    Toast.makeText(getContext(), "Podaj nazwę obrazu produktu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Utwórz obiekt produktu
                Products product = new Products(productName, productPrice, productQuantity, productImageName);
                product.setObraz(productImageName);

                // Dodaj produkt przy użyciu ViewModel
                viewModel.addProduct(product, new Retrofit.AddProductCallback() {
                    @Override
                    public void onSuccess() {
                        // Obsługa sukcesu, np. wyświetlenie komunikatu o sukcesie
                        Toast.makeText(getContext(), "Produkt został pomyślnie dodany", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Obsługa błędu, np. wyświetlenie komunikatu o błędzie
                        Toast.makeText(getContext(), "Błąd podczas dodawania produktu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
