package ie.gavin.ulstudenttimetable.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyDBHandler extends SQLiteOpenHelper{
    public static final int DELETE = 0;
    public static final int ADD = 1;

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "ULtimetable.db";
    public static final String TABLE_MODULE = "module";
    public static final String TABLE_WEEK = "date";
    public static final String TABLE_STUDENT_TIMETABLE = "studentTimetable";
    public static final String TABLE_CLASS_WEEKS = "classWeeks";
    public static final String TABLE_UID = "uid";
    public static final String TABLE_MODULE_NAMES = "moduleNames";
    public static final String TABLE_USERS = "users";
    //modules table
    public static final String COLUMN_ID_TABLE_POINTER = "idTablePointer";
    public static final String COLUMN_MODULE_CODE = "moduleCode";
    public static final String COLUMN_START_TIME = "startTime";
    public static final String COLUMN_END_TIME = "endTime";
    public static final String COLUMN_ROOM = "room";
    public static final String COLUMN_LECTURER = "lecturer";
    public static final String COLUMN_DAY = "day";
    //weeks table
    public static final String COLUMN_WEEK = "week";
    public static final String COLUMN_WEEK_LABEL = "weekLabel";
    public static final String COLUMN_WEEK_START = "startWeek";
   //student table
    public static final String COLUMN_STUDENT_ID = "studentID";
    public static final String COLUMN_MODULE_POINTER = "modulePointer";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_GROUP_NAME = "groupName";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_COLOR = "color";

    //class weeks
    public static final String COLUMN_START_WEEK = "startWeek";
    public static final String COLUMN_END_WEEK = "endWeek";

    //module name
    public static final String COLUMN_MODULE_NAME = "moduleName";

    //users table
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_USER_NAME = "userName";

    //uid
    public static final String COLUMN_ID = "id";

//    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
//    }

    private static MyDBHandler mInstance = null;
    private Context context;

    public static MyDBHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyDBHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    private MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_MODULE + "(" +
                COLUMN_ID_TABLE_POINTER + " INTEGER PRIMARY KEY, " +
                COLUMN_MODULE_CODE + " VARCHAR(10), " +
                COLUMN_START_TIME + " TIME, " +
                COLUMN_END_TIME + " TIME, " +
                COLUMN_ROOM + " VARCHAR(10), " +
                COLUMN_LECTURER + " VARCHAR(10), " +
                COLUMN_DAY + " INT, " +
                COLUMN_GROUP_NAME + " VARCHAR(5), " +
                COLUMN_TYPE + " VARCHAR(45) " +
                ");";

        String query2 = "CREATE TABLE " + TABLE_WEEK + "(" +
                COLUMN_WEEK + " INTEGER PRIMARY KEY ," +
                COLUMN_WEEK_LABEL + " VARCHAR(15), " +
                COLUMN_WEEK_START + " DATE " +
                ");";

        String query3 = "CREATE TABLE " + TABLE_STUDENT_TIMETABLE + "(" +
                COLUMN_ID_TABLE_POINTER + " INTEGER PRIMARY KEY, " +
                COLUMN_MODULE_POINTER + " INTEGER, " +
                COLUMN_MODULE_CODE + " VARCHAR(10), " +
                COLUMN_START_TIME + " TIME, " +
                COLUMN_DAY + " INTEGER, " +
                COLUMN_END_TIME + " TIME, " +
                COLUMN_STUDENT_ID + " INTEGER, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_GROUP_NAME + " VARCHAR(5), " +
                COLUMN_TYPE + " VARCHAR(45), " +
                COLUMN_TITLE + " VARCHAR(45), " +
                COLUMN_LECTURER + " VARCHAR(45), " +
                COLUMN_ROOM + " VARCHAR(10), " +
                COLUMN_COLOR + " INTEGER " +
                ");";

        String query4 = "CREATE TABLE " + TABLE_CLASS_WEEKS + "(" +
                COLUMN_START_WEEK + " INTEGER, " +
                COLUMN_END_WEEK + " INTEGER, " +
                COLUMN_ID_TABLE_POINTER + " INTEGER, " +
                "PRIMARY KEY(" + COLUMN_START_WEEK + ", " + COLUMN_END_WEEK + ", " + COLUMN_ID_TABLE_POINTER +
                ")" +
                ");";
        String query5 = "CREATE TABLE " + TABLE_UID + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                ");";

        String query6 = "CREATE TABLE " + TABLE_MODULE_NAMES + "(" +
                COLUMN_MODULE_CODE + " VARCHAR(10) PRIMARY KEY, " +
                COLUMN_MODULE_NAME + " VARCHAR(45) " +
                ");";

        String query7 = "CREATE TRIGGER module_update AFTER UPDATE ON "
                        + TABLE_MODULE
                        + " FOR EACH ROW BEGIN UPDATE "
                        + TABLE_STUDENT_TIMETABLE
                        + " SET " + COLUMN_MODULE_POINTER + " = "
                        + " new." +COLUMN_ID_TABLE_POINTER
                        + " WHERE " + COLUMN_MODULE_POINTER + " = "
                        + "old." + COLUMN_ID_TABLE_POINTER + "; END;";

        String query8 = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY ," +
                COLUMN_USER_NAME + " VARCHAR(30) " +
                ");";

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query6);
        db.execSQL(query7);
        db.execSQL(query8);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEEK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_TIMETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS_WEEKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULE_NAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);

    }


    //update a module
    public boolean updateModuleTable(Module module){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_MODULE_CODE + " = '" + module.get_ModuleCode() + "'"
                + " AND " + COLUMN_START_TIME + " = '" + module.get_startTime()+ "'"
                + " AND " + COLUMN_END_TIME + " = '" + module.get_endTime()+ "'"
                + " AND " + COLUMN_DAY + " = '" + module.get_day()
                + "';";
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROOM, module.get_room());
        values.put(COLUMN_LECTURER, module.get_lecturer());
        values.put(COLUMN_GROUP_NAME, module.get_groupName());
        values.put(COLUMN_TYPE, module.get_type());
        int id = db.update(TABLE_MODULE, values, whereClause, null);
        //Log.d("UPDATED ROWS", ": " + id);
        //long id = db.insert(TABLE_UID, COLUMN_ID, null); //get return value and pass to idTablePointer

        if (id == 0){
            whereClause = COLUMN_MODULE_CODE + " = '" + module.get_ModuleCode() + "'"
                    + " AND " + COLUMN_ROOM + " = '" + module.get_room()+ "'"
                    + " AND " + COLUMN_LECTURER + " = '" + module.get_lecturer()+ "'"
                    + " AND " + COLUMN_GROUP_NAME + " = '" + module.get_groupName() + "'"
                    + " AND " + COLUMN_TYPE + " = '" + module.get_type()
                    + "';";
            ContentValues values2 = new ContentValues();
            values2.put(COLUMN_START_TIME, module.get_startTime());
            values2.put(COLUMN_END_TIME, module.get_endTime());
            values2.put(COLUMN_DAY, module.get_day());
            id = db.update(TABLE_MODULE, values2, whereClause, null);
        }

        db.close();
        if (id == 1)
            return true;
        else
            return false;

    }



    //Add a new row to the Module database
    //pass in week and handle weeks table entry at the same time
    public void addToModuleTable(Module module, String weeks){
        weeks = weeks.substring(weeks.indexOf(":") + 1);
        SQLiteDatabase db = getWritableDatabase();

        long id = db.insert(TABLE_UID, COLUMN_ID, null); //get return value and pass to idTablePointer
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_TABLE_POINTER, id);//id taken from UID table
        values.put(COLUMN_MODULE_CODE, module.get_ModuleCode());
        values.put(COLUMN_START_TIME, module.get_startTime());
        values.put(COLUMN_END_TIME, module.get_endTime());
        values.put(COLUMN_ROOM, module.get_room());
        values.put(COLUMN_LECTURER, module.get_lecturer());
        values.put(COLUMN_DAY, String.valueOf(module.get_day()));
        values.put(COLUMN_GROUP_NAME, module.get_groupName());
        values.put(COLUMN_TYPE, module.get_type());

        db.insert(TABLE_MODULE, null, values);
        db.close();

        if (weeks.contains(",")){
            String[] splitArray = weeks.split(",");
            for(int i = 0; i <splitArray.length; i++) {
                String[] secondSplitArray;
                if (splitArray[i].contains("-")) {
                    secondSplitArray = splitArray[i].split("-");
                } else {
                    secondSplitArray = new String[] {splitArray[i],splitArray[i]};
                }
                addToClassWeekTable(secondSplitArray[0], secondSplitArray[1], id);
            }

        }

        else {
            String[] splitArray = weeks.split("-");
            addToClassWeekTable(splitArray[0], splitArray[1], id);
        }


    }


    // addToWeekDetails
    public void addToWeekDetails(int week, String weekLabel, Date weekCommencing){
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
        String weekStart = outFormat.format( weekCommencing );
        Log.v("Datetestin", weekStart);
        SQLiteDatabase db = getWritableDatabase();

        String sql="REPLACE INTO " + TABLE_WEEK + " (" + COLUMN_WEEK + ", " + COLUMN_WEEK_LABEL + ", " + COLUMN_WEEK_START + ") " +
                "VALUES (" + week + ", '" + weekLabel + "', '" + weekStart + "')";

        db.execSQL(sql);
        db.close();
    }

    //get all studentTimetable users
    public ArrayList<Week> getWeekDetails(){
        ArrayList<Week> result = new ArrayList<>();

        String query = "SELECT * "
                + " FROM " + TABLE_WEEK
                + ";";

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            do
            {
                Log.v("Datetestout", c.getString(c.getColumnIndex(COLUMN_WEEK_START)));
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date weekStart = inFormat.parse(c.getString(c.getColumnIndex(COLUMN_WEEK_START)));

                result.add(
                        new Week(
                                c.getInt(c.getColumnIndex(COLUMN_WEEK)),
                                c.getString(c.getColumnIndex(COLUMN_WEEK_LABEL)),
                                weekStart
                        )
                );

            } while (c.moveToNext());

        } catch (SQLiteException e) {
            Log.d("SQL Error", e.getMessage());

        } catch (CursorIndexOutOfBoundsException ce) {
            Log.d("ID not found", ce.getMessage());

        } catch (ParseException e) {
            Log.d("week date parse error", e.getMessage());
            e.printStackTrace();
        } finally {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }



    // addToClassWeeksTable
    public void addToClassWeekTable(String startWeek, String endWeek, long idPointer){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_START_WEEK, startWeek);
        values.put(COLUMN_END_WEEK, endWeek);
        values.put(COLUMN_ID_TABLE_POINTER, String.valueOf(idPointer));

        db.insert(TABLE_CLASS_WEEKS, null, values);
        db.close();
       // Log.v("Class Week Data added", " " + startWeek + " - " + endWeek + ": " + String.valueOf(idPointer));
    }

    //// addToModuleNamesTable
    public void addToModuleNamesTable(String moduleCode, String moduleName){

        if (getModuleName(moduleCode) == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_MODULE_CODE, moduleCode);
            values.put(COLUMN_MODULE_NAME, moduleName);

            db.insert(TABLE_MODULE_NAMES, null, values);

            db.close();
        }
    }

    public String getModuleName(String moduleCode){
        String result = null;
        String query = "SELECT * FROM " + TABLE_MODULE_NAMES + " WHERE " +
                COLUMN_MODULE_CODE + " = '" + moduleCode + "';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            result = c.getString(c.getColumnIndex("moduleName"));
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage());

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
        }

        return result;
    }




    //add row to studentTimetable table
    //pass in week and handle weeks table entry at the same time?
    public void addToStudentTimetable(StudentTimetable entry, String weeks) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_UID, COLUMN_ID, null); //get return value and pass to idTablePointer
        ContentValues values = new ContentValues();
        if (!weeks.isEmpty()) weeks = weeks.substring(weeks.indexOf(":") + 1);
        values.put(COLUMN_ID_TABLE_POINTER, id);
        values.put(COLUMN_MODULE_POINTER, entry.get_modulePointer());
        values.put(COLUMN_MODULE_CODE, entry.get_moduleCode());
        values.put(COLUMN_START_TIME, entry.get_start_time());
        values.put(COLUMN_DAY, String.valueOf(entry.get_day()));
        values.put(COLUMN_END_TIME, entry.get_endTime());
        values.put(COLUMN_STUDENT_ID, entry.get_studentID());
        values.put(COLUMN_NOTES, entry.get_notes());
        values.put(COLUMN_GROUP_NAME, entry.get_groupName());
        values.put(COLUMN_TYPE, entry.get_type());
        values.put(COLUMN_TITLE, entry.get_title());
        values.put(COLUMN_LECTURER, entry.get_lecturer());
        values.put(COLUMN_ROOM, entry.get_room());
        values.put(COLUMN_COLOR, entry.get_color());

        db.insert(TABLE_STUDENT_TIMETABLE, null, values);
        db.close();
        if (!weeks.isEmpty()){
            if (weeks.contains(",")) {
                String[] splitArray = weeks.split(",");
                for (int i = 0; i < splitArray.length; i++) {
                    String[] secondSplitArray = splitArray[i].split("-");
                    addToClassWeekTable(secondSplitArray[0], secondSplitArray[1], id);
                }

            } else {
                String[] splitArray = weeks.split("-");
                addToClassWeekTable(splitArray[0], splitArray[1], id);
                //Log.v("Testing WEEKS " , "s: " + splitArray[0] + "e: " + splitArray[1]);
            }
            }
    }

    //add or remove a module on studentTable
    public void addOrRemoveModuleFromTimetable(ArrayList<Module> module, int studentID, int flag){
        int UID = 0;
        int modulePointer = 0;
        int color = Color.parseColor("#FF0000");
        StudentTimetable tempStudent;
        if (flag == ADD) {
            for (int i = 0; i < module.size(); i++) {
                UID = module.get(i).get_idTablePointer();
                tempStudent = new StudentTimetable(0, null, UID, null, 0, null, studentID, null, null, null, null, null, null, color);
                addToStudentTimetable(tempStudent, "");
            }
        }
        else {
            for (int i = 0; i < module.size(); i++) {
                modulePointer = module.get(i).get_idTablePointer();
                deleteModuleFromStudentTimetable(modulePointer, studentID);
            }
        }
    }










    //return a module row from id
    public Module getModuleFromID(int id){
        Module result = null;
        String query = "SELECT * FROM " + TABLE_MODULE + " WHERE " +
                COLUMN_ID_TABLE_POINTER + " = " + id + ";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            result = new Module(
                    Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                    c.getString(c.getColumnIndex("moduleCode")),
                    c.getString(c.getColumnIndex("startTime")),
                    c.getString(c.getColumnIndex("endTime")),
                    c.getString(c.getColumnIndex("room")),
                    c.getString(c.getColumnIndex("lecturer")),
                    Integer.parseInt(c.getString(c.getColumnIndex("day"))),
                    c.getString(c.getColumnIndex("groupName")),
                    c.getString(c.getColumnIndex("type"))
            );
        }
            catch (SQLiteException e)
            {
                Log.d("SQL Error", e.getMessage());
                return null;
            }
            catch (CursorIndexOutOfBoundsException ce)
            {
                Log.d("ID not found", ce.getMessage());

            }
            finally
            {
                //release all resources
                if (c != null) c.close();
                db.close();
            }
        return result;
    }

    //return a studentTimetable row from id
    public StudentTimetable getStudentTimetableFromID(int id){
        StudentTimetable result = null;
        String query = "SELECT  * FROM " + TABLE_STUDENT_TIMETABLE
                + " LEFT JOIN " + TABLE_CLASS_WEEKS
                + " USING (" +COLUMN_ID_TABLE_POINTER +")"
                + " LEFT JOIN (SELECT " +COLUMN_ID_TABLE_POINTER + " idPoint, "
                + COLUMN_MODULE_CODE + " modCode, "
                + COLUMN_START_TIME + " modStrt, "
                + COLUMN_END_TIME + " modEnd, "
                + COLUMN_ROOM + " modRoom, "
                + COLUMN_LECTURER + " modLec, "
                + COLUMN_DAY + " modDay, "
                + COLUMN_GROUP_NAME + " modGroup, "
                + COLUMN_TYPE + " modType, "
                + COLUMN_START_WEEK + " sWeek, "
                + COLUMN_END_WEEK + " eWeek FROM "
                +  TABLE_MODULE + " LEFT JOIN " +TABLE_CLASS_WEEKS
                + " USING(" + COLUMN_ID_TABLE_POINTER + ") ) as t"
                + " ON idPoint" + " = " + COLUMN_MODULE_POINTER
                + " WHERE " + COLUMN_ID_TABLE_POINTER + " = " + id
                +";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();

            if (c.getString(c.getColumnIndex("modulePointer")).equals("0")){
                //if (true){
                result = new StudentTimetable(
                        Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                        c.getString(c.getColumnIndex("moduleCode")),
                        Integer.parseInt(c.getString(c.getColumnIndex("modulePointer"))),
                        c.getString(c.getColumnIndex("startTime")),
                        Integer.parseInt(c.getString(c.getColumnIndex("day"))),
                        c.getString(c.getColumnIndex("endTime")),
                        Integer.parseInt(c.getString(c.getColumnIndex("studentID"))),
                        c.getString(c.getColumnIndex("notes")),
                        c.getString(c.getColumnIndex("groupName")),
                        c.getString(c.getColumnIndex("type")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("lecturer")),
                        c.getString(c.getColumnIndex("room")),
                        c.getInt(c.getColumnIndex("color"))
                );
                ArrayList<String> weeks = new ArrayList<>();
                weeks.add(c.getString(c.getColumnIndex("startWeek")) + "-" + c.getString(c.getColumnIndex("endWeek")));
                result.set_weeks(weeks);
            }

            else{
                result = new StudentTimetable(
                        Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                        c.getString(c.getColumnIndex("modCode")),
                        Integer.parseInt(c.getString(c.getColumnIndex("modulePointer"))),
                        c.getString(c.getColumnIndex("modStrt")),
                        Integer.parseInt(c.getString(c.getColumnIndex("modDay"))),
                        c.getString(c.getColumnIndex("modEnd")),
                        Integer.parseInt(c.getString(c.getColumnIndex("studentID"))),
                        c.getString(c.getColumnIndex("notes")),
                        c.getString(c.getColumnIndex("modGroup")),
                        c.getString(c.getColumnIndex("modType")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("modLec")),
                        c.getString(c.getColumnIndex("modRoom")),
                        c.getInt(c.getColumnIndex("color"))
                );
                ArrayList<String> weeks = new ArrayList<>();
                weeks.add(c.getString(c.getColumnIndex("sWeek")) + "-" + c.getString(c.getColumnIndex("eWeek")));
                result.set_weeks(weeks);
            }


        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage());

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }

    //get all from studentTimetable
    public ArrayList<StudentTimetable> getAllFromStudentTimetable(){
        ArrayList<StudentTimetable> result = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_STUDENT_TIMETABLE
                + " LEFT JOIN " + TABLE_CLASS_WEEKS
                + " USING (" +COLUMN_ID_TABLE_POINTER +")"
                + " LEFT JOIN (SELECT " +COLUMN_ID_TABLE_POINTER + " idPoint, "
                + COLUMN_MODULE_CODE + " modCode, "
                + COLUMN_START_TIME + " modStrt, "
                + COLUMN_END_TIME + " modEnd, "
                + COLUMN_ROOM + " modRoom, "
                + COLUMN_LECTURER + " modLec, "
                + COLUMN_DAY + " modDay, "
                + COLUMN_GROUP_NAME + " modGroup, "
                + COLUMN_TYPE + " modType, "
                + COLUMN_START_WEEK + " sWeek, "
                + COLUMN_END_WEEK + " eWeek FROM "
                +  TABLE_MODULE + " LEFT JOIN " +TABLE_CLASS_WEEKS
                + " USING(" + COLUMN_ID_TABLE_POINTER + ") ) as t"
                + " ON idPoint" + " = " + COLUMN_MODULE_POINTER
                +";";

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            do
            {
                if (c.getString(c.getColumnIndex("modulePointer")).equals("0")){
                //if (true){
                    result.add(new StudentTimetable(
                            Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                            c.getString(c.getColumnIndex("moduleCode")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modulePointer"))),
                            c.getString(c.getColumnIndex("startTime")),
                            Integer.parseInt(c.getString(c.getColumnIndex("day"))),
                            c.getString(c.getColumnIndex("endTime")),
                            Integer.parseInt(c.getString(c.getColumnIndex("studentID"))),
                            c.getString(c.getColumnIndex("notes")),
                            c.getString(c.getColumnIndex("groupName")),
                            c.getString(c.getColumnIndex("type")),
                            c.getString(c.getColumnIndex("title")),
                            c.getString(c.getColumnIndex("lecturer")),
                            c.getString(c.getColumnIndex("room")),
                            c.getInt(c.getColumnIndex("color"))
                    ));
                    ArrayList<String> weeks = new ArrayList<>();
                    weeks.add(c.getString(c.getColumnIndex("startWeek")) + "-" + c.getString(c.getColumnIndex("endWeek")));
                    result.get(result.size()-1).set_weeks(weeks);
                }

                else{
                    result.add(new StudentTimetable(
                            Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                            c.getString(c.getColumnIndex("modCode")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modulePointer"))),
                            c.getString(c.getColumnIndex("modStrt")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modDay"))),
                            c.getString(c.getColumnIndex("modEnd")),
                            Integer.parseInt(c.getString(c.getColumnIndex("studentID"))),
                            c.getString(c.getColumnIndex("notes")),
                            c.getString(c.getColumnIndex("modGroup")),
                            c.getString(c.getColumnIndex("modType")),
                            c.getString(c.getColumnIndex("title")),
                            c.getString(c.getColumnIndex("modLec")),
                            c.getString(c.getColumnIndex("modRoom")),
                            c.getInt(c.getColumnIndex("color"))
                    ));
                    ArrayList<String> weeks = new ArrayList<>();
                    weeks.add(c.getString(c.getColumnIndex("sWeek")) + "-" + c.getString(c.getColumnIndex("eWeek")));
                    result.get(result.size()-1).set_weeks(weeks);
                }

            }


            while (c.moveToNext());
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage());

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }

    // addToUsersTable
    public void addToUsersTable(int studentId, String studentName){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, studentId);
        values.put(COLUMN_USER_NAME, studentName);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    //get all studentTimetable users
    public HashMap<Integer, String> getUsers(){
        HashMap<Integer, String> result = new HashMap<>();

        String query = "SELECT * "
                + " FROM " + TABLE_USERS
                + ";";

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            do
            {
                result.put(c.getInt(c.getColumnIndex(COLUMN_USER_ID)), c.getString(c.getColumnIndex(COLUMN_USER_NAME)));

            } while (c.moveToNext());

        } catch (SQLiteException e) {
            Log.d("SQL Error", e.getMessage());
            return null;

        } catch (CursorIndexOutOfBoundsException ce) {
            Log.d("ID not found", ce.getMessage());

        } finally {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }


    //get all timetable results for a particular studentID
    public ArrayList<StudentTimetable> getAllFromStudentTimetable(int studentID){
        ArrayList<StudentTimetable> result = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_STUDENT_TIMETABLE
                + " LEFT JOIN " + TABLE_CLASS_WEEKS
                + " USING (" +COLUMN_ID_TABLE_POINTER +")"
                + " LEFT JOIN (SELECT " +COLUMN_ID_TABLE_POINTER + " idPoint, "
                + COLUMN_MODULE_CODE + " modCode, "
                + COLUMN_START_TIME + " modStrt, "
                + COLUMN_END_TIME + " modEnd, "
                + COLUMN_ROOM + " modRoom, "
                + COLUMN_LECTURER + " modLec, "
                + COLUMN_DAY + " modDay, "
                + COLUMN_GROUP_NAME + " modGroup, "
                + COLUMN_TYPE + " modType, "
                + COLUMN_START_WEEK + " sWeek, "
                + COLUMN_END_WEEK + " eWeek FROM "
                +  TABLE_MODULE + " LEFT JOIN " +TABLE_CLASS_WEEKS
                + " USING(" + COLUMN_ID_TABLE_POINTER + ") ) as t"
                + " ON idPoint" + " = " + COLUMN_MODULE_POINTER
                + " WHERE " +  COLUMN_STUDENT_ID + " = "+ studentID
                +";";



        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            do
            {
                if (c.getString(c.getColumnIndex("modulePointer")).equals("0")){
                    result.add(new StudentTimetable(
                            Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                            c.getString(c.getColumnIndex("moduleCode")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modulePointer"))),
                            c.getString(c.getColumnIndex("startTime")),
                            Integer.parseInt(c.getString(c.getColumnIndex("day"))),
                            c.getString(c.getColumnIndex("endTime")),
                            Integer.parseInt(c.getString(c.getColumnIndex("studentID"))),
                            c.getString(c.getColumnIndex("notes")),
                            c.getString(c.getColumnIndex("groupName")),
                            c.getString(c.getColumnIndex("type")),
                            c.getString(c.getColumnIndex("title")),
                            c.getString(c.getColumnIndex("lecturer")),
                            c.getString(c.getColumnIndex("room")),
                            c.getInt(c.getColumnIndex("color"))
                    ));
                    ArrayList<String> weeks = new ArrayList<>();
                    weeks.add(c.getString(c.getColumnIndex("startWeek")) + "-" + c.getString(c.getColumnIndex("endWeek")));
                    result.get(result.size()-1).set_weeks(weeks);
                }

                else{
                    result.add(new StudentTimetable(
                            Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                            c.getString(c.getColumnIndex("modCode")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modulePointer"))),
                            c.getString(c.getColumnIndex("modStrt")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modDay"))),
                            c.getString(c.getColumnIndex("modEnd")),
                            Integer.parseInt(c.getString(c.getColumnIndex("studentID"))),
                            c.getString(c.getColumnIndex("notes")),
                            c.getString(c.getColumnIndex("modGroup")),
                            c.getString(c.getColumnIndex("modType")),
                            c.getString(c.getColumnIndex("title")),
                            c.getString(c.getColumnIndex("modLec")),
                            c.getString(c.getColumnIndex("modRoom")),
                            c.getInt(c.getColumnIndex("color"))
                    ));
                    ArrayList<String> weeks = new ArrayList<>();
                    weeks.add(c.getString(c.getColumnIndex("sWeek")) + "-" + c.getString(c.getColumnIndex("eWeek")));
                    result.get(result.size()-1).set_weeks(weeks);
                }

            }
            while (c.moveToNext());
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage()+ query);

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }



    //get all timetable results for a particular studentID on a particular week
    public ArrayList<StudentTimetable> getAllFromStudentTimetable(int studentID, int week){
        ArrayList<StudentTimetable> result = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_STUDENT_TIMETABLE
                + " LEFT JOIN " + TABLE_CLASS_WEEKS
                + " USING (" +COLUMN_ID_TABLE_POINTER +")"
                + " LEFT JOIN (SELECT " +COLUMN_ID_TABLE_POINTER + " idPoint, "
                + COLUMN_MODULE_CODE + " modCode, "
                + COLUMN_START_TIME + " modStrt, "
                + COLUMN_END_TIME + " modEnd, "
                + COLUMN_ROOM + " modRoom, "
                + COLUMN_LECTURER + " modLec, "
                + COLUMN_DAY + " modDay, "
                + COLUMN_GROUP_NAME + " modGroup, "
                + COLUMN_TYPE + " modType, "
                + COLUMN_START_WEEK + " sWeek, "
                + COLUMN_END_WEEK + " eWeek FROM "
                +  TABLE_MODULE + " LEFT JOIN " +TABLE_CLASS_WEEKS
                + " USING(" + COLUMN_ID_TABLE_POINTER + ") ) as t"
                + " ON idPoint" + " = " + COLUMN_MODULE_POINTER
                + " WHERE " +  COLUMN_STUDENT_ID + " = "+ studentID
                +";";



        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            do
            {
                if (c.getString(c.getColumnIndex("modulePointer")).equals("0")
                        && week >= Integer.parseInt(c.getString(c.getColumnIndex("startWeek")))
                        && week <= Integer.parseInt(c.getString(c.getColumnIndex("endWeek")))){
                    result.add(new StudentTimetable(
                            Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                            c.getString(c.getColumnIndex("moduleCode")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modulePointer"))),
                            c.getString(c.getColumnIndex("startTime")),
                            Integer.parseInt(c.getString(c.getColumnIndex("day"))),
                            c.getString(c.getColumnIndex("endTime")),
                            Integer.parseInt(c.getString(c.getColumnIndex("studentID"))),
                            c.getString(c.getColumnIndex("notes")),
                            c.getString(c.getColumnIndex("groupName")),
                            c.getString(c.getColumnIndex("type")),
                            c.getString(c.getColumnIndex("title")),
                            c.getString(c.getColumnIndex("lecturer")),
                            c.getString(c.getColumnIndex("room")),
                            c.getInt(c.getColumnIndex("color"))
                    ));
                    ArrayList<String> weeks = new ArrayList<>();
                    weeks.add(c.getString(c.getColumnIndex("startWeek")) + "-" + c.getString(c.getColumnIndex("endWeek")));
                    result.get(result.size()-1).set_weeks(weeks);
                }

                else if (week >= Integer.parseInt(c.getString(c.getColumnIndex("sWeek")))
                    && week <= Integer.parseInt(c.getString(c.getColumnIndex("eWeek")))){

                    result.add(new StudentTimetable(
                            Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                            c.getString(c.getColumnIndex("modCode")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modulePointer"))),
                            c.getString(c.getColumnIndex("modStrt")),
                            Integer.parseInt(c.getString(c.getColumnIndex("modDay"))),
                            c.getString(c.getColumnIndex("modEnd")),
                            Integer.parseInt(c.getString(c.getColumnIndex("studentID"))),
                            c.getString(c.getColumnIndex("notes")),
                            c.getString(c.getColumnIndex("modGroup")),
                            c.getString(c.getColumnIndex("modType")),
                            c.getString(c.getColumnIndex("title")),
                            c.getString(c.getColumnIndex("modLec")),
                            c.getString(c.getColumnIndex("modRoom")),
                            c.getInt(c.getColumnIndex("color"))
                    ));
                    ArrayList<String> weeks = new ArrayList<>();
                    weeks.add(c.getString(c.getColumnIndex("sWeek")) + "-" + c.getString(c.getColumnIndex("eWeek")));
                    result.get(result.size()-1).set_weeks(weeks);
                    //Log.v("TESTING 2 PARAM ", "sWk: " + (c.getString(c.getColumnIndex("sWeek")))+ "-" + c.getString(c.getColumnIndex("eWeek")));
                }

            }
            while (c.moveToNext());
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage()+ query);

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }


    //get all from Module Table
    public ArrayList<Module> getAllFromModuleTable(){
        ArrayList<Module> result = new ArrayList<>();
        //String query = "SELECT * FROM " + TABLE_MODULE + ";";
        String query = "SELECT * FROM " + TABLE_MODULE + " JOIN " + TABLE_CLASS_WEEKS
                + " USING (" + COLUMN_ID_TABLE_POINTER + ")"
                +";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            do
            {
                result.add(new Module(
                        Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                        c.getString(c.getColumnIndex("moduleCode")),
                        c.getString(c.getColumnIndex("startTime")),
                        c.getString(c.getColumnIndex("endTime")),
                        c.getString(c.getColumnIndex("room")),
                        c.getString(c.getColumnIndex("lecturer")),
                        Integer.parseInt(c.getString(c.getColumnIndex("day"))),
                        c.getString(c.getColumnIndex("groupName")),
                        c.getString(c.getColumnIndex("type"))
                ));
                ArrayList<String> weeks = new ArrayList<>();
                weeks.add(c.getString(c.getColumnIndex("startWeek")) + "-" + c.getString(c.getColumnIndex("endWeek")));
                result.get(result.size()-1).set_weeks(weeks);
            }
            while (c.moveToNext());
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage());

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }


    //get all from Module Table with an ID & week
    public ArrayList<Module> getAllFromModuleTable(String moduleCode, int week){
        ArrayList<Module> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_MODULE + " JOIN " + TABLE_CLASS_WEEKS
                + " USING (" + COLUMN_ID_TABLE_POINTER + ")"
                + " WHERE " + COLUMN_MODULE_CODE + " = '" + moduleCode
                + "' AND " + COLUMN_START_WEEK + " <= " + week
                + " AND " + COLUMN_END_WEEK + " >= " + week
                +";";


        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            do
            {
                result.add(new Module(
                        Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                        c.getString(c.getColumnIndex("moduleCode")),
                        c.getString(c.getColumnIndex("startTime")),
                        c.getString(c.getColumnIndex("endTime")),
                        c.getString(c.getColumnIndex("room")),
                        c.getString(c.getColumnIndex("lecturer")),
                        Integer.parseInt(c.getString(c.getColumnIndex("day"))),
                        c.getString(c.getColumnIndex("groupName")),
                        c.getString(c.getColumnIndex("type"))
                ));

                ArrayList<String> weeks = new ArrayList<>();
                weeks.add(c.getString(c.getColumnIndex("startWeek")) + "-" + c.getString(c.getColumnIndex("endWeek")));
                result.get(result.size()-1).set_weeks(weeks);
                //Log.v("TESTING WEEKS: ", " " + (c.getString(c.getColumnIndex("startWeek")) + "-" + c.getString(c.getColumnIndex("endWeek"))));

            }
            while (c.moveToNext());
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage());

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }



    //get all from Module Table
    public ArrayList<Module> getAllFromModuleTable(String moduleCode){
        ArrayList<Module> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_MODULE + " JOIN " + TABLE_CLASS_WEEKS
                + " USING (" + COLUMN_ID_TABLE_POINTER + ")"
                + " WHERE " + COLUMN_MODULE_CODE + " = '" + moduleCode
                 + "';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            do
            {
                result.add(new Module(
                        Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer"))),
                        c.getString(c.getColumnIndex("moduleCode")),
                        c.getString(c.getColumnIndex("startTime")),
                        c.getString(c.getColumnIndex("endTime")),
                        c.getString(c.getColumnIndex("room")),
                        c.getString(c.getColumnIndex("lecturer")),
                        Integer.parseInt(c.getString(c.getColumnIndex("day"))),
                        c.getString(c.getColumnIndex("groupName")),
                        c.getString(c.getColumnIndex("type"))
                ));
                ArrayList<String> weeks = new ArrayList<>();
                weeks.add(c.getString(c.getColumnIndex("startWeek")) + "-" + c.getString(c.getColumnIndex("endWeek")));
                result.get(result.size()-1).set_weeks(weeks);
            }
            while (c.moveToNext());
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage());

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
        }


        return result;
    }

    //Add a note
    public void  insertNoteOnTimetableEntry(int IDTablePointer, String note){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE `" + TABLE_STUDENT_TIMETABLE + "` SET `" + COLUMN_NOTES +
                "` = '" + note  + "' WHERE " + COLUMN_ID_TABLE_POINTER + "= '" + IDTablePointer +"';";
        db.execSQL(query);
        db.close();
    }



    //Delete a row from moduleTimetable
    public void  deleteModuleEntryFromID(int IDTablePointer){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MODULE + " WHERE " + COLUMN_ID_TABLE_POINTER + "=\"" + IDTablePointer + "\";");
        db.close();

    }
    //delete all entries with this moduleID
    public void  deleteModule(int moduleID){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MODULE + " WHERE " + COLUMN_MODULE_CODE + "=\"" + moduleID + "\";");
        db.close();

    }

    //delete a row from studentTimetable
    public void  deleteStudentTimetableEntryFromID(int IDTablePointer){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STUDENT_TIMETABLE + " WHERE " + COLUMN_ID_TABLE_POINTER + "=\"" + IDTablePointer + "\";");
        db.close();
    }

    //delete a Module from studentTimetable for one Student
    public void  deleteModuleFromStudentTimetable(int modulePointer, int studentID){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STUDENT_TIMETABLE + " WHERE " + COLUMN_MODULE_POINTER + "=\"" + modulePointer + "\""
                + " AND " + COLUMN_STUDENT_ID + " = \"" + studentID + "\";");
        db.close();
    }

    public void  deleteAllFromModule(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MODULE + ";");
        db.close();
    }

    public void  deleteAllFromStudent(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STUDENT_TIMETABLE + ";");
        db.close();
    }

    public void  deleteAllClassWeeks(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CLASS_WEEKS + ";");
        db.close();
    }

    public void  deleteAllUIDs(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_UID + ";");
        db.close();
    }


    public int getUIDForModuleEntry(String moduleCode, int day, String start, String end){
        SQLiteDatabase db = getWritableDatabase();
        int result = 0;
        String query = "SELECT " + COLUMN_ID_TABLE_POINTER + " FROM " + TABLE_MODULE
                + " WHERE " + COLUMN_MODULE_CODE + " = '" + moduleCode
                + "' AND "+ COLUMN_DAY + " = " + day
                + " AND "+ COLUMN_START_TIME + " = '" + start
                + "' AND "+ COLUMN_END_TIME + " = '" + end
                + "';";
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            c.moveToFirst();
            result = Integer.parseInt(c.getString(c.getColumnIndex("idTablePointer")));
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage() + query);
            return 0;
        }
        catch (CursorIndexOutOfBoundsException ce)
        {
            Log.d("ID not found", ce.getMessage() + query);

        }
        finally
        {
            //release all resources
            if (c != null) c.close();
            db.close();
            if (c == null) return 0;
        }

        return result;
    }

}
