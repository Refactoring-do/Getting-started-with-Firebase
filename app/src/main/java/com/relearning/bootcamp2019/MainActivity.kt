package com.relearning.bootcamp2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var messagesList: ArrayList<Message> = ArrayList()
    private lateinit var adapter: FirebaseChatAdapter
    private var sendButton: Button? = null
    private var editText: EditText? = null
    private var username = "User" + (0..1000000).random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView?.layoutManager = LinearLayoutManager(this)

        adapter = FirebaseChatAdapter(messagesList, this)
        recyclerView?.adapter = adapter

        sendButton = findViewById(R.id.sendButton)

        editText = findViewById(R.id.editText)

        val database = FirebaseDatabase.getInstance()
        database.setLogLevel(Logger.Level.DEBUG)
        val myRef = database.getReference("messages")

        sendButton?.setOnClickListener {
            editText?.text.let {
                myRef.push().setValue(Message(username, it.toString()))
            }
            editText?.setText("")
        }
        myRef.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                var message = snapshot.getValue(Message::class.java)
                message?.let {
                    messagesList.add(it)
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}
