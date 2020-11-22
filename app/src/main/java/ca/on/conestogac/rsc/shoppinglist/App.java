package ca.on.conestogac.rsc.shoppinglist;

import android.app.Application;

import androidx.databinding.DataBindingUtil;

import ca.on.conestogac.rsc.shoppinglist.databinding.AppDataBindingComponent;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataBindingUtil.setDefaultComponent(new AppDataBindingComponent());
    }
}
