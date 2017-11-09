package pl.pjask.stocknews.db;

public class DBSchema {
    public static final class MenuTable {
        public static final String NAME = "menu";

        public static final class Cols {
            public static final String ID = "id";
            public static final String SYMBOL_NAME = "symbol";
            public static final String FETCH_NEWS = "news";
            public static final String FETCH_ESPI = "espi";
        }
    }

    public static final class NewsTable {
        public static final String NAME = "news";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String MENU_ID = "menu_id";
            public static final String SYMBOL = "symbol";
            public static final String TITLE = "title";
            public static final String URL = "url";
            public static final String VISITED = "visited";
        }
    }


    public static final class SymbolHintTable {
        public static final String NAME = "hints";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String SYMBOL_NAME = "symbol";
            public static final String FULL_NAME = "name";
        }
    }
}
