package dataHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import model.Items

class ItemsDataBaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "items.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allitems"
        private const val COLUMN_ID = "id"
        private const val ITEM_NAME = "itemName"
        private const val ITEM_AMOUNT = "itemAmount"
    }

    // It will create the table
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $ITEM_NAME TEXT, $ITEM_AMOUNT REAL)"
        db?.execSQL(createTableQuery)
    }

    // To remove the table after version change
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    // Insert the items in Table
    fun insertItems(items: Items){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ITEM_NAME,items.itemName)
            put(ITEM_AMOUNT, items.itemAmount)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Retrieve all items from Table
    fun getAllItems(): List<Items>{
        val db = readableDatabase
        val itemList = mutableListOf<Items>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val item_id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val item_name = cursor.getString(cursor.getColumnIndexOrThrow(ITEM_NAME))
            val item_amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ITEM_AMOUNT))

            val item = Items(item_id,item_name,item_amount)
            itemList.add(item)
        }
        cursor.close()
        db.close()
        return itemList
    }

    // Update the item in Table
    fun updateExpenses(item: Items){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID,item.id)
            put(ITEM_NAME,item.itemName)
            put(ITEM_AMOUNT,item.itemAmount)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(item.id.toString())
        db.update(TABLE_NAME, values, whereClause,whereArgs)
        db.close()
    }

    // Get the information of item by unique ID.
    fun getItemByID(itemID: Int): Items{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $itemID"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val item_name = cursor.getString(cursor.getColumnIndexOrThrow(ITEM_NAME))
        val item_amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ITEM_AMOUNT))

        cursor.close()
        db.close()
        return Items(id,item_name,item_amount)
    }

    // Delete the item from table
    fun deleteItem(itemID: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(itemID.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    // Sum of the all amounts.
    fun sumAmount(): Double{
        val db = readableDatabase
        var sum: Double = 0.0
        val query = "SELECT SUM($ITEM_AMOUNT) FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst()){
            sum = cursor.getDouble(0)
            Log.d("TotalAmount",cursor.getDouble(0).toString())
        }
        cursor.close()
        db.close()

        return sum
    }
}