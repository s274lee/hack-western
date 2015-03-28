package hackwestern.hackwestern;

/**
 * Created by Rebecca on 3/28/2015.
 */
public class SQLScripts {
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
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

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SQLContract.MessageTable.TABLE_NAME;
}