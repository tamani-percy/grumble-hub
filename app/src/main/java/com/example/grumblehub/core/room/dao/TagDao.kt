package com.example.grumblehub.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grumblehub.core.room.entities.TagEntity

@Dao
interface TagDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertTag(tag: TagEntity)

     @Query("SELECT * FROM tags WHERE tagId = :tagId")
     suspend fun getTagById(tagId: Long): TagEntity?

     @Query("SELECT * FROM tags")
     suspend fun getAllTags(): List<TagEntity>

     @Delete
     suspend fun deleteTag(tag: TagEntity)
}