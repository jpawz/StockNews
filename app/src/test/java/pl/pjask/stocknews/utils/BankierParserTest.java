package pl.pjask.stocknews.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import pl.pjask.stocknews.models.ArticleModel;
import pl.pjask.stocknews.models.Stock;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.assertTrue;

public class BankierParserTest {

    final Stock symbol = new Stock("KGHM");
    final String exemplarySymbol = "KGHM";
    final String fullName = "KGHM Polska Miedź SA";

    BankierParser mBankierParser;

    @Before
    public void setUp() {
        mBankierParser = new BankierParser();
        symbol.setFetchNews(true);
        symbol.setFetchEspi(false);
    }

    @Test
    public void shouldReturnFiveItems() throws IOException {
        int numberOfArticlesOnMainPage = 5;
        assertTrue(mBankierParser.getArticles(symbol).size() == numberOfArticlesOnMainPage);
    }

    @Test(expected = IOException.class)
    public void exceptionShouldBeThrownWhenUrlIsWrong() throws Exception {
        Stock unknownStock = new Stock("unknown");
        unknownStock.setFetchNews(true);
        mBankierParser.getArticles(unknownStock);
    }

    @Test
    public void symbolsShouldContainKGHM() throws IOException {
        assertTrue(mBankierParser.getSymbols().contains(symbol.getStockSymbol()));
    }

    @Test
    public void checkFullNameOfSymbol() throws IOException {
        assertTrue(mBankierParser.getStockMap().get(symbol.getStockSymbol()).getStockFullName().equals(fullName));
    }

    @Test
    public void checkUrl() throws IOException {
        ArticleModel articleModel = mBankierParser.getArticles(symbol).get(0);

        assertThat(articleModel.getUrl()).startsWith("http://www.bankier.pl");
        assertThat(articleModel.getUrl()).endsWith(".html");
    }

    @Test
    public void checkDateString() throws IOException {
        String datePattern = "....-..-..";
        ArticleModel articleModel = mBankierParser.getArticles(symbol).get(0);

        assertThat(articleModel.getDate()).matches(datePattern);
    }
}