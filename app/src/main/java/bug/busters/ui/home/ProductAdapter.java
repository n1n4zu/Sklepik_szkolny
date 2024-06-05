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

/**
 * Klasa adaptera do wyświetlania produktów
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Products> productList;
    private Context context;

    /**
     * Konstruktor
     * @param context Kontekst
     * @param productList Lista produktów
     */
    public ProductAdapter(Context context, List<Products> productList) {
        this.context = context;
        this.productList = productList;
    }

    /**
     * Tworzy nowy ViewHolder
     * @param parent ViewGroup
     * @param viewType Typ widoku
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    /**
     * Podłącza dane do widoku
     * @param holder ViewHolder
     * @param position Pozycja
     */
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productList.get(position);
        holder.tvProductName.setText(product.getNazwa());
        holder.tvProductQuantity.setText("Ilość: " + String.valueOf(product.getIlosc()));
        holder.tvProductPrice.setText("Cena: " + String.valueOf(product.getCena()) + " zł");

        String imageName = product.getObraz();
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        if (resId != 0) {
            Glide.with(context)
                    .load(resId)
                    .placeholder(R.drawable.meritum) // opcjonalnie placeholder
                    .error(R.drawable.meritum) // opcjonalnie obraz błędu
                    .into(holder.ivProductImage);
        } else {
            Glide.with(context)
                    .load(R.drawable.meritum)
                    .into(holder.ivProductImage);
        }

        holder.buttonAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(context, product.getNazwa() + " dodano do koszyka", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Zwraca ilość elementów w liście
     * @return Liczba elementów
     */
    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * Aktualizuje listę produktów
     * @param products Lista produktów
     */
    public void updateProducts(List<Products> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    /**
     * Klasa ViewHolder
     */
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductQuantity, tvProductPrice;
        ImageView ivProductImage;
        Button buttonAddToCart;

        /**
         * Konstruktor
         * @param itemView Widok
         */
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
