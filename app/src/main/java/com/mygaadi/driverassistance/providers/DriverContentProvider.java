package com.mygaadi.driverassistance.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.mygaadi.driverassistance.DBPackage.DriverDatabaseHelper;
import com.mygaadi.driverassistance.DBPackage.Tables;


/**
 * Created by Aditya on 10/27/2015.
 */
public class DriverContentProvider extends ContentProvider {

    private SQLiteDatabase db;
    private Context mContext;
    public static final String PROVIDER_NAME = "com.driverassitance.provider";
    private static final String TAG = DriverContentProvider.class.getCanonicalName();

    public static final Uri ALL_LOCATION_URI =
            Uri.parse("content://" + PROVIDER_NAME + "/allLocations");

    private static final String DbTables[] = {"allLocations"};

    static final int ALL_LOCATION = 0;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, Tables.ALL_LOCATION, ALL_LOCATION);
//        uriMatcher.addURI(PROVIDER_NAME, "allmake/#", SAMPLE1_ID);
//        uriMatcher.addURI(PROVIDER_NAME, Tables.ALL_MAKE_MODEL, ALL_MAKE_MODEL);
//        uriMatcher.addURI(PROVIDER_NAME, "allMake/#", SAMPLE2_ID);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        DriverDatabaseHelper dbHelper = DriverDatabaseHelper.getInstance(mContext);
        db = dbHelper.getWritableDatabase();
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        return (db == null) ? false : true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArg, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int matcherId = uriMatcher.match(uri);
        String groupBy = null;

        switch (matcherId) {
            case ALL_LOCATION:
                qb.setTables(Tables.ALL_LOCATION);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArg, groupBy, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_LOCATION:
                return Tables.ALL_LOCATION;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        switch (uriMatcher.match(uri)) {
            case ALL_LOCATION:
                db.beginTransaction();
                try {
//                    long _ID1 = db.insertWithOnConflict(Tables.ALL_LOCATION, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    long _ID1 = db.insert(Tables.ALL_LOCATION, null, values);
                    if (_ID1 > 0) {
                        _uri = ContentUris.withAppendedId(ALL_LOCATION_URI, _ID1);
                        getContext().getContentResolver().notifyChange(_uri, null);
                        db.setTransactionSuccessful();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
                break;
            default:
                throw new SQLException("Failed to insert row into " + uri);
        }
        return _uri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArg) {

        int count = 0;

        switch (uriMatcher.match(uri)) {
            case ALL_LOCATION:
                count = db.delete(DbTables[ALL_LOCATION], selection, selectionArg);
                break;
            default:
                throw new SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
