package bug.busters.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import bug.busters.orders.Order;
import bug.busters.R;
import bug.busters.api.Retrofit;

/**
 * Klasa adaptera dla RecyclerView do wyświetlania zamówień
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;
    private double totalPrice = 0.0;
    private Retrofit retrofit;

    /**
     * Konstruktor adaptera
     * @param context Kontekst
     * @param orders Lista zamówień
     */
    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.retrofit = new Retrofit();
    }

    /**
     * Metoda do tworzenia nowego widoku ViewHolder
     * @param parent ViewGroup, do którego zostanie dodany nowy View po powiązaniu z pozycją adaptera
     * @param viewType Typ widoku nowego widoku
     * @return OrderViewHolder nowy obiekt ViewHolder
     */
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    /**
     * Metoda do aktualizacji zawartości widoku ViewHolder
     * @param holder ViewHolder, który powinien zostać zaktualizowany, aby reprezentował zawartość elementu w danej pozycji w zestawie danych
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
            holder.tvPhoneNumber.setText("Nr telefonu: " + order.getNrTel());

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
            holder.tvTotalPrice.setText("Cena do zapłaty: " + String.format("%.2f zł", totalPrice));
            holder.tvTotalPrice.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.VISIBLE);

            // Wyświetlamy Spinner i Button pod sumą cen
            holder.spinnerStatus.setVisibility(View.VISIBLE);
            holder.btnUpdateStatus.setVisibility(View.VISIBLE);

            // Tworzymy adapter dla Spinnera
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    R.array.status_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinnerStatus.setAdapter(adapter);

            // Tworzymy listenera dla Buttona
            holder.btnUpdateStatus.setOnClickListener(v -> {
                String newStatus = holder.spinnerStatus.getSelectedItem().toString();
                retrofit.changeOrderStatus(order.getIdOrder(), newStatus, new Retrofit.StatusUpdateCallback() {
                    /**
                     * Metoda wywoływana po udanej zmianie statusu
                     */
                    @Override
                    public void onSuccess() {
                    }

                    /**
                     * Metoda wywoływana w przypadku błędu
                     * @param t Wyjątek
                     */
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            });
        } else {
            // W przeciwnym razie ukrywamy pole sumy cen oraz Spinner i Button
            holder.tvTotalPrice.setVisibility(View.GONE);
            holder.spinnerStatus.setVisibility(View.GONE);
            holder.btnUpdateStatus.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.GONE);
        }
    }

    /**
     * Metoda do pobierania ostatniego indeksu dla danego id_order
     * @param idOrder id_order
     * @return Ostatni indeks dla danego id_order
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
     * Metoda do zwracania liczby elementów w zestawie danych adaptera
     * @return Liczba elementów w zestawie danych adaptera
     */
    @Override
    public int getItemCount() {
        return orders.size();
    }

    /**
     * Klasa ViewHolder dla widoku zamówienia
     */
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvName, tvSurname, tvPhoneNumber, tvStatus, tvProductName, tvQuantity, tvTotalPrice;
        Spinner spinnerStatus;
        Button btnUpdateStatus;

        /**
         * Konstruktor ViewHoldera
         * @param itemView widok
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
            spinnerStatus = itemView.findViewById(R.id.spinnerStatus);
            btnUpdateStatus = itemView.findViewById(R.id.btnChangeStatus);
        }
    }
}
