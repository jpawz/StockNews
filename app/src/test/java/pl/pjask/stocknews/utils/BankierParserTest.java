package pl.pjask.stocknews.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;


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
        assertTrue(mBankierParser.getNews(symbol).size() == 5);
    }

    @Test(expected = IOException.class)
    public void exceptionShouldBeThrownWhenUrlIsWrong() throws Exception {
        mBankierParser.getNews("unknownSymbol");
    }

    @Test
    public void symbolsShouldContainKGHM() throws IOException {
        assertTrue(mBankierParser.getSymbols().contains(symbol));
    }

    @Test
    public void checkFullNameOfSymbol() throws IOException {
        assertTrue(mBankierParser.getStockMap().get(symbol).getStockFullName().equals(fullName));
    }

}