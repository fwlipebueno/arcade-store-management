package br.edu.felipebueno.arcade.domain.model;

public class Inventory {

    public boolean hasAvailableStock(Product product, int quantity) {
        return product != null && product.hasStock(quantity);
    }

    public void reserve(Product product, int quantity) {
        product.removeStock(quantity);
    }

    public void release(Product product, int quantity) {
        product.addStock(quantity);
    }
}
