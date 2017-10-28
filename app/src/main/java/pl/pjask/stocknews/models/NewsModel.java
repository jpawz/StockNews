package pl.pjask.stocknews.models;

public class NewsModel {

    private final String title;
    private String stockSymbol;

    public NewsModel(String title) {
        this.title = title;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getTitle() {
        return title;
    }

}
