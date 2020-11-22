package ca.on.conestogac.rsc.shoppinglist.models;

import java.util.ArrayList;
import java.util.List;

public class ListItem {
    private String title;
    private final List<ProductItem> items;

    public ListItem(String title) {
        this.title = title;
        this.items = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return items.size(); // TODO: change this later
    }

    public int getSize() {
        return items.size();
    }

    public void setTitle(String name) {
        this.title = name;
    }
}
