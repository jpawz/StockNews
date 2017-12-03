package pl.pjask.stocknews;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import pl.pjask.stocknews.models.Stock;
import pl.pjask.stocknews.utils.MenuUtils;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = LOLLIPOP, manifest = "/src/main/AndroidManifest.xml")
public class MenuUtilsTest {
    MenuUtils mMenuUtils;
    Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
        mMenuUtils = MenuUtils.getInstance(context);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSymbolsInsertion() {
        String symbolName1 = "KGHM";
        String symbolName2 = "PGE";
        Stock stock1 = new Stock(symbolName1);
        Stock stock2 = new Stock(symbolName2);

        mMenuUtils.addSymbol(stock1);
        mMenuUtils.addSymbol(stock2);

        assertTrue("symbols added and received from mMenuUtils are different",
                mMenuUtils.getSymbolNames().contains(symbolName1) &&
                        mMenuUtils.getSymbolNames().contains(symbolName2) &&
                        mMenuUtils.getSymbolNames().size() == 2);
    }

    @Test
    public void testRemoveSymbol() {
        String symbolName1 = "KGHM";
        String symbolName2 = "PGE";
        Stock stock1 = new Stock(symbolName1);
        Stock stock2 = new Stock(symbolName2);
        mMenuUtils.addSymbol(stock1);
        mMenuUtils.addSymbol(stock2);

        mMenuUtils.removeSymbol(symbolName1);

        assertTrue("symbol is not removed from mMenuUtils",
                mMenuUtils.getSymbolNames().size() == 1 &&
                        mMenuUtils.getSymbolNames().contains(symbolName2));
    }

}