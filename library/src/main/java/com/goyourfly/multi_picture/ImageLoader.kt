package com.goyourfly.multi_picture

import android.net.Uri
import android.widget.ImageView
import java.io.Serializable

/**
 * Created by gaoyufei on 2017/6/22.
 */

interface ImageLoader : Serializable {

    fun loadImage(image: ImageView, uri: Uri)

}