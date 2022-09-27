package com.example.noteit

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noteit.databinding.ItemRowBinding

class ItemAdapter(private var items:ArrayList<notesentity>,
                  private var updateListner:(id:Int)->Unit,private var deleteListner:(id:Int)->Unit
            ):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


    class ViewHolder(binding: ItemRowBinding):RecyclerView.ViewHolder(binding.root){
      val llmain=binding.llMain
       val tvNAme=binding.tvName
        val tvEmail=binding.tvEmail
        val ivEdit=binding.ivEdit
        val ivDelete=binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context=holder.itemView.context
        val item=items[position]


        holder.tvNAme.text=item.title
        holder.tvEmail.text=item.desp
        if(position%2==0)
            holder.llmain.setBackgroundColor(ContextCompat.getColor(context,R.color.lightGray))
        else
            holder.llmain.setBackgroundColor(ContextCompat.getColor(context,R.color.deepgrey))

        holder.ivEdit.setOnClickListener{
            updateListner.invoke(item.Id)
        }
        holder.ivDelete.setOnClickListener{
           deleteListner.invoke(item.Id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}