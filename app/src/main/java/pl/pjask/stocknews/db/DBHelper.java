package pl.pjask.stocknews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import pl.pjask.stocknews.db.DBSchema.SymbolHintTable;
import pl.pjask.stocknews.utils.FileUtils;

import static pl.pjask.stocknews.db.DBSchema.ArticlesTable;
import static pl.pjask.stocknews.db.DBSchema.MenuTable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "stocknews.db";
    private static DBHelper instance;
    private static Context context;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        DBHelper.context = context;
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MenuTable.NAME + "(" +
                MenuTable.Cols.ID + " INTEGER PRIMARY KEY," +
                MenuTable.Cols.SYMBOL_NAME + " TEXT NOT NULL UNIQUE, " +
                MenuTable.Cols.FETCH_NEWS + " INTEGER," +
                MenuTable.Cols.FETCH_ESPI + " INTEGER" +
                ")"
        );

        db.execSQL("create table " + ArticlesTable.NAME + "(" +
                ArticlesTable.Cols.ID + " INTEGER PRIMARY KEY," +
                ArticlesTable.Cols.MENU_ID + " INTEGER REFERENCES " + MenuTable.NAME + ", " +
                ArticlesTable.Cols.SYMBOL + " TEXT," +
                ArticlesTable.Cols.TITLE + " TEXT NOT NULL UNIQUE," +
                ArticlesTable.Cols.DATE + " TEXT," +
                ArticlesTable.Cols.URL + " TEXT, " +
                ArticlesTable.Cols.VISITED + " INTEGER" +
                ")");

        db.execSQL("create table " + SymbolHintTable.NAME + "(" +
                SymbolHintTable.Cols.ID + " INTEGER PRIMARY KEY," +
                SymbolHintTable.Cols.SYMBOL_NAME + " TEXT," +
                SymbolHintTable.Cols.FULL_NAME + " TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MenuTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ArticlesTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SymbolHintTable.NAME);
            onConfigure(db);
        }
    }

    public void importDatabase(String path) throws IOException {
        File newDb = new File(path);
        File oldDb = context.getDatabasePath(DATABASE_NAME);

        close();

        FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
        instance.getWritableDatabase();
    }

    public void exportDatabase(String directory) throws IOException {
        File destination = new File(directory + "/" + DATABASE_NAME);
        File database = context.getDatabasePath(DATABASE_NAME);

        close();
        FileUtils.copyFile(
                new FileInputStream(database), new FileOutputStream(destination));

        instance.getWritableDatabase();

    }
}