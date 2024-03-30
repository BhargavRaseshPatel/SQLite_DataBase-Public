package hanson.bhargav.sqlite_database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import dataHelper.ItemsDataBaseHelper
import hanson.bhargav.sqlite_database.databinding.ActivityUpdateExpensesBinding
import model.Items

class UpdateExpenses : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateExpensesBinding
    private lateinit var db: ItemsDataBaseHelper
    private var itemID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ItemsDataBaseHelper(this)
        itemID = intent.getIntExtra("expense_id",-1)
        if(itemID == -1 ){
            finish()
            return
        }

        val item = db.getItemByID(itemID)
        binding.updateItemName.setText(item.itemName)
        binding.updateItemAmount.setText(item.itemAmount.toString())

        // Item will be updated on Table
        binding.updateExpense.setOnClickListener {
            val newItemName = binding.updateItemName.text.toString().trim()
            val newItemAmount = binding.updateItemAmount.text.toString().toDouble()

            Log.d("AmountValue",newItemAmount.toString())

            if (binding.updateItemName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter the field", Toast.LENGTH_SHORT).show()
            }
            else {
                val updateItem = Items(itemID, newItemName, newItemAmount)
                db.updateExpenses(updateItem)
                finish()
                Toast.makeText(this, "Item has been updated successfully", Toast.LENGTH_SHORT).show()
            }
        }

        // activity will be finished by clicking on Cancel
        binding.cancelButton.setOnClickListener{
            finish()
        }
    }
}