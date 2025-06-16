package com.example.catalist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDao {

    @Query("SELECT * FROM catinfoentity")
    suspend fun getAllCats(): List<CatInfoEntity>

    @Query("DELETE FROM catinfoentity")
    suspend fun deleteAllCats()

    @Query("SELECT * FROM catinfoentity WHERE id = :id")
    suspend fun getCatById(id: String): CatInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCats(cat: List<CatInfoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(cat: CatInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<Image>)

    @Query("SELECT * FROM image WHERE catId = :catId")
    suspend fun getAllImagesBy(catId: String): List<Image>
}
