package pl.pjask.stocknews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.pjask.stocknews.db.DBSchema.SymbolHintTable;

import static pl.pjask.stocknews.db.DBSchema.MenuTable;
import static pl.pjask.stocknews.db.DBSchema.NewsTable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "stocknews.db";
    private static DBHelper instance;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
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
                MenuTable.Cols.SYMBOL_NAME + " TEXT" +
                ")"
        );

        db.execSQL("create table " + NewsTable.NAME + "(" +
                NewsTable.Cols.ID + " INTEGER PRIMARY KEY," +
                NewsTable.Cols.MENU_ID + " INTEGER REFERENCES " + MenuTable.NAME + ", " +
                NewsTable.Cols.SYMBOL + " TEXT," +
                NewsTable.Cols.TITLE + " TEXT NOT NULL UNIQUE," +
                NewsTable.Cols.URL + " TEXT" +
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
            db.execSQL("DROP TABLE IF EXISTS " + NewsTable.NAME);
            onConfigure(db);
        }
    }
}