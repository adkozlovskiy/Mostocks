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
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.services.CostMonitorService;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.lang.reflect.Type;
import java.util.List;

import static com.kozlovskiy.mostocks.ui.splash.SplashActivity.KEY_STOCKS_INTENT;

public class StocksFragment extends Fragment
        implements StocksView {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    public static final String TAG = StocksFragment.class.getSimpleName();
    private StocksPresenter stocksPresenter;
    private StocksAdapter stocksAdapter;
    private LinearLayoutManager llm;
    CostMonitorService costMonitorService;
    public static final String BROADCAST_ACTION = CostMonitorService.QuoteBinder.class.getCanonicalName();

    boolean bound = false;
    ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "onServiceConnected: ");
            CostMonitorService.QuoteBinder quoteBinder = (CostMonitorService.QuoteBinder) binder;
            costMonitorService = quoteBinder.getInstance();
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            bound = false;
        }
    };

    public StocksFragment() {
        super(R.layout.fragment_stocks);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Stock>>() {
        }.getType();
        String json = getArguments().getString(KEY_STOCKS_INTENT);
        List<Stock> stocks = gson.fromJson(json, type);
        stocksPresenter = new StocksPresenter(this, getContext(), stocks);
        llm = new LinearLayoutManager(getContext());
        stocksAdapter = new StocksAdapter(getContext(), false, null);

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(quoteReceiver, filter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(stocksAdapter);

        stocksPresenter.initializeStocks();
    }

    @Override
    public void showRetryDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), CostMonitorService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
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
        getActivity().unbindService(connection);
        bound = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stocksPresenter.unsubscribe();
        getActivity().unregisterReceiver(quoteReceiver);
    }

    BroadcastReceiver quoteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String symbol = intent.getStringExtra("symbol");
            double quote = intent.getDoubleExtra("quote", -1);

        }
    };
}