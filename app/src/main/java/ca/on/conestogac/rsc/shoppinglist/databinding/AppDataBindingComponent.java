package ca.on.conestogac.rsc.shoppinglist.databinding;

public class AppDataBindingComponent implements androidx.databinding.DataBindingComponent {
    @Override
    public RecyclerViewDataBinding getRecyclerViewDataBinding() {
        return new RecyclerViewDataBinding();
    }
}
