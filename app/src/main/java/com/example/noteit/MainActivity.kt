package com.example.noteit

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteit.databinding.ActivityMainBinding
import com.example.noteit.databinding.UpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        val notesDao = (application as notesApp).db.notesDoa()
        binding?.button?.setOnClickListener {
            add(notesDao)
        }
        //Fetching to check if notes are beign saved or not
        lifecycleScope.launch {
            notesDao.fetchallnotes().collect() {
                val list = ArrayList(it)
                setupDataInrecyclerView(list, notesDao)
            }
        }
    }

    private fun add(notesdao: notesdao) {
        val title = binding?.ettitle?.text.toString()
        val desp = binding?.etdetail?.text.toString()
        if (title.isNotEmpty() && desp.isNotEmpty()) {
            lifecycleScope.launch {
                notesdao.insert(notesentity(title = title, desp = desp))
                Toast.makeText(this@MainActivity, "Note Added", Toast.LENGTH_SHORT).show()
                binding?.etdetail?.text?.clear()
                binding?.ettitle?.text?.clear()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Enter the above mentioned things",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupDataInrecyclerView(notesList: ArrayList<notesentity>, notesdao: notesdao) {
        if (notesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(notesList,
                { update -> Dialog(update, notesdao) },
                { delete -> delete(delete, notesdao) }
            )
            binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
            binding?.recyclerView?.adapter = itemAdapter
            binding?.recyclerView?.visibility = View.VISIBLE
            binding?.textView3?.visibility = View.GONE
        } else {
            binding?.recyclerView?.visibility = View.GONE
            binding?.textView3?.visibility = View.VISIBLE
        }
    }


    fun Dialog(id: Int, notesdao: notesdao) {
        val updatedialog =
            android.app.Dialog(this, R.style.Theme_Dialog)
        updatedialog.setCanceledOnTouchOutside(false)
        val binding = UpdateBinding.inflate(layoutInflater)
        updatedialog.setContentView(binding.root)
        lifecycleScope.launch {
            notesdao.fetchnotebyid(id).collect {
                if(it!=null)
                {
                    binding.etUpdateTitle.setText(it.title)
                    binding.etUpdateDetail.setText(it.desp)
                }
            }
        }
        binding.tvUpdate.setOnClickListener {
            val title = binding.etUpdateTitle.text.toString()
            val desp = binding.etUpdateDetail.text.toString()
            if (title.isNotEmpty() && desp.isNotEmpty()) {
                lifecycleScope.launch {
                    notesdao.update(notesentity(id, title,desp))
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_SHORT).show()
                    updatedialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, "Fill it properly", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvCancel.setOnClickListener {
            updatedialog.dismiss()
        }
        updatedialog.show()
    }

    fun delete(id: Int, notesdao: notesdao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(R.drawable.ic_action_delete)
        lifecycleScope.launch{
          notesdao.fetchnotebyid(id).collect{
              if(it!=null)
                  builder.setMessage("Are you sure you wants to delete ${it.title}")
          }
        }
      builder.setPositiveButton("Yes"){dialoginterface,_->
          lifecycleScope.launch{
              notesdao.delete(notesentity(id))
              Toast.makeText(applicationContext,"Record Deleted",Toast.LENGTH_SHORT).show()
          }
          dialoginterface.dismiss()
      }
        builder.setNegativeButton("No"){
            dialoginterface,_->
            dialoginterface.dismiss()
        }
        val alertdialog:AlertDialog=builder.create()
        alertdialog.setCanceledOnTouchOutside(false)
        alertdialog.show()

    }
}




