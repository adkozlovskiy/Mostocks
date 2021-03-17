package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.kozlovskiy.mostocks.services.websocket.ClientWebSocket;
import com.kozlovskiy.mostocks.services.websocket.StockCostSocketConnection;

public class ChartPresenter implements ClientWebSocket.MessageListener {

    private final ChartView chartView;
    private StockCostSocketConnection stockCostSocketConnection;
    private Context context;

    public ChartPresenter(ChartView chartView, Context context) {
        this.chartView = chartView;
        this.context = context;

    }

    public void subscribe(String ticker) {
        stockCostSocketConnection = new StockCostSocketConnection(context, ticker);
        stockCostSocketConnection.setListener(this);
        stockCostSocketConnection.openConnection();
    }

    public void unsubscribe() {
        stockCostSocketConnection.closeConnection();
    }

    @Override
    public void onSocketMessage(String message) {
        Log.d("websocket", "onSocketMessage: " +message);
    }
}
