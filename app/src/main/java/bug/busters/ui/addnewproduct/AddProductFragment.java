package bug.busters.ui.addnewproduct;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.File;

import bug.busters.R;
import bug.busters.api.Retrofit;
import bug.busters.products.Products;

public class AddProductFragment extends Fragment {

    private EditText productNameEditText;
    private EditText productQuantityEditText;
    private EditText productPriceEditText;
    private ImageView productImageView;
    private Button selectImageButton;
    private Button addProductButton;

    private AddProductViewModel viewModel;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private Uri imageUri;

    // Inicjalizacja fragmentu
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_product, container, false);

        productNameEditText = view.findViewById(R.id.productName);
        productQuantityEditText = view.findViewById(R.id.productQuantity);
        productPriceEditText = view.findViewById(R.id.productPrice);
        productImageView = view.findViewById(R.id.productImagePreview);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        addProductButton = view.findViewById(R.id.addProductButton);

        viewModel = new ViewModelProvider(this).get(AddProductViewModel.class);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sprawdź uprawnienia do dostępu do pamięci zewnętrznej
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Jeśli uprawnienia nie zostały jeszcze przyznane, poproś użytkownika o nie
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_EXTERNAL_STORAGE);
                } else {
                    // Jeśli uprawnienia są już udzielone, otwórz galerię
                    openFileChooser();
                }
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pobierz dane wprowadzone przez użytkownika
                String productName = productNameEditText.getText().toString();
                int productQuantity = Integer.parseInt(productQuantityEditText.getText().toString());
                double productPrice = Double.parseDouble(productPriceEditText.getText().toString());

                // Sprawdź, czy został wybrany obraz
                if (imageUri == null) {
                    // Wyświetl komunikat użytkownikowi o konieczności wybrania obrazu
                    Toast.makeText(getContext(), "Wybierz obraz produktu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Utwórz obiekt produktu
                Products product = new Products(productName, productPrice, productQuantity);

                // Dodaj produkt przy użyciu ViewModel
                File imageFile = new File(getRealPathFromURI(imageUri));
                viewModel.addProduct(product, imageFile, new Retrofit.AddProductCallback() {
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

    // Otwieranie galerii do wyboru obrazu
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Pobierz rzeczywistą ścieżkę pliku z URI
    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(productImageView);
            productImageView.setVisibility(View.VISIBLE);
            selectImageButton.setVisibility(View.GONE);
            addProductButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            // Sprawdź, czy użytkownik przyznał uprawnienia
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uprawnienia zostały udzielone, otwórz galerię
                openFileChooser();
            } else {
                // Uprawnienia nie zostały udzielone, wyświetl komunikat
                Toast.makeText(getContext(), "Brak uprawnień do dostępu do pamięci zewnętrznej", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
