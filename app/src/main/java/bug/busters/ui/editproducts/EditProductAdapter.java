package bug.busters.ui.editproducts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bug.busters.products.Products;
import bug.busters.R;
import com.bumptech.glide.Glide;

public class EditProductAdapter extends RecyclerView.Adapter<EditProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<Products> productList;
    private final ProductClickListener clickListener;

    public interface ProductClickListener {
        void onProductClick(Products product);
    }

    public EditProductAdapter(Context context, ProductClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void updateProducts(List<Products> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_product_item_layout, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView productNameTextView;
        private final TextView productPriceTextView;
        private final TextView productQuantityTextView;
        private final ImageView productImageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.tvProductName);
            productPriceTextView = itemView.findViewById(R.id.tvProductPrice);
            productQuantityTextView = itemView.findViewById(R.id.tvProductQuantity);
            productImageView = itemView.findViewById(R.id.ivProductImage);

            itemView.setOnClickListener(this);
        }

        public void bind(Products product) {
            productNameTextView.setText(product.getNazwa());
            productPriceTextView.setText(String.format("%.2f zł", product.getCena()));
            productQuantityTextView.setText("Ilość: " + String.valueOf(product.getIlosc()));

            // Tworzenie pełnego adresu URL obrazu na serwerze
            String imageUrl = "http://192.168.0.48/images/" + product.getObraz();

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.meritum) // Placeholder obrazu
                    .error(R.drawable.meritum) // Obraz błędu
                    .into(productImageView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Products clickedProduct = productList.get(position);
                clickListener.onProductClick(clickedProduct);
            }
        }
    }
}
