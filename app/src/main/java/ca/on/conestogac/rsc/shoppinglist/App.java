package ca.on.conestogac.rsc.shoppinglist;

import android.app.Application;

import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.data.source.ApplicationDatabase;

public class App extends Application {

    private ApplicationDbRepository repository;

    public ApplicationDbRepository getRepository() {
        return repository;
    }

    @Override
    public void onCreate() {
        repository = new ApplicationDbRepository(ApplicationDatabase.getInstance(getApplicationContext()));
        super.onCreate();
    }
}
