package com.example.projectgroceryapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), GroceryAdapter.GroceryItemClickInterface {

    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list:List<GroceryItems>
    lateinit var groceryAdapter: GroceryAdapter
    lateinit var groceryViewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemsRV= findViewById(R.id.idRecycleViewItem)
        addFAB= findViewById(R.id.FABAdd)
        list= ArrayList<GroceryItems>()
        groceryAdapter= GroceryAdapter(list,this)
        itemsRV.layoutManager= LinearLayoutManager(this)
        itemsRV.adapter= groceryAdapter
        val groceryRepository= GroceryRepository(GroceryDatabase(this))
        val factory= GroceryViewModelFactory(groceryRepository)
        groceryViewModel= ViewModelProvider(this,factory).get(GroceryViewModel::class.java)
        groceryViewModel.getAllGroceryItems().observe(this, Observer{
            groceryAdapter.list= it
            groceryAdapter.notifyDataSetChanged()
        })

        addFAB.setOnClickListener {
            openDialog()
        }

    }

    fun openDialog(){
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.grocery_add_dialog)
        val cancelBtn = dialog.findViewById<AppCompatButton>(R.id.idBtnCancel)
        val addBtn = dialog.findViewById<AppCompatButton>(R.id.idBtnAdd)
        val itemEdtName= dialog.findViewById<EditText>(R.id.idEditItemName)
        val itemEdtQuantity= dialog.findViewById<EditText>(R.id.idEditItemQuantity)
        val itemEdtPrice= dialog.findViewById<EditText>(R.id.idEditItemPrice)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        addBtn.setOnClickListener {
            val itemName: String= itemEdtName.text.toString()
            val itemQuantity: String= itemEdtQuantity.text.toString()
            val itemPrice: String= itemEdtPrice.text.toString()
            val qty: Int =itemQuantity.toInt()
            val pr: Double= itemPrice.toDouble()
            if(itemName.isNotEmpty()&& itemQuantity.isNotEmpty() && itemPrice.isNotEmpty()){
                val items= GroceryItems(itemName, qty, pr)
                groceryViewModel.insert(items)
                Toast.makeText(applicationContext, "Item Inserted..", Toast.LENGTH_SHORT).show()
                groceryAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }else{
                Toast.makeText(applicationContext, "Please enter all the data..", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()

    }

    override fun onItemClick(groceryItems: GroceryItems) {
        groceryViewModel.delete(groceryItems)
        groceryAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext, "Item Deleted.. ", Toast.LENGTH_SHORT).show()

    }
}