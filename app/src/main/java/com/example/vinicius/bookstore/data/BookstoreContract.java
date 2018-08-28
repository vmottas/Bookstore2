package com.example.vinicius.bookstore.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;


public final class BookstoreContract {

    public static final String CONTENT_AUTHORITY = "com.example.vinicius.bookstore";
    public static final String PATH_BOOKS = "bookstore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public final static String COLUMN_KEY = _ID;

    private BookstoreContract(){}

    public static final class BookstoreEntry implements BaseColumns {

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        public final static String TABLE_NAME = "bookstore";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_BOOK_NAME = "name";
        public final static String COLUMN_BOOK_PRICE = "price";
        public final static String COLUMN_BOOK_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier";
        public final static String COLUMN_SUPPLIER_TEL = "supplier_tel";
    }
}
