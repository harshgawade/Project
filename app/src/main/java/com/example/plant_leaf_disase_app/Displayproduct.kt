package com.example.plant_leaf_disase_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Displayproduct : AppCompatActivity() {

    private var database: FirebaseDatabase? = null
    private var ref: DatabaseReference? = null

    private var adapter:AdapterDisplay?=null
    private var list: ArrayList<My>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displayproduct)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        list = ArrayList()


        val prefs = getSharedPreferences("My", MODE_PRIVATE)
        val add = prefs.getString("address", "No name defined")
        Toast.makeText(applicationContext,add.toString(),Toast.LENGTH_LONG).show()
        database = FirebaseDatabase.getInstance()


        val mDatabaseRef = FirebaseDatabase.getInstance().getReference("place")

        val query: Query = mDatabaseRef.orderByChild("address").equalTo(add)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                print(dataSnapshot)


                for (data in dataSnapshot.children) {
                    println(data)




                    val models: My? = data.getValue(My::class.java)
                    println(models)
                    if (models != null) {
                        list!!.add(models)
                    }

                }
                adapter = AdapterDisplay(list,applicationContext)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
}