package com.bignerdranch.android.roomwordsample

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    // Использование by lazy, поэтому база данных и репозиторий создаются только тогда, когда они нужны
    // а не при запуске приложения
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}
//Создал экземпляр базы данных. database
//Создал экземпляр репозитория на основе базы данных DAO. repository
//Поскольку эти объекты должны создаваться только тогда, когда они впервые нужны, а не при запуске
//приложения, вы используете делегирование свойств Kotlin: by lazy.