package net.nym.wificrack.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import net.nym.wificrack.R;
import net.nym.wificrack.bean.WIFIInfo;
import net.nym.wificrack.common.BaseApplication;
import net.nym.wificrack.utils.ContextUtils;
import net.nym.wificrack.utils.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author nym
 * @date 2015/10/14 0014.
 * @since 1.0
 */
public class SqliteSDCardHelper {
    private final String DB_NAME = "wifi.db";
    /**
     * {表名}
     * */
    private final String[] TABLE_WIFI = {"wifi","SSID","password"};
    private SQLiteDatabase mSQLiteDatabase;
    private static SqliteSDCardHelper my;

    public static SqliteSDCardHelper getInstance(){
        if (my == null){
            my = new SqliteSDCardHelper();
        }
        return my;
    }

    private SqliteSDCardHelper() {
        if (ContextUtils.hasExternalStorage()){
            File path = new File(Environment.getExternalStorageDirectory()
                    , BaseApplication.getAppContext().getString(R.string.dir_name));
            if (!path.exists()){
                path.mkdirs();
            }
            File file = new File(path, DB_NAME);

            if (!file.exists()){
                try {
                    file.createNewFile();
                    String sql = String.format("CREATE TABLE IF NOT EXISTS %1s" +
                            "(_id INTEGER PRIMARY KEY,%2s TEXT UNIQUE NOT NULL,%3s TEXT)"
                            ,TABLE_WIFI);
                    mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file,null);
                    mSQLiteDatabase.beginTransaction();
                    try {
                        mSQLiteDatabase.execSQL(sql);
                        mSQLiteDatabase.setTransactionSuccessful();
                        Log.i("sql=%s", sql);
                    } finally {
                        mSQLiteDatabase.endTransaction();
                        mSQLiteDatabase.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public SQLiteDatabase getSQLiteDatabase() {
        synchronized (this) {
            if (mSQLiteDatabase != null){
                if (!mSQLiteDatabase.isOpen()){
                    mSQLiteDatabase.close();
                }
            }
            File path = new File(Environment.getExternalStorageDirectory()
                    , BaseApplication.getAppContext().getString(R.string.dir_name));
            File file = new File(path, DB_NAME);
            mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file,null);
            return mSQLiteDatabase;
        }
    }

    /**
     * Query the given table, returning a {@link Cursor} over the result set.
     *
     * @param table
     *            The table name to compile the query against.
     * @param columns
     *            A list of which columns to return. Passing null will return
     *            all columns, which is discouraged to prevent reading data from
     *            storage that isn't going to be used.
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL
     *            WHERE clause (excluding the WHERE itself). Passing null will
     *            return all rows for the given table.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the
     *            values from selectionArgs, in order that they appear in the
     *            selection. The values will be bound as Strings.
     * @param groupBy
     *            A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having
     *            A filter declare which row groups to include in the cursor, if
     *            row grouping is being used, formatted as an SQL HAVING clause
     *            (excluding the HAVING itself). Passing null will cause all row
     *            groups to be included, and is required when row grouping is
     *            not being used.
     * @param orderBy
     *            How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @return A {@link Cursor} object, which is positioned before the first
     *         entry. Note that {@link Cursor}s are not synchronized, see the
     *         documentation for more details.
     * @see Cursor
     */
    public Cursor select(String table, String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String having,
                         String orderBy) {
        SQLiteDatabase db = getSQLiteDatabase();
        Cursor cursor = db.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy);
        return cursor;
    }

    /**
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @return
     */
    public Cursor select(String table, String[] columns, String selection,
                         String[] selectionArgs) {
        return select(table, columns, selection, selectionArgs, null, null,
                null);
    }

    /**
     * Convenience method for inserting a row into the database.
     *
     * @param table
     *            the table to insert the row into
     * @param nullColumnHack
     *            optional; may be <code>null</code>. SQL doesn't allow
     *            inserting a completely empty row without naming at least one
     *            column name. If your provided <code>values</code> is empty, no
     *            column names are known and an empty row can't be inserted. If
     *            not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert
     *            a NULL into in the case where your <code>values</code> is
     *            empty.
     * @param values
     *            this map contains the initial column values for the row. The
     *            keys should be the column names and the values the column
     *            values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public synchronized long insert(String table, String nullColumnHack,
                                    ContentValues values) {
        SQLiteDatabase db = getSQLiteDatabase();

        long flag = db.replace(table, nullColumnHack, values);
        db.close();
        return flag;
    }

    /**
     * Convenience method for updating rows in the database.
     *
     * @param table
     *            the table to update in
     * @param values
     *            a map from column names to new column values. null is a valid
     *            value that will be translated to NULL.
     * @param whereClause
     *            the optional WHERE clause to apply when updating. Passing null
     *            will update all rows.
     * @return the number of rows affected
     */
    public synchronized int update(String table, ContentValues values,
                                   String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getSQLiteDatabase();
        int flag = db.update(table, values, whereClause, whereArgs);
        db.close();
        return flag;
    }

    /**
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
     */
    public synchronized int delete(String table, String whereClause,
                                   String[] whereArgs) {
        SQLiteDatabase db = getSQLiteDatabase();
        int flag = db.delete(table, whereClause, whereArgs);
        db.close();
        return flag;
    }

    /**
     * Convenience method for replacing a row in the database.
     *
     * @param table the table in which to replace the row
     * @param nullColumnHack optional; may be <code>null</code>.
     *            SQL doesn't allow inserting a completely empty row without
     *            naming at least one column name.  If your provided <code>initialValues</code> is
     *            empty, no column names are known and an empty row can't be inserted.
     *            If not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert a NULL into
     *            in the case where your <code>initialValues</code> is empty.
     * @param initialValues this map contains the initial column values for
     *   the row.
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long replace(String table, String nullColumnHack, ContentValues initialValues) {
        SQLiteDatabase db = getSQLiteDatabase();
        long flag = db.replace(table, nullColumnHack, initialValues);
        db.close();
        return flag;
    }

    public synchronized long addToWIFI(WIFIInfo info) {
        ContentValues values = new ContentValues();
        values.put(TABLE_WIFI[1], info.getSSID() + "");
        values.put(TABLE_WIFI[2], info.getPassword() + "");

        return replace(TABLE_WIFI[0],null,values);
    }

    public synchronized long addToWIFI(String ssid,String password) {
        ContentValues values = new ContentValues();
        values.put(TABLE_WIFI[1], ssid + "");
        values.put(TABLE_WIFI[2], password + "");

        return replace(TABLE_WIFI[0],null,values);
    }

    public int deleteWIFI(String SSID) {
        return delete(TABLE_WIFI[0], TABLE_WIFI[2] + "=? ", new String[]{SSID});
    }

    public List<HashMap<String,Object>> getAllWifi(){
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        Cursor cursor = select(TABLE_WIFI[0],new String[]{"*"},null,null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do {

                    HashMap<String,Object> info = new HashMap<String,Object>();
                    info.put(TABLE_WIFI[1],cursor.getString(cursor.getColumnIndex(TABLE_WIFI[1])));
                    info.put(TABLE_WIFI[2], cursor.getString(cursor.getColumnIndex(TABLE_WIFI[2])));

                    list.add(info);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }


    public void destroy(){
        if (mSQLiteDatabase != null){
            if (mSQLiteDatabase.isOpen()){
                mSQLiteDatabase.close();
            }
        }
    }
}
