package hanson.bhargav.sqlite_database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dataHelper.ItemsDataBaseHelper
import hanson.bhargav.sqlite_database.databinding.ActivityAddExpensesBinding
import model.Items

class AddExpenses : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpensesBinding
    private lateinit var db: ItemsDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ItemsDataBaseHelper(this)

        // Item will be added on Table
        binding.addExpense.setOnClickListener {
            val item_name = binding.textItemName.text.toString().trim()
            val item_amount = binding.textItemAmount.text.toString().toDouble()

            if (binding.textItemName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter the field", Toast.LENGTH_SHORT).show()
            } else {
                val item = Items(0, item_name, item_amount)
                db.insertItems(item)
                finish()
                Toast.makeText(this, "Item has been added successfully", Toast.LENGTH_SHORT).show()
            }
        }

        // activity will be finished by clicking on Cancel
        binding.cancelButton.setOnClickListener {
            finish()
        }
    }
}