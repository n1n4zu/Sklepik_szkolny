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
        holder.tvProductPrice.setText("Cena: " + String.valueOf(product.getCena()) + " zł");

        // Get resource identifier from string
        String imageName = product.getObraz() + ".jpg";
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        // Set image resource
        if (resId != 0) { // 0 means not found
            holder.ivProductImage.setImageResource(resId);
        } else {
            // Handle the case where the image resource is not found
            holder.ivProductImage.setImageResource(R.drawable.meritum); // Use a placeholder image
        }

        holder.buttonAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            // You can add a Toast message or update UI to notify the user
            Toast.makeText(context, product.getNazwa() + " dodano do koszyka", Toast.LENGTH_SHORT).show();
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
