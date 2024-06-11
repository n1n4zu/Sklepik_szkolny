package bug.busters.ui.addnewproduct;

import androidx.lifecycle.ViewModel;

import bug.busters.api.Retrofit;
import bug.busters.products.Products;

public class AddProductViewModel extends ViewModel {

    private Retrofit retrofit;

    public AddProductViewModel() {
        retrofit = new Retrofit();
    }

    public void addProduct(Products product, Retrofit.AddProductCallback callback) {
        retrofit.addProduct(product, new Retrofit.OrderCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
