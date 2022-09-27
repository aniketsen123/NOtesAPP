package com.example.noteit

import androidx.room.*
import kotlinx.coroutines.flow.Flow
//if functions is suspended then don;t make it a flow return type
@Dao
interface notesdao {
    @Insert
    suspend fun insert(notesentity: notesentity)
    @Update
    suspend fun update(notesentity: notesentity)
    @Delete
    suspend fun delete(notesentity: notesentity)
    //that we are going to get.
    //
    //Like so.
    //
    //And now you might wonder, what the heck is this flow?
    //
    //Well, a flow is part of the routine class used to hold values that can always change at runtime.
    //
    //That's because it automatically emits a value.
    //
    //More like a live update.
    @Query("SELECT * FROM 'notes'")
    fun fetchallnotes():Flow<List<notesentity>>
    @Query("SELECT * FROM 'notes' where id=:id")
    fun fetchnotebyid(id:Int):Flow<notesentity>
}