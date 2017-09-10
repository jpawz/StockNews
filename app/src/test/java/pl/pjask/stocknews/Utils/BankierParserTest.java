package pl.pjask.stocknews.Utils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;


public class BankierParserTest {

    final String symbol = "KGHM";

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

}