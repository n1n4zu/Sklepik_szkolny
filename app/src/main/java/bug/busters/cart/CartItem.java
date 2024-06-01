package bug.busters.cart;

import bug.busters.products.Products;

/**
 * Klasa pomocnicza do klasy OrderRequeust
 * Przechowuje informacje o produktach i ich ilości
 */
public class CartItem {
    private Products product;
    private int quantity;

    /**
     * Konstruktor klasy CartItem
     * @param product Informacje o produktach
     * @param quantity Ilość produktów
     */
    public CartItem(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Getter pola product
     * @return product
     */
    public Products getProduct() {
        return product;
    }

    /**
     * Getter pola quantity
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Setter pola quantity
     * @param quantity Ilość
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
