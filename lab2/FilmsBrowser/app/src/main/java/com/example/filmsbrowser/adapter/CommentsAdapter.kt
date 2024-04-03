package com.example.filmsbrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.filmsbrowser.R
import com.example.filmsbrowser.model.Comment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class CommentsAdapter(context: Context, private val commentsRef: DatabaseReference) :
    ArrayAdapter<Comment>(context, 0) {

    private val comments: MutableList<Comment> = mutableListOf()

    init {
        commentsRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val comment = snapshot.getValue(Comment::class.java)
                comment?.let {
                    comments.add(comment)
                    notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val comment = snapshot.getValue(Comment::class.java)
                comment?.let {
                    comments.remove(comment)
                    notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun getCount(): Int {
        return comments.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        }

        val comment = comments[position]

        val tvAuthor: TextView = itemView!!.findViewById(R.id.tvCommentAuthor)
        tvAuthor.text = comment.author

        val tvText: TextView = itemView.findViewById(R.id.tvCommentText)
        tvText.text = comment.text

        return itemView
    }
}
