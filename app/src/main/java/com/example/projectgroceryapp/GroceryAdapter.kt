package com.example.projectgroceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView

class GroceryAdapter(
    var list: List<GroceryItems>,
    val groceryItemClickInterface: GroceryItemClickInterface
): RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>() {

    inner class GroceryViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val name= itemView.findViewById<TextView>(R.id.idItemName)
        val quantity= itemView.findViewById<TextView>(R.id.idQuantity)
        val rate= itemView.findViewById<TextView>(R.id.idRate)
        val totCost= itemView.findViewById<TextView>(R.id.idTotCost)
        val delete= itemView.findViewById<ImageView>(R.id.idimgDelete)

    }

    interface GroceryItemClickInterface{
        fun onItemClick(groceryItems: GroceryItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.grocery_rv_item, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        holder.name.text= list.get(position).itemName

        holder.quantity.text= list.get(position).itemQuantity.toString()

        holder.rate.text= "Rs. "+list.get(position).itemPrice.toString()

        val itemTotal: Double= list.get(position).itemQuantity *list.get(position).itemPrice
        holder.totCost.text ="Rs. "+itemTotal.toString()

        holder.delete.setOnClickListener{
            groceryItemClickInterface.onItemClick(list.get(position))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}