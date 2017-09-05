package pl.pjask.stocknews.NewsProviders;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;


public class BankierNewsProviderTest {

    String url = "http://www.bankier.pl/inwestowanie/profile/quote.html?symbol=KGHM";

    BankierNewsProvider mNewsProvider;

    @Before
    public void setUp() {
        mNewsProvider = new BankierNewsProvider();
    }


    @Test
    public void shouldReturnFiveItems() throws IOException {
        assertTrue(mNewsProvider.getNews(url).size() == 5);
    }

    @Test(expected = IOException.class)
    public void exceptionShouldBeThrownWhenUrlIsWrong() throws Exception {
        mNewsProvider.getNews("http://www.bankier.pl/inwestowanie/profile/quote.html?symbol=unknownSymbol");
    }

}