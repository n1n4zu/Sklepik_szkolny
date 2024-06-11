package bug.busters.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import bug.busters.products.Products;
import bug.busters.R;
import bug.busters.cart.CartManager;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Products> productList;
    private Context context;

    public ProductAdapter(Context context, List<Products> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productList.get(position);
        holder.tvProductName.setText(product.getNazwa());
        holder.tvProductQuantity.setText("Ilość: " + String.valueOf(product.getIlosc()));
        holder.tvProductPrice.setText("Cena: " + String.format("%.2f zł", product.getCena()));

        // Tworzenie pełnego adresu URL obrazu na serwerze
        String imageUrl = "http://192.168.0.48/images/" + product.getObraz();

        // Ładowanie obrazu przy użyciu Glide z adresu URL
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.meritum) // Opcjonalnie: obrazek placeholdera
                .error(R.drawable.meritum) // Opcjonalnie: obrazek błędu
                .into(holder.ivProductImage);

        holder.buttonAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product, new CartManager.AddToCartCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, product.getNazwa() + " dodano do koszyka", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(context, "Nie udało się dodać produktu do koszyka: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProducts(List<Products> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductQuantity, tvProductPrice;
        ImageView ivProductImage;
        Button buttonAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            buttonAddToCart = itemView.findViewById(R.id.button2);
        }
    }
}
