package bug.busters.ui.slideshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import bug.busters.orders.Order;
import bug.busters.R;

/**
 * Klasa adaptera do wyświetlania zamówień użytkownika
 */
public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;
    private double totalPrice = 0.0;

    /**
     * Konstruktor klasy
     * @param context Kontekst
     * @param orders Zamówienia
     */
    public UserOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    /**
     * Tworzy nowy obiekt ViewHolder
     * @param parent ViewGroup, do którego zostanie dodany nowy View po powiązaniu z pozycją adaptera
     * @param viewType Ttyp widoku nowego widoku
     * @return Nowy obiekt ViewHolder
     */
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_order, parent, false);
        return new OrderViewHolder(view);
    }

    /**
     * Bindowanie danych do widoku
     * @param holder ViewHolder, który powinien zostać zaktualizowany, aby reprezentował zawartość tem w danej pozycji w zestawie danych
     * @param position Pozycja elementu w zestawie danych adaptera
     */
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Sprawdzamy, czy bieżące zamówienie jest pierwszym wystąpieniem danego id_order
        if (position == 0 || order.getIdOrder() != orders.get(position - 1).getIdOrder()) {
            // Jeśli tak, to wyświetlamy informacje dotyczące nowego zamówienia
            holder.tvOrderId.setText(String.valueOf(order.getIdOrder()));
            holder.tvName.setText(order.getImie());
            holder.tvSurname.setText(order.getNazwisko());
            holder.tvPhoneNumber.setText(order.getNrTel());

            // Resetujemy totalPrice dla nowego zamówienia
            totalPrice = 0.0;
        } else {
            // W przeciwnym razie ukrywamy niektóre pola, ponieważ są to powtórzenia tych samych danych zamówienia
            holder.tvOrderId.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.GONE);
            holder.tvSurname.setVisibility(View.GONE);
            holder.tvPhoneNumber.setVisibility(View.GONE);
        }

        // Wyświetlamy informacje dotyczące produktu i ilości dla każdej pozycji zamówienia
        holder.tvProductName.setText(order.getProductName());
        holder.tvQuantity.setText("Ilość: " + String.valueOf(order.getQuantity()));

        // Dodajemy cenę bieżącego produktu do totalPrice
        totalPrice += order.getTotalPrice();

        // Sprawdzamy, czy bieżący indeks jest równy ostatniemu indeksowi dla zamówienia o tym samym id_order
        int lastIndex = getLastIndexForOrderId(order.getIdOrder());
        if (position == lastIndex) {
            // Jeśli tak, wyświetlamy sumę cen wszystkich pozycji zamówienia
            holder.tvStatus.setText("Status: " + order.getStatus());
            holder.tvTotalPrice.setText("Cena do załpaty: " + String.valueOf(totalPrice) + " zł");
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvTotalPrice.setVisibility(View.VISIBLE);
        } else {
            // W przeciwnym razie ukrywamy pole sumy cen oraz Spinner i Button
            holder.tvTotalPrice.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.GONE);
        }
    }

    /**
     * Pobieranie indeksu ostatniego wystąpienia danego id_order
     * @param idOrder id_order, dla którego szukamy indeksu
     * @return Indeks ostatniego wystąpienia danego id_order
     */
    private int getLastIndexForOrderId(int idOrder) {
        int lastIndex = 0;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getIdOrder() == idOrder) {
                lastIndex = i;
            }
        }
        return lastIndex;
    }

    /**
     * Pobieranie liczby elementów w zestawie danych
     * @return Liczba elementów w zestawie danych
     */
    @Override
    public int getItemCount() {
        return orders.size();
    }

    /**
     * Klasa ViewHolder, która przechowuje widoki elementów widoku
     */
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvName, tvSurname, tvPhoneNumber, tvStatus, tvProductName, tvQuantity, tvTotalPrice;

        /**
         * Konstruktor klasy
         * @param itemView Widok elementu widoku
         */
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvName = itemView.findViewById(R.id.tvName);
            tvSurname = itemView.findViewById(R.id.tvSurname);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
