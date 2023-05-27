package com.swan.swanbsdiffapk

class BsPatch {
    companion object {
        init {
            System.loadLibrary("swanbsdiffapk")
        }
        external fun bsPatch(oldApk:String, patch: String, output:String)
    }


}