package com.first.kritikm.split;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Kritikm on 06-Oct-16.
 */
public class UserDataContract {

    //no instantiating this class broooo
    private UserDataContract(){}

    //variables used all over the world(contract class)
    private static final String DATABASE_NAME = "Split.db";
    private static final String TEXT = " TEXT";
    private static final String COMMA = ",";

    public static class EaterEntry extends SQLiteOpenHelper
    {

        private static final String TABLE_NAME = "eaters";
        private static final String EVENT_ID = "event_id";
        private static final String EATER_NAME = "eater";
        private static final String EATER_ATE = "eater_ate";
        private static final String EATER_PAID = "eater_paid";
        private static final String SQL_CREATE_EATERENTRY_TABLE  =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        EVENT_ID + " INTEGER, " +
                        EATER_NAME + TEXT + COMMA +
                        EATER_ATE + " REAL" + COMMA + EATER_PAID + " REAL" + COMMA +
                        " FOREIGN KEY(" + EVENT_ID + ")" +
                        " REFERENCES " + SplitEntry.TABLE_NAME + "(" +
                        SplitEntry._ID + ")" + COMMA +
                        " PRIMARY KEY(" + EVENT_ID + COMMA + EATER_NAME + "));";

        private static final String SQL_DELETE_EATERENTRY_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public EaterEntry(Context context)
        {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        }

        public Cursor getRows(int eventId)
        {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + EVENT_ID + "=" + eventId, null);
        }

        public boolean insert(int eventId, String eaterName, double eaterAte, double eaterPaid)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(EVENT_ID, eventId);
            contentValues.put(EATER_NAME, eaterName);
            contentValues.put(EATER_ATE, eaterAte);
            contentValues.put(EATER_PAID, eaterPaid);

            if(db.insert(TABLE_NAME, null, contentValues) == -1)
                return false;
            return true;
        }
    }

    public static class SplitEntry extends SQLiteOpenHelper implements BaseColumns
    {

        //_ID here is the Event ID. EVENT is the name of the event

        private static final String TABLE_NAME = "splits";
        private static final String EVENT = "event";
        private static final String USER_ID = "user_id";
        private static final String EVENT_DATE = "event_date";
        private static final String LOCATION = "location";
        private static final String CURRENCY = "currency";
        private static final String TOTAL_AMOUNT = "total_amount";

        private static final String SQL_CREATE_EVENT_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        USER_ID + " INTEGER, " +
                        EVENT + TEXT + COMMA +
                        EVENT_DATE + " TEXT" + COMMA +
                        LOCATION + TEXT + COMMA +
                        CURRENCY + TEXT + COMMA +
                        TOTAL_AMOUNT + " REAL" + COMMA +
                        "FOREIGN KEY(" + USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME +
                        "(" + UserEntry._ID + ")" +
                        ");";
        private static final String SQL_DELETE_EVENT_TABLE =
                "DROP TABLE IF EXISTS " + SplitEntry.TABLE_NAME;

        public SplitEntry(Context context)
        {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public int getEventId(String eventName, String eventDate, String eventLocation)
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + _ID + " FROM " + TABLE_NAME + " WHERE EVENT ='" + eventName +
                                        "' AND EVENT_DATE='" + eventDate + "' AND LOCATION='" + eventLocation + "'", null);
            if(cursor.moveToFirst())
                return cursor.getInt(0);
            return -1;
        }

        public Cursor getRows(int userId)
        {
            SQLiteDatabase db = getReadableDatabase();

            return db.rawQuery("select * from " + TABLE_NAME + " where " + USER_ID + "=" + userId, null);
        }

        public boolean insert(Integer userId, String eventName, String eventDate, String eventLocation, String currency, Double totalAmount)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(USER_ID, userId);
            contentValues.put(EVENT, eventName);
            contentValues.put(EVENT_DATE, eventDate);
            contentValues.put(LOCATION, eventLocation);
            contentValues.put(CURRENCY, currency);
            contentValues.put(TOTAL_AMOUNT, totalAmount);
            if(db.insert(TABLE_NAME, null, contentValues)== -1)
                return false;
            return true;
        }
    }

    public static class UserEntry extends SQLiteOpenHelper implements BaseColumns
    {
        private static final String TABLE_NAME = "users";
        private static final String NAME = "name";
        private static final String USERNAME = "username";
        private static final String PASSWORD = "password";
        private static final String EMAIL = "email";
        private static final String SQL_CREATE_USER_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        _ID + " INTEGER PRIMARY KEY," +
                        NAME + TEXT + COMMA +
                        USERNAME + TEXT + COMMA +
                        EMAIL + TEXT + COMMA +
                        PASSWORD + TEXT + ");";
        private static final String SQL_DELETE_USER_TABLE =
                "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


        public UserEntry(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }


        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(SQL_CREATE_USER_TABLE);
            db.execSQL(SplitEntry.SQL_CREATE_EVENT_TABLE);
            db.execSQL(EaterEntry.SQL_CREATE_EATERENTRY_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_USER_TABLE);
            onCreate(db);
        }

        public boolean insert(String name, String username, String email, String password)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(NAME, name);
            contentValues.put(USERNAME, username);
            contentValues.put(EMAIL, email);
            contentValues.put(PASSWORD, password);

            if(db.insert(TABLE_NAME, null, contentValues) == -1)
                return false;
            return true;
        }

        public Cursor getRowUsername(String username)
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor user = db.rawQuery("select * from " + TABLE_NAME + " where username='" + username + "'", null);
            return user;
        }

        public Cursor getRowEmail(String email)
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor user = db.rawQuery("select * from " + TABLE_NAME + " where email='" + email + "'", null);
            return user;
        }

        public int getUserId(String username)
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("select " + _ID + " from " + TABLE_NAME + " where " + USERNAME + "='" + username + "'", null);
            if(cursor.moveToFirst())
                return cursor.getInt(0);
            return -1;
        }

        public Cursor getRowName(String username)
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor user = db.rawQuery("select * from " + TABLE_NAME + " where username='" + username + "'", null);
            return user;
        }
    }

}
