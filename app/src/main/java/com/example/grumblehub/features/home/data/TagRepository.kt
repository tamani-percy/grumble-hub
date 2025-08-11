package com.example.grumblehub.features.home.data

import com.example.grumblehub.core.room.dao.TagDao
import com.example.grumblehub.core.room.entities.TagEntity

class TagRepository(
    private val tagDao: TagDao,
) {

    suspend fun getTags(): List<TagEntity> {
        return tagDao.getAllTags()
    }

    suspend fun createTag(tag: TagDto): Result<TagEntity> {
        return try {
            val tagEntity = TagEntity(
                name = tag.name,
                tagId = 0,
                createdAt = System.currentTimeMillis(),
                updatedAt = null,
            )
            tagDao.insertTag(tagEntity)
            Result.success(tagEntity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTag(tag: TagEntity) {
        tagDao.deleteTag(tag)
    }
}