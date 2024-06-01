package bug.busters.users;

/**
 * Klasa przechowująca dane użytkownika
 */
public class Users {
    private int id;
    private String imie;
    private String nazwisko;
    private String email;
    private String nr_tel;
    private String pass;
    private char rola;

    /**
     * Getter pola id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter pola rola
     * @return rola
     */
    public char getRola() {
        return rola;
    }
}
