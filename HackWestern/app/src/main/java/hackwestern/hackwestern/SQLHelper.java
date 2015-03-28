package hackwestern.hackwestern;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Rebecca on 3/28/2015.
 */
public class SQLHelper {
    private class Scripts {
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + SQLContract.MessageTable.TABLE_NAME + " (" +
                        SQLContract.MessageTable._ID + " INTEGER PRIMARY KEY," +
                        SQLContract.MessageTable.COLUMN_RECIPIENT + " TEXT" + COMMA_SEP +
                        SQLContract.MessageTable.COLUMN_PHONE_NUMBER + " INTEGER" + COMMA_SEP +
                        SQLContract.MessageTable.COLUMN_TIME_CREATED + " TEXT" + COMMA_SEP +
                        SQLContract.MessageTable.COLUMN_TIME_SENT + " TEXT" + COMMA_SEP +
                        SQLContract.MessageTable.COLUMN_SENT_FLAG + " INTEGER" + COMMA_SEP +
                        SQLContract.MessageTable.COLUMN_LONGITUDE + " REAL" + COMMA_SEP +
                        SQLContract.MessageTable.COLUMN_LATITUDE + " REAL" + COMMA_SEP +
                " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + SQLContract.MessageTable.TABLE_NAME;
    }

    public class DbExecutor extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "HackWestern.db";

        public DbExecutor(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Scripts.SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(Scripts.SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}