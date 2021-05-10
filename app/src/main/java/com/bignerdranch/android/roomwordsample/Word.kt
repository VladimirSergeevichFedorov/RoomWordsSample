package com.bignerdranch.android.roomwordsample

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity(tableName = "word_table")
class Word(@PrimaryKey @ColumnInfo(name = "word") val word: String)
//@Entity(tableName = "word_table")Каждый @Entityкласс представляет собой таблицу SQLite.
// Аннотируйте свое объявление класса, чтобы указать, что это сущность.
// Вы можете указать имя таблицы, если хотите, чтобы оно отличалось от имени класса. Это называет таблицу "word_table".
//@PrimaryKeyКаждой сущности нужен первичный ключ. Для простоты каждое слово действует как собственный первичный ключ.
//@ColumnInfo(name = "word")Задает имя столбца в таблице, если вы хотите, чтобы оно отличалось от имени переменной-члена.
// Это имя столбца «слово».