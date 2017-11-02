package pl.pjask.stocknews.models;

public class NewsModel {

    private final String title;
    private String stockSymbol;
    private String url;
    private boolean visited;

    public NewsModel(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
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
