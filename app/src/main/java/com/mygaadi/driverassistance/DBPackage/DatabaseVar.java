package com.mygaadi.driverassistance.DBPackage;

import android.database.DatabaseErrorHandler;
import android.provider.BaseColumns;

/**
 * Created by Aditya on 1/28/2016.
 * <p>
 * This class represents a contract for a row_counter table containing row
 * counters for projects. The project must exist before creating row counters
 * since the counter have a foreign key to the project.
 */
public final class DatabaseVar {
    /**
     * Contains the SQL query to use to create the table containing the row counters.
     */
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "
            + Tables.ALL_LOCATION + " ("
            + DriverLocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DriverLocationEntry.DRIVER_ID + " INTEGER,"
            + DriverLocationEntry.LATITUDE + " VARCHAR,"
            + DriverLocationEntry.LONGITUDE + " VARCHAR,"
            + DriverLocationEntry.CREATED_AT + " VARCHAR)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Tables.ALL_LOCATION;

    /**
     * This class represents the rows for an entry in the row_counter table. The
     * primary key is the _id column from the BaseColumn class.
     */
    public static abstract class DriverLocationEntry implements BaseColumns {
        public static final String DRIVER_ID = "driverId";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String CREATED_AT = "createdAt";
    }
}