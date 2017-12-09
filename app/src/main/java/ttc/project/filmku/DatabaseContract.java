package ttc.project.filmku;

import android.provider.BaseColumns;

/**
 * Created by AR-PC on 12/8/2017.
 */

public class DatabaseContract {
    public static final class FavoritesEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites_table";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POSTER = "poster";
    }
}
