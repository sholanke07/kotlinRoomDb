package com.example.kotlinnewroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener{

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initViewModel()
        searchUser()

        createUserButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, create_new_user::class.java))
        }
    }

    private fun searchUser(){
        search_button.setOnClickListener {
            if (!TextUtils.isEmpty(searchEditText.text.toString())){
                viewModel.searchUser(searchEditText.text.toString())

            }else{
                viewModel.getUserList()
            }
        }
    }

    private fun initRecyclerView(){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val divider = DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
            recyclerViewAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = recyclerViewAdapter

        }
    }

    fun initViewModel(){
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
        viewModel.getUserListObserverable().observe(this, Observer<UserList> {
            if (it == null){
                Toast.makeText(this@MainActivity, "No Result Found....", Toast.LENGTH_LONG).show()
            }else{
                recyclerViewAdapter.userList = it.data.toMutableList()
                recyclerViewAdapter.notifyDataSetChanged()
            }
        })
           viewModel.getUserList()

    }

    override fun onItemEditClick(user: User) {
        val intent = Intent(this@MainActivity, create_new_user::class.java)
        intent.putExtra("user_id", user.id)
        startActivityForResult(intent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
    }
}