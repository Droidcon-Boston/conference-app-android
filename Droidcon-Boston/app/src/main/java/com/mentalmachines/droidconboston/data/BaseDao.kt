package com.mentalmachines.droidconboston.data

import androidx.room.Delete
import androidx.room.Insert

interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert
    fun insert(obj: T)

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     */
    @Insert
    fun insert(vararg obj: T)

    /**
     * Insert list of objects in the database.
     *
     * @param objs the objects to be inserted.
     */
    @Insert
    fun insertAll(objs: List<T>)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(obj: T)
}
