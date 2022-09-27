package com.example.noteit

import android.app.Application

class notesApp:Application() {
    // loads our data when ever needed
    val db by lazy {
        notesroomdatabase.getinstance(this)
    }
}