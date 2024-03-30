package adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import dataHelper.ItemsDataBaseHelper
import hanson.bhargav.sqlite_database.MainActivity
import hanson.bhargav.sqlite_database.R
import hanson.bhargav.sqlite_database.UpdateExpenses
import model.Items

class ItemAdapter(private var items: List<Items>, context: Context) :
    RecyclerView.Adapter<ItemAdapter.ItemsViewHolder>() {

    private val db: ItemsDataBaseHelper = ItemsDataBaseHelper(context)

    // It is assigning the id to view.
    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.rvItemName)
        val amountTextView: TextView = itemView.findViewById(R.id.rvItemAmount)
        val updateButton: Button = itemView.findViewById(R.id.buttonItemUpdate)
        val deleteButton: Button = itemView.findViewById(R.id.buttonItemDelete)
    }

    // It will set the layout for the item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
        return ItemsViewHolder(view)
    }

    // It will count the items in DataBase
    override fun getItemCount(): Int = items.size

    // It will bind the items with buttons and textView
    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.itemName
        holder.amountTextView.text = "$ "+ item.itemAmount.toString()
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateExpenses::class.java).apply {
                putExtra("expense_id", item.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        val builder = AlertDialog.Builder(holder.itemView.context)
        holder.deleteButton.setOnClickListener {
            builder.setTitle("Delete Item")
                .setMessage("Are you sure, you want to delete item ?")
                .setPositiveButton("Delete") { dialog, which ->
                    db.deleteItem(item.id)
                    refreshData(db.getAllItems())
                    Toast.makeText(
                        holder.itemView.context,
                        "Item has been deleted successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }

    // It will notify that data has been updated.
    fun refreshData(newItems: List<Items>) {
        items = newItems
        notifyDataSetChanged()
    }
}