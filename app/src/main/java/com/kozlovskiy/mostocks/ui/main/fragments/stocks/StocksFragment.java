package com.kozlovskiy.mostocks.ui.main.fragments.stocks;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.services.WebSocketService;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.kozlovskiy.mostocks.ui.splash.SplashActivity.KEY_STOCKS_INTENT;

public class StocksFragment extends Fragment
        implements StocksView {

    public static final String BROADCAST_ACTION = WebSocketService.class.getCanonicalName();
    public static final String TAG = StocksFragment.class.getSimpleName();

    private StocksPresenter stocksPresenter;
    private StocksAdapter stocksAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager llm;
    private List<String> symbols;
    private Context context;
    private Gson gson;
    private Type type;
    boolean bound = false;

    ServiceConnection connection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            WebSocketService.QuoteBinder quoteBinder = (WebSocketService.QuoteBinder) binder;
            WebSocketService webSocketService = quoteBinder.getInstance();
            webSocketService.bindSocket(symbols);
            Log.d(TAG, "onServiceConnected: ");
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    public StocksFragment() {
        super(R.layout.fragment_stocks);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        llm = new LinearLayoutManager(context);
        stocksAdapter = new StocksAdapter(context, false, null);

        gson = new Gson();
        type = new TypeToken<List<Stock>>() {
        }.getType();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(stocksAdapter);

        if (getArguments() != null) {
            String json = getArguments().getString(KEY_STOCKS_INTENT);
            List<Stock> stocks = gson.fromJson(json, type);

            symbols = new ArrayList<>();
            for (Stock stock : stocks) {
                symbols.add(stock.getSymbol());
            }

            stocksPresenter = new StocksPresenter(this, context, stocks);
            stocksPresenter.initializeStocks();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        Intent intent = new Intent(getContext(), WebSocketService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        context.registerReceiver(quoteReceiver, filter);
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void updateStocks(List<Stock> stocks) {
        stocksAdapter.updateStocks(stocks);

        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        context.unbindService(connection);
        context.unregisterReceiver(quoteReceiver);
        stocksPresenter.unsubscribe();
    }

    private final BroadcastReceiver quoteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String symbol = intent.getStringExtra("symbol");
            double quote = intent.getDoubleExtra("quote", -1);

            Log.d(TAG, "onReceive: symbol " + symbol + ", quote" + quote);

        }
    };
}