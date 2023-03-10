package com.android.justordinarymovieapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.justordinarymovieapp.model.genre.Genre

@Entity(tableName = GenreEntity.TABLE_NAME)
data class GenreEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "name")
    val name: String? = null

) {
    companion object {
        const val TABLE_NAME = "table_genre"
    }

    object ModelMapper {
        fun fromGenre(genre: Genre?): GenreEntity {
            return GenreEntity(
                id = genre?.id,
                name = genre?.name
            )
        }
    }
}