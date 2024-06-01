package bug.busters.orders;

/**
 * Klasa przechowująca dane logowwania
 */
public class Order {
    private int id_order;
    private String imie;
    private String nazwisko;
    private String nr_tel;
    private String status;
    private String product_name;
    private int quantity;
    private double total_price;

    /**
     * Getter pola id_order
     * @return id_order
     */
    public int getIdOrder() {
        return id_order;
    }

    /**
     * Getter pola imie
     * @return imie
     */
    public String getImie() {
        return imie;
    }

    /**
     * Getter pola nazwisko
     * @return nazwisko
     */
    public String getNazwisko() {
        return nazwisko;
    }

    /**
     * Getter pola nr_tel
     * @return nr_tel
     */
    public String getNrTel() {
        return nr_tel;
    }

    /**
     * Getter pola status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter pola product_name
     * @return product_name
     */
    public String getProductName() {
        return product_name;
    }

    /**
     * Getter pola quantity
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Getter pola total_price
     * @return total_price
     */
    public double getTotalPrice() {
        return total_price;
    }
}
