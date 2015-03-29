package hackwestern.hackwestern;

/**
 * Created by Rebecca on 3/28/2015.
 */
public class SQLScripts {
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_MESSAGE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLContract.MessageTable.TABLE_NAME + " (" +
                    SQLContract.MessageTable._ID + " INTEGER PRIMARY KEY," +
                    SQLContract.MessageTable.COLUMN_RECIPIENT + " TEXT" + COMMA_SEP +
                    SQLContract.MessageTable.COLUMN_PHONE_NUMBER + " INTEGER" + COMMA_SEP +
                    SQLContract.MessageTable.COLUMN_MESSAGE + " TEXT " + COMMA_SEP +
                    SQLContract.MessageTable.COLUMN_TIME_CREATED + " TEXT" + COMMA_SEP +
                    SQLContract.MessageTable.COLUMN_TIME_SENT + " TEXT" + COMMA_SEP +
                    SQLContract.MessageTable.COLUMN_SENT_FLAG + " INTEGER" + COMMA_SEP +
                    SQLContract.MessageTable.COLUMN_LONGITUDE + " REAL" + COMMA_SEP +
                    SQLContract.MessageTable.COLUMN_LATITUDE + " REAL" +
            " )";

    public static final String SQL_DELETE_MESSAGE_TABLE =
            "DROP TABLE IF EXISTS " + SQLContract.MessageTable.TABLE_NAME;

     public static final String SQL_SELECT_MESSAGE_TABLE = "SELECT latitude FROM "+ SQLContract.MessageTable.TABLE_NAME;

     public static final String SQL_SELECT_UNSENT_MESSAGES = "SELECT "+SQLContract.MessageTable._ID+ COMMA_SEP +
             SQLContract.MessageTable.COLUMN_RECIPIENT + COMMA_SEP + SQLContract.MessageTable.COLUMN_PHONE_NUMBER + COMMA_SEP + SQLContract.MessageTable.COLUMN_MESSAGE + COMMA_SEP +
             SQLContract.MessageTable.COLUMN_LONGITUDE + COMMA_SEP + SQLContract.MessageTable.COLUMN_LATITUDE + " FROM "+SQLContract.MessageTable.TABLE_NAME +
             " WHERE "+SQLContract.MessageTable.COLUMN_SENT_FLAG + "=0";
}