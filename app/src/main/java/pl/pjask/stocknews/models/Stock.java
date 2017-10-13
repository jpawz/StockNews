package pl.pjask.stocknews.models;

import android.support.annotation.NonNull;

public class Stock implements Comparable<Stock> {
    private final String stockSymbol;


    public Stock(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }


    @Override
    public String toString() {
        return stockSymbol;
    }


    @Override
    public int compareTo(@NonNull Stock stock) {
        return stockSymbol.compareTo(stock.getStockSymbol());
    }
}
