package hanson.bhargav.sqlite_database

import adapter.ItemAdapter
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import dataHelper.ItemsDataBaseHelper
import hanson.bhargav.sqlite_database.databinding.ActivityMainBinding
import model.Items

class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding
    private lateinit var db: ItemsDataBaseHelper
    private lateinit var itemsAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        db = ItemsDataBaseHelper(this)
        itemsAdapter = ItemAdapter(db.getAllItems(),this)

        // RecycleView layout manager and adapter
        binder.rvItems.layoutManager = LinearLayoutManager(this)
        binder.rvItems.adapter = itemsAdapter

        // Add Item Button
        binder.addExpenseButton.setOnClickListener{
            val intent = Intent(this,AddExpenses::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        itemsAdapter.refreshData(db.getAllItems())
    }
}