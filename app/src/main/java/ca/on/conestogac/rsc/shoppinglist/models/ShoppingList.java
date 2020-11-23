package ca.on.conestogac.rsc.shoppinglist.models;

import java.io.Serializable;

public class ShoppingList implements Serializable {
    private String title;

    public ShoppingList(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }
}
