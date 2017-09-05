package pl.pjask.stocknews.NewsProviders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.pjask.stocknews.NewsModel;


/**
 * Parser for bankier.pl website.
 */
public class BankierNewsProvider {
    private static final String NEWS_CLASS = "boxContent boxList";

    /**
     * Returns header of news for given url.
     * @param url full url ex.: http://www.bankier.pl/inwestowanie/profile/quote.html?symbol=KGHM
     * @return List of NewsModels
     * @throws IOException when url is invalid.
     */
    public List<NewsModel> getNews(String url) throws IOException {
        List<NewsModel> newsModels = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Elements boxes = document.getElementsByClass(NEWS_CLASS);
        Element newsElement = boxes.get(0);
        Elements newsElements = newsElement.getElementsByTag("li");

        for (Element element : newsElements) {
            newsModels.add(new NewsModel(element.text()));
        }

        return newsModels;
    }
}
