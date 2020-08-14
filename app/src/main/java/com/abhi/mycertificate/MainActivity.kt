package com.abhi.mycertificate

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi.mycertificate.`interface`.OnItemClick
import com.abhi.mycertificate.models.RowModel
import com.abhi.mycertificate.models.Semester
import com.abhi.mycertificate.models.Subject
import com.abhi.mycertificate.models.Year
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.reflect.Field
import java.util.*

class MainActivity : AppCompatActivity(), OnItemClick {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var rowAdapter: RowAdapter
    lateinit var rows: MutableList<RowModel>

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recylerHome)

        rows = mutableListOf()
        layoutManager = LinearLayoutManager(this)
        rowAdapter = RowAdapter(this, rows, this)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )


        recyclerView.adapter = rowAdapter
        progressLayout = findViewById(R.id.progressLayout)

        progressBar = findViewById(R.id.progressBar)
        display("Institution")
    }

    private fun display(path: String) {
        progressLayout.visibility = VISIBLE
        val db = Firebase.firestore
        val TAG = "abhi123"
        db.collection(path)
            .get()
            .addOnSuccessListener { result ->

                rows.clear()

                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val stateList1: MutableList<Subject> = mutableListOf()
                    for (document1 in document.data) {
                        Log.d(TAG, "${document.id} => ${document1.key}")
                        val field = Model(document1.key, document1.value.toString())
                        stateList1.add(Subject(field, null))
                    }
                    rows.add(RowModel(RowModel.COUNTRY, Semester(document.id, stateList1)))
                }
                rowAdapter.notifyDataSetChanged()
                progressLayout.visibility = GONE
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        Log.d(TAG, "Error getting documents: ")
    }

    override fun onClick(value: Model) {


            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(value.FieldValue), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            val newIntent = Intent.createChooser(intent, "Open File")
            try {
                startActivity(newIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, " this is errorcatched ", Toast.LENGTH_SHORT)
                    .show()
            }


    }


}