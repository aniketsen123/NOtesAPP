package com.example.noteit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [notesentity::class], version = 1)
abstract  class notesroomdatabase: RoomDatabase() {
    abstract fun notesDoa(): notesdao

    companion object {
        //when instances value is update  then all threads are updated
        @Volatile
        private var INSTANCE: notesroomdatabase? = null

        fun getinstance(context: Context): notesroomdatabase {
            //2 thread try to create the wsame object
            synchronized(this) {
                var instances = INSTANCE
                if (instances == null) {
                    instances = Room.databaseBuilder(
                        context.applicationContext,
                        notesroomdatabase::class.java,
                        "notes_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instances
                }
                return instances
            }


        }
    }
}