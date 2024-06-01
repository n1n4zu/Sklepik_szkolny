package bug.busters.orders;

import java.util.List;

import bug.busters.cart.CartItem;

/**
 * Klasa obsługująca zamówienia
 */
public class OrderRequest {
    private List<CartItem> cartItems;
    private int userId;

    /**
     * Konstruktor
     * @param cartItems Lista produktów
     * @param userId ID użytkownika
     */
    public OrderRequest(List<CartItem> cartItems, int userId) {
        this.cartItems = cartItems;
        this.userId = userId;
    }
}
