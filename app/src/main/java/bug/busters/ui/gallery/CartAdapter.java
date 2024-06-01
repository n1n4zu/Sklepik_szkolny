package bug.busters.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bug.busters.cart.CartItem;
import bug.busters.R;

/**
 * Klasa adaptera do wyświetlania zawartości koszyka
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private OnDeleteClickListener onDeleteClickListener;

    /**
     * Konstruktor adaptera
     * @param context Kontekst
     * @param cartItems Lista produktów w koszyku
     */
    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    /**
     * Interfejs do obsługi kliknięcia przycisku Usuń
     */
    public interface OnDeleteClickListener {
        /**
         * Metoda wywoływana po kliknięciu przycisku Usuń
         * @param position Indeks produktu w koszyku
         */
        void onDeleteClick(int position);
    }

    /**
     * Ustawia listener do obsługi kliknięcia przycisku Usuń
     * @param listener Listener
     */
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }


    /**
     *
     * @param parent ViewGroup, do którego zostanie dodany nowy View po powiązaniu z pozycją adaptera
     * @param viewType Typ widoku nowego widoku.
     * @return Tworzony obiekt ViewHolder koszyka
     */
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    /**
     * Podstawia dane do widoku koszyka
     * @param holder ViewHolder koszyka
     * @param position Indeks produktu w koszyku
     */
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.tvProductName.setText(cartItem.getProduct().getNazwa());
        holder.tvProductQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.tvProductPrice.setText(String.valueOf(cartItem.getProduct().getCena()));

        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });
    }

    /**
     * Zwraca liczbę elementów w koszyku
     * @return Liczba elementów w koszyku
     */
    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    /**
     * Klasa ViewHolder koszyka
     */
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductQuantity, tvProductPrice;
        ImageView ivProductImage;
        Button btnDelete;

        /**
         * Konstruktor ViewHolder koszyka
         * @param itemView Widok koszyka
         */
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
