package adk.today;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Adu on 6/24/2016.
 */

public class Storage extends SQLiteOpenHelper {

    /*
    * This class uses standard SQL Queries to access the database "CLASSES" which stores the data.
    * It has the following schema:
    *   ( Day  TEXT,     -- Period's respective day.
    *     Sub  TEXT,     -- Subject
    *     Prof TEXT,     -- Taught By
    *     Time TEXT,     -- hh:mm - hh:mm
    *     Room TEXT,     -- In Room
    *     Ty   TEXT,     -- Theory/Lab
    *     ID   NUMBER )  -- Just a count.
    *
    */

    private static final String DATABASE_NAME = "CLASSES.db";
    private static final String TABLE_NAME = "CLASSES";
    private static final String COL_0 = "ID";
    private static final String COL_1 = "DAY";
    private static final String COL_2 = "SUB";
    private static final String COL_3 = "PROF";
    private static final String COL_4 = "TIME";
    private static final String COL_5 = "ROOM";
    private static final String COL_6 = "TY";
    private static int id = 1;
    Context context;

    public Storage(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d("ADU", "CREATING TABLE");
            db.execSQL("CREATE TABLE " + TABLE_NAME + "(DAY TEXT, SUB TEXT, PROF TEXT, TIME TEXT , ROOM TEXT, TY TEXT ,ID NUMBER);");
        } catch (Exception e) {
            Toast.makeText(context, "You've just found a bug! ( Code - S_O.C )", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME + ")");
            onCreate(db);
        } catch (Exception e) {
            Toast.makeText(context, "You've just found a bug! ( Code - S_O.U )", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertData(String day, String sub, String prof, String time, String room, String type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, day);
        contentValues.put(COL_2, sub);
        contentValues.put(COL_3, prof);
        contentValues.put(COL_4, time);
        contentValues.put(COL_5, room);
        contentValues.put(COL_6, type);
        contentValues.put(COL_0, id++);
        try {
            Log.d("ADK--", "Inserting" + day + " "
                    + sub + " "
                    + prof + " "
                    + time + " "
                    + room + " "
                    + type);
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(" +
                    "'" + day + "'," +
                    "'" + sub + "'," +
                    "'" + prof + "'," +
                    "'" + time + "'," +
                    "'" + room + "'," +
                    "'" + type + "'," +
                    "'" + id + "');");

            Log.d("ADK--", getData(day).toString());
        } catch (Exception e) {
            Toast.makeText(context, "You've just found a bug! ( Code - S_I.D ) \n" +
                    "P.S : No injections allowed here! :D", Toast.LENGTH_SHORT).show();
            Log.d("ADK--", e.getMessage());
        }

    }

    public List<Period> getData(String day) {
        /***
         * Returns the list of periods on the day specified by : day argument
         */
        List<Period> periods = new ArrayList<Period>();

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM CLASSES WHERE DAY = '" + day + "';", null);
            while (cursor.moveToNext()) {
                periods.add(new Period(cursor.getString(1), cursor.getString(3), cursor.getString(5), cursor.getString(2), cursor.getString(4)));
                String name = cursor.getString(1);
                String prof = cursor.getString(2);
                String time = cursor.getString(3);
                String room = cursor.getString(6);
            }

            // Sort the data before sending it back.
            // This makes sure that the periods are in proper order.
            Collections.sort(periods);

            cursor.close();
        } catch (Exception e) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(DAY TEXT, SUB TEXT, PROF TEXT, TIME TEXT , ROOM TEXT, TY TEXT ,ID NUMBER);");
                Log.d("ADK--", "Creating DB");

            } catch (Exception e1) {}
        }

        return periods;
    }

    public void deleteData(String day, String sub, String prof, String time, String room) {

        String[] args = {day, sub, prof, time, room};
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " +
                    "DAY = '" + day + "' AND " +
                    "SUB = '" + sub + "' AND " +
                    "PROF = '" + prof + "' AND " +
                    "TIME = '" + time + "' AND " +
                    "ROOM = '" + room + "' ;");
        } catch (Exception e) {
            Toast.makeText(context, "You've just found a bug! ( Code - S_D.D ) ", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(String day, String sub, String prof, String time, String room,
                           String nsub, String nprof, String ntime, String nroom, String nType) {

        String[] args = {day, sub, prof, time, room};
        SQLiteDatabase db = this.getWritableDatabase();


        try {

            db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                    "SUB = '" + nsub + "'," +
                    "PROF = '" + nprof + "'," +
                    "TIME = '" + ntime + "'," +
                    "ROOM = '" + nroom + "'," +
                    "TY = '" + nType + "' WHERE " +
                    "DAY = '" + day + "' AND " +
                    "SUB = '" + sub + "' AND " +
                    "PROF = '" + prof + "' AND " +
                    "TIME = '" + time + "' AND " +
                    "ROOM = '" + room + "' ;");


        } catch (Exception e) {
            Toast.makeText(context, "You've just found a bug! ( Code - S_U.D ) \n" +
                    "P.S : No injections allowed here! :D", Toast.LENGTH_SHORT).show();
        }
    }

    public void reset() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NAME + " ;");
        } catch (Exception e) {
        }
    }

    // This is default data that I put in the database , as it's my regular timetable.
    public void setUpInit() {

        reset();
        insertData("Monday", "WT Lab", "Shruthi Saxena", "9:00 - 11:40", "320", "Lab");
        insertData("Monday", "ME", "Ram Mohan", "11:40 - 12:30", "119", "Theory");
        insertData("Monday", "LP", "Sireesha", "2:00 - 2:50", "119", "Theory");
        insertData("Monday", "DAA", "Tarak", "2:50 - 3:40", "119", "Theory");
        insertData("Monday", "AI", "K Srinivas Rao", "3:40 - 4:30", "119", "Theory");

        insertData("Tuesday", "LP Lab", "Shruthi Saxena", "9:00 - 11:40", "203", "Lab");
        insertData("Tuesday", "DAA", "Tarak", "11:40 - 12:30", "119", "Theory");
        insertData("Tuesday", "ME", "Ram Mohan", "2:00 - 2:50", "119", "Theory");
        insertData("Tuesday", "LP", "Sireesha", "2:50 - 3:40", "119", "Theory");
        insertData("Tuesday", "WT", "T Srinivas", "3:40 - 4:30", "119", "Theory");

        insertData("Wednesday", "LP", "Sireesha", "9:00 - 10:00", "119", "Theory");
        insertData("Wednesday", "WT", "T Srinivas", "10:00 - 10:50", "119", "Theory");
        insertData("Wednesday", "ME", "Ram Mohan", "10:50 - 11:40", "119", "Theory");
        insertData("Wednesday", "AI", "K Srinivas Rao", "11:40 - 12:30", "119", "Theory");
        insertData("Wednesday", "CG", "K Mahesh", "2:00 - 3:40", "119", "Theory");
        insertData("Wednesday", "DAA", "Tarak", "3:40 - 4:30", "119", "Theory");

        insertData("Thursday", "DAA", "Tarak", "9:00 - 10:00", "119", "Theory");
        insertData("Thursday", "ME", "Ram Mohan", "10:00 - 10:50", "119", "Theory");
        insertData("Thursday", "AI", "K Srinivas Rao", "10:50 - 11:40", "119", "Theory");
        insertData("Thursday", "LP", "Sireesha", "11:40 - 12:30", "119", "Theory");
        insertData("Thursday", "WT", "T Srinivas", "2:00 - 3:40", "119", "Theory");
        insertData("Thursday", "CG", "K Mahesh", "3:40 - 4:30", "119", "Theory");

        insertData("Friday", "CG", "K Mahesh", "9:00 - 10:00", "119", "Theory");
        insertData("Friday", "AI", "K Srinivas Rao", "10:00 - 10:50", "119", "Theory");
        insertData("Friday", "WT", "T Srinivas", "10:50 - 11:40", "119", "Theory");
        insertData("Friday", "DAA", "Tarak", "11:40 - 12:30", "119", "Theory");

        insertData("Saturday", "ME", "Ram Mohan", "9:00 - 10:00", "119", "Theory");
        insertData("Saturday", "AI", "K Srinivas Rao", "10:00 - 10:50", "119", "Theory");
        insertData("Saturday", "LP", "Sireesha", "10:50 - 11:40", "119", "Theory");
        insertData("Saturday", "CG", "K Mahesh", "11:40 - 12:30", "119", "Theory");

    }

}

