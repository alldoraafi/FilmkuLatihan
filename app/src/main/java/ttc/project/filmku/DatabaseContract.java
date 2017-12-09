package ttc.project.filmku;

import android.provider.BaseColumns;

/**
 * Created by AR-PC on 12/8/2017.
 */

public class DatabaseContract {
    public static final class FilmEntry implements BaseColumns{
        public static final String TABLE_NAME = "film_table";
        public static final String COLUMN_TITLE = "title";
    }
}
