package ca.on.conestogac.rsc.shoppinglist.models;

public class ProductItem {
    private boolean checked;
    private String title;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
