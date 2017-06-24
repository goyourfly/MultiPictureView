package com.goyourfly.multi_picture_demo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.goyourfly.multi_picture.ImageLoader
import com.goyourfly.multi_picture.MultiPictureView
import java.util.*

class MainActivity : AppCompatActivity() {
    val requestCodeAddImage = 1
    val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler) as RecyclerView }
    val adapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val list = mutableListOf<Item>()
        for (i in 0..10) {
            val uris = mutableListOf<Uri>()
            for (j in 0..Random().nextInt(7) + 1) {
                val offset = Random().nextInt(URLS.size - 9)
                uris.add(Uri.parse(URLS[offset + j]))
            }
            list.add(Item("I believe if you keep your faith, you keep your trust, you keep the right attitude, if you're grateful, you'll see God open up new doors."
                    , uris))
        }

        adapter.setItems(list)


        findViewById(R.id.fab)
                .setOnClickListener {
                    startActivityForResult(Intent(this@MainActivity, AddItemActivity::class.java),requestCodeAddImage)
                }
    }


    class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        private val list = mutableListOf<Item>()
        override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
            return ViewHolder(
                    LayoutInflater
                            .from(viewGroup.context)
                            .inflate(R.layout.item_content, viewGroup, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            holder.textContent.text = item.text
            holder.multiPictureView.setList(item.images)
        }

        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val textContent = view.findViewById(R.id.text_content) as TextView
            val multiPictureView = view.findViewById(R.id.multi_image_view) as MultiPictureView

            init {
                MultiPictureView.setImageLoader(object : ImageLoader {
                    override fun loadImage(image: ImageView, uri: Uri) {
                        Glide.with(image.context)
                                .load(uri)
                                .placeholder(R.drawable.ic_placeholder_loading)
                                .into(image)
                    }
                })
            }
        }

        fun addItem(item: Item) {
            list.add(item)
            notifyItemInserted(list.indexOf(item))
        }

        fun setItems(items: List<Item>) {
            list.clear()
            list.addAll(items)
            notifyDataSetChanged()
        }

        fun removeItem(item: Item) {
            val index = list.indexOf(item)
            list.removeAt(index)
            notifyItemRemoved(index)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("...","onActivityResult:$requestCode,image:$requestCode")
        if(resultCode == Activity.RESULT_OK && requestCode == requestCodeAddImage){
           if(data != null){
               val text = data.getStringExtra("text")
               val uris = data.getParcelableArrayListExtra<Uri>("image")

               Log.d("...","onActivityResult:$text,image:$uris")
               adapter.addItem(Item(text,uris))
           }
        }
    }

    data class Item(val text: String, val images: List<Uri>)


    val BASE = "http://i.imgur.com/"
    val EXT = ".jpg"
    val URLS = arrayOf(BASE + "CqmBjo5" + EXT, BASE + "zkaAooq" + EXT, BASE + "0gqnEaY" + EXT, BASE + "9gbQ7YR" + EXT, BASE + "aFhEEby" + EXT, BASE + "0E2tgV7" + EXT, BASE + "P5JLfjk" + EXT, BASE + "nz67a4F" + EXT, BASE + "dFH34N5" + EXT, BASE + "FI49ftb" + EXT, BASE + "DvpvklR" + EXT, BASE + "DNKnbG8" + EXT, BASE + "yAdbrLp" + EXT, BASE + "55w5Km7" + EXT, BASE + "NIwNTMR" + EXT, BASE + "DAl0KB8" + EXT, BASE + "xZLIYFV" + EXT, BASE + "HvTyeh3" + EXT, BASE + "Ig9oHCM" + EXT, BASE + "7GUv9qa" + EXT, BASE + "i5vXmXp" + EXT, BASE + "glyvuXg" + EXT, BASE + "u6JF6JZ" + EXT, BASE + "ExwR7ap" + EXT, BASE + "Q54zMKT" + EXT, BASE + "9t6hLbm" + EXT, BASE + "F8n3Ic6" + EXT, BASE + "P5ZRSvT" + EXT, BASE + "jbemFzr" + EXT, BASE + "8B7haIK" + EXT, BASE + "aSeTYQr" + EXT, BASE + "OKvWoTh" + EXT, BASE + "zD3gT4Z" + EXT, BASE + "z77CaIt" + EXT)
}
