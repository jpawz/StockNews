package pl.pjask.stocknews.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.pjask.stocknews.models.NewsModel;
import pl.pjask.stocknews.models.Stock;


/**
 * Parser for bankier.pl website.
 */
public class BankierParser {
    private static final String BANKIER_SYMBOLS_URL = "http://www.bankier.pl/gielda/notowania/akcje";
    private static final String SYMBOL_ROW_CLASS = ".colWalor";

    private static final String BANKIER_NEWS_URL_TEMPLATE = "http://www.bankier.pl/inwestowanie/profile/quote.html?symbol=";
    private static final String NEWS_CLASS = "boxContent boxList";

    /**
     * Returns header of news for given symbol.
     *
     * @param symbol symbol name for example: KGHM, PGE, PKOBP...
     * @return List of NewsModels
     * @throws IOException when website can't parsed.
     */
    public List<NewsModel> getNews(String symbol) throws IOException {
        List<NewsModel> newsModels = new ArrayList<>();

        Document document = Jsoup.connect(BANKIER_NEWS_URL_TEMPLATE + symbol).get();
        Elements boxes = document.getElementsByClass(NEWS_CLASS);
        Element newsElement = boxes.get(0);
        Elements newsElements = newsElement.getElementsByTag("li");

        for (Element element : newsElements) {
            newsModels.add(new NewsModel(element.text()));
        }

        return newsModels;
    }

    /**
     * Returns all symbols as Set from bankier website.
     *
     * @return set of symbols.
     * @throws IOException when website can't be parsed.
     */
    public Set<String> getSymbols() throws IOException {
        Document document = Jsoup.connect(BANKIER_SYMBOLS_URL).get();

        Elements rows = document.select("tbody").select(SYMBOL_ROW_CLASS);

        Set<String> symbols = new TreeSet<>();
        for (Element row : rows) {
            symbols.add(StringUtils.strip(row.getElementsByTag("a").text(), " "));
        }

        return symbols;
    }

    public Map<String, Stock> getStockMap() throws IOException {
        Document document = Jsoup.connect(BANKIER_SYMBOLS_URL).get();

        Elements rows = document.select("tbody").select(SYMBOL_ROW_CLASS);

        Map<String, Stock> symbols = new HashMap<>();
        for (Element row : rows) {
            String symbol = StringUtils.strip(row.getElementsByTag("a").text(), " ");
            Stock stock = new Stock(symbol);
            stock.setStockFullName(row.getElementsByTag("a").attr("title"));

            symbols.put(symbol, stock);
        }

        return symbols;
    }
}
