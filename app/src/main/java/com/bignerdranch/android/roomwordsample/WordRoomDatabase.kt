package com.bignerdranch.android.roomwordsample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Word::class),version = 1,exportSchema = false)//Аннотирует класс как базу данных комнаты с таблицей (сущностью) класса Word
 abstract class WordRoomDatabase: RoomDatabase() {

    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.wordDao()

                    // Удаляет всё содержимое
                    wordDao.deleteAll()

                    // Добавляет слова
                    var word = Word("Hello")
                    wordDao.insert(word)
                    word = Word("World!")
                    wordDao.insert(word)

                    // TODO: Add your own words!
                    word = Word("TODO!")
                    wordDao.insert(word)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
//Класс базы данных для Room должен быть abstract и расширятьRoomDatabase.
//Вы аннотируете класс как базу данных Room @Database и используете параметры аннотации для объявления сущностей, которые принадлежат базе данных, и установки номера версии.
// Каждой сущности соответствует таблица, которая будет создана в базе данных. Миграции базы данных выходят за рамки этой кодовой таблицы,
// поэтому exportSchema здесь установлено значение false, чтобы избежать предупреждения сборки. В реальном приложении рассмотрите возможность установки каталога для Room,
// который будет использоваться для экспорта схемы, чтобы вы могли проверить текущую схему в своей системе контроля версий.
//База данных предоставляет DAO с помощью абстрактного метода получения для каждого @Dao.
//Вы определили синглтон , WordRoomDatabase,чтобы предотвратить одновременное открытие нескольких экземпляров базы данных.
//getDatabase возвращает синглтон. Он создаст базу данных при первом доступе, используя построитель базы данных Room,
// чтобы создать RoomDatabase объект в контексте приложения из WordRoomDatabase класса и присвоить ему имя "word_database".