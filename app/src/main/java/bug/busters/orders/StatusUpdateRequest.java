package bug.busters.orders;


/**
 * Klasa reprezentująca żądanie aktualizacji statusu zamówienia
 */
public class StatusUpdateRequest {
    private int id_order;
    private String newStatus;

    /**
     * Konstruktor, dzięki któremu klasa przechowuje informaje o ID zamówienia i nowym statusie
     * @param id_order ID zamówienia
     * @param newStatus Nowy status
     */
    public StatusUpdateRequest(int id_order, String newStatus) {
        this.id_order = id_order;
        this.newStatus = newStatus;
    }
}
