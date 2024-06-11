package bug.busters.products;

/**
 * Klasa przechowująca dane o produktach
 */
public class Products {
    private int id_product;
    private String nazwa;
    private int ilosc;
    private double cena;
    private String obraz;

    public Products(int id, String nazwa, double cena, int ilosc) {
        this.id_product = id;
        this.nazwa = nazwa;
        this.cena = cena;
        this.ilosc = ilosc;
    }

    public Products(String nazwa, double cena, int ilosc, String obraz) {
        this.nazwa = nazwa;
        this.cena = cena;
        this.ilosc = ilosc;
    }


    /**
     * Getter pola id_product
     * @return id_product
     */
    public int getId_product() {
        return id_product;
    }

    /**
     * Getter pola nazwa
     * @return nazwa
     */
    public String getNazwa() {
        return nazwa;
    }

    /**
     * Getter pola ilosc
     * @return ilosc
     */
    public int getIlosc() {
        return ilosc;
    }

    /**
     * Getter pola cena
     * @return cena
     */
    public double getCena() {
        return cena;
    }

    /**
     * Getter pola obraz
     * @return obraz
     */
    public String getObraz() {
        return obraz;
    }

    /**
     * Setter pola obraz
     * @param obraz
     */
    public void setObraz(String obraz) {
        this.obraz = obraz;
    }
}
