package pl.pjask.stocknews.models;

public class NewsModel {

    private String title;

    public NewsModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
