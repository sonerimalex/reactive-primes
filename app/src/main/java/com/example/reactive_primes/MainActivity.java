package com.example.reactive_primes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity {

    long defaultRange = 100L;
    long startTime;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.range)
    EditText range;

    @BindView(R.id.action_primes)
    Button actionPrimes;

    @BindView(R.id.action_spectres)
    Button actionSpectres;

    @BindView(R.id.action_toppings)
    Button actionToppings;

    @BindView(R.id.list_primes)
    RecyclerView listPrimes;

    @BindView(R.id.list_spectres)
    RecyclerView listSpectres;

    @BindView(R.id.list_toppings)
    RecyclerView listToppings;

    @OnClick(R.id.action_primes)
    public void onActionPrimes(){
        listPrimes.setVisibility(View.VISIBLE);
        listSpectres.setVisibility(View.GONE);
        listToppings.setVisibility(View.GONE);
    }

    @OnClick(R.id.action_spectres)
    public void onActionSpectres(){
        listPrimes.setVisibility(View.GONE);
        listSpectres.setVisibility(View.VISIBLE);
        listToppings.setVisibility(View.GONE);
    }

    @OnClick(R.id.action_toppings)
    public void onActionToppings(){
        listPrimes.setVisibility(View.GONE);
        listSpectres.setVisibility(View.GONE);
        listToppings.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view){
        Date date = new Date();
        startTime = date.getTime();
        Completable.fromAction(this::reactivePrimes).andThen(Completable.fromAction(this::updateList)).subscribe();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        range.setText(Long.toString(defaultRange));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean menuEnable = false;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return menuEnable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Consumer<HashMap<Long, Long>> reactivePrimes() {
        ReplaySubject<Spectre> topping = ReplaySubject.create();
        ReplaySubject<Spectre> t1 = ReplaySubject.create();
        ReplaySubject<Spectre> t2 = ReplaySubject.create();
        ReplaySubject<Spectre> t11 = ReplaySubject.create();
        ReplaySubject<Spectre> t21 = ReplaySubject.create();
        topping.subscribe(data -> {
            t1.onNext(data);
            t2.onNext(data);
        });
        t1.subscribe(data -> {
            t11.onNext(data);
        });
        t2.subscribe(data -> {
            t21.onNext(data);
        });
        Observable<Spectre> result = Observable.zip(t11, t21, (spectre1, spectre2) -> spectre1);
        result.subscribe(data -> {
            Log.d("RESULT", data.toString());
        });
        Observable.range(0, 100).subscribe(index -> {
            topping.onNext(new Spectre(index));
        });
        return null;
    }

    private Observable<DataSnapshot> getTopping(long key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("toppings").child(Long.toString(key)).getRef();
        Observable<DataSnapshot> result = RxFirebaseDatabase.observeSingleValueEvent(ref).toObservable();
        return result.map(data -> {
            return data;
        });
    }

    public void updateList() {
    }

}
