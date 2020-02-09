package com.example.po

import android.os.AsyncTask
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class AsyncHttp(var name: String, var value: Double) :
    AsyncTask<String?, Int?, Boolean>(), Parcelable {
    var urlConnection: HttpURLConnection? = null
    var flg = false

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble()
    ) {
        flg = parcel.readByte() != 0.toByte()
    }

    //非同期処理ここから
    override fun doInBackground(vararg p0: String?): Boolean? {
        val urlinput = "http://10.206.0.228/android_DB/post.php"
        try {
            val url = URL(urlinput)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection!!.requestMethod = "POST"
            urlConnection!!.doOutput = true
            //POST用パラメータ
            val postDataSample = "name=$name&text=$value"
            //POSTパラメータ設定
            val out = urlConnection!!.outputStream
            out.write(postDataSample.toByteArray())
            out.flush()
            out.close()
            Log.d("test", postDataSample)
            //レスポンスを受け取る
            urlConnection!!.inputStream
            flg = true
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return flg
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(value)
        parcel.writeByte(if (flg) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AsyncHttp> {
        override fun createFromParcel(parcel: Parcel): AsyncHttp {
            return AsyncHttp(parcel)
        }

        override fun newArray(size: Int): Array<AsyncHttp?> {
            return arrayOfNulls(size)
        }
    }
}