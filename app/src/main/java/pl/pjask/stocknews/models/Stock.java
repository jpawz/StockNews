package pl.pjask.stocknews.models;

import android.support.annotation.NonNull;

public class Stock implements Comparable<Stock> {
    private final String stockSymbol;
    private String stockFullName;
    private boolean fetchNews;
    private boolean fetchEspi;
    private boolean fetchForum;

    public Stock(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public boolean fetchNews() {
        return fetchNews;
    }

    public void setFetchNews(boolean fetchNews) {
        this.fetchNews = fetchNews;
    }

    public boolean fetchEspi() {
        return fetchEspi;
    }

    public void setFetchEspi(boolean fetchEspi) {
        this.fetchEspi = fetchEspi;
    }

    public boolean fetchForum() {
        return fetchForum;
    }

    public void setFetchForum(boolean fetchForum) {
        this.fetchForum = fetchForum;
    }

    public String getStockFullName() {
        return stockFullName;
    }

    public void setStockFullName(String stockFullName) {
        this.stockFullName = stockFullName;
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
