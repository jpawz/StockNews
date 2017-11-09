package pl.pjask.stocknews.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import pl.pjask.stocknews.models.NewsModel;

import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class BankierParserTest {

    final String symbol = "KGHM";
    final String fullName = "KGHM Polska Miedź SA";

    BankierParser mBankierParser;

    @Before
    public void setUp() {
        mBankierParser = new BankierParser();
    }

    @Test
    public void shouldReturnFiveItems() throws IOException {
        int numberOfArticlesOnMainPage = 5;
        assertTrue(mBankierParser.getArticles(symbol).size() == numberOfArticlesOnMainPage);
    }

    @Test(expected = IOException.class)
    public void exceptionShouldBeThrownWhenUrlIsWrong() throws Exception {
        mBankierParser.getArticles("unknownSymbol");
    }

    @Test
    public void symbolsShouldContainKGHM() throws IOException {
        assertTrue(mBankierParser.getSymbols().contains(symbol));
    }

    @Test
    public void checkFullNameOfSymbol() throws IOException {
        assertTrue(mBankierParser.getStockMap().get(symbol).getStockFullName().equals(fullName));
    }

    @Test
    public void checkUrl() throws IOException {
        NewsModel newsModel = mBankierParser.getArticles(symbol).get(0);

        assertThat(newsModel.getUrl()).startsWith("www.bankier.pl").endsWith(".html");
    }
}