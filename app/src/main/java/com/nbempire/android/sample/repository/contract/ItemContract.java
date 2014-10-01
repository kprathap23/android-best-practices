package com.nbempire.android.sample.repository.contract;

import android.provider.BaseColumns;

/**
 * Created by nbarrios on 01/10/14.
 */
public class ItemContract {

    private ItemContract() {
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String INTEGER_TYPE = " INTEGER";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                    ItemEntry._ID + " INTEGER PRIMARY KEY," +
                    ItemEntry.Column.ID + TEXT_TYPE + COMMA_SEP +
                    ItemEntry.Column.PRICE + INTEGER_TYPE + COMMA_SEP +
                    ItemEntry.Column.STOP_TIME + INTEGER_TYPE +
                    ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;

    /**
     * It represents the Item table.
     */
    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "item";

        public static class Column {
            public static final String ID = "id";
            public static final String PRICE = "price";
            public static final String STOP_TIME = "stopTime";
        }
    }
}
