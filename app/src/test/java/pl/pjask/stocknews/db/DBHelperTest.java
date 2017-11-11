package pl.pjask.stocknews.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import pl.pjask.stocknews.BuildConfig;
import pl.pjask.stocknews.db.DBSchema.SymbolHintTable;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static pl.pjask.stocknews.db.DBSchema.MenuTable;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = LOLLIPOP, manifest = "src/main/AndroidManifest.xml")
public class DBHelperTest {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getReadableDatabase();
    }

    @After
    public void cleanup() {
        db.close();
    }

    @Test
    public void tesdDbCreated() {
        assertTrue("DB didn't open", db.isOpen());
        db.close();
    }

    @Test
    public void checkMenuCols() {
        Cursor cursor = db.query(MenuTable.NAME, null, null, null, null, null, null);
        assertNotNull(cursor);

        String[] dbCols = cursor.getColumnNames();
        Field[] dataCols = MenuTable.Cols.class.getFields();

        assertEquals(dataCols.length, dbCols.length);
    }


    @Test
    public void checkCols() {
        Cursor cursor = db.query(DBSchema.ArticlesTable.NAME, null, null, null, null, null, null);
        assertNotNull(cursor);


        assertEquals("not all newsTable columns were created",
                DBSchema.ArticlesTable.Cols.class.getFields().length, cursor.getColumnNames().length);

    }

    @Test
    public void checkHintsCols() {
        Cursor cursor = db.query(SymbolHintTable.NAME, null, null, null, null, null, null);
        assertNotNull(cursor);


        assertEquals("not all SymbolHintTable columns were created",
                SymbolHintTable.Cols.class.getFields().length, cursor.getColumnNames().length);

    }
}