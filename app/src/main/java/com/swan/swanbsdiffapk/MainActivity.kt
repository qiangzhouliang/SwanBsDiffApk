package com.swan.swanbsdiffapk

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.swan.swanbsdiffapk.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sampleText.text = BuildConfig.VERSION_NAME

        // 运行时权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            var perms = arrayOf(WRITE_EXTERNAL_STORAGE)
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(perms, 1000)
            }
        }
    }

    fun update(view: View) {
        // 服务器下载一个 patch
        object :AsyncTask<Void, Void, File>(){
            override fun doInBackground(vararg params: Void?): File {
                // 获取旧版本路径（正在运行的apk路径）
                val patch = File(applicationContext.filesDir, "path").absolutePath
                val oldApk = File(applicationContext.filesDir, "old.apk").absolutePath
                val output = createNewApk().absolutePath
                // 调用jni进行合成
                BsPatch.bsPatch(oldApk, patch, output)
                return File(output)
            }

            override fun onPostExecute(result: File?) {
                super.onPostExecute(result)
                // 安装
                result?.let { install(it.absolutePath) }

            }
        }
    }

    private fun createNewApk(): File {
        val newApk = File(applicationContext.filesDir, "bsdiff.apk")
        if (!newApk.exists()){
            newApk.createNewFile()
        }

        return newApk;
    }

    private fun install(apkPath: String) {
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            startActivity(
                Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setDataAndType(
                        FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            File(apkPath)
                        ), "application/vnd.android.package-archive"
                    )
            )
        } else {
            startActivity(
                Intent(Intent.ACTION_VIEW).setDataAndType(
                    Uri.fromFile(File(apkPath)),
                    "application/vnd.android.package-archive"
                )
            )
        }
    }


}