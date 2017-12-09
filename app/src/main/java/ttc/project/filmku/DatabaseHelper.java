package ttc.project.filmku;

/**
 * Created by AR-Laptop on 12/9/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AR-Laptop on 11/26/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PENGELUARAN_TABLE =
                "CREATE TABLE " + DatabaseContract.FavoritesEntry.TABLE_NAME +"(" +
                        DatabaseContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DatabaseContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                        DatabaseContract.FavoritesEntry.COLUMN_POSTER + " TEXT NOT NULL," +
                        DatabaseContract.FavoritesEntry.COLUMN_ID + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_PENGELUARAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}