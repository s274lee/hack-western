package hackwestern.hackwestern;

import android.provider.BaseColumns;

/**
 * Created by Rebecca on 3/28/2015.
 */
public final class SQLContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public SQLContract() {}

    /* Inner class that defines the table contents */
    public abstract class MessageTable implements BaseColumns {
        public static final String TABLE_NAME = "user_messages";
        public static final String COLUMN_RECIPIENT = "recipient";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_PHONE_NUMBER = "recipient_phone";
        public static final String COLUMN_TIME_CREATED = "time_created";
        public static final String COLUMN_TIME_SENT = "time_sent";
        public static final String COLUMN_SENT_FLAG = "sent_flag";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";
    }
}
