package com.bignerdranch.android.roomwordsample

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

//import androidx.constraintlayout.helper.widget.Flow

//import java.util.concurrent.Flow

// Объявляет DAO как частное свойство в конструкторе. Пройти в DAO
// вместо всей базы данных, потому что вам нужен только доступ к DAO
class WordRepository (private val wordDao: WordDao){

    // Room выполняет все запросы в отдельном потоке.
    // Наблюдаемый поток уведомит наблюдателя об изменении данных.
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // По умолчанию Room запускает запросы на приостановку вне основного потока, поэтому нам не нужно
    // реализуем что-нибудь еще, чтобы гарантировать, что мы не будем долго работать с базой данных
    // отключение от основного потока.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word:Word){
        wordDao.insert(word)
    }
}
//DAO передается конструктору репозитория, а не всей базе данных. Это связано с тем, что ему нужен только доступ к DAO,
// поскольку DAO содержит все методы чтения / записи для базы данных. Нет необходимости предоставлять всю базу данных
// в репозиторий.
//Список слов является публичным достоянием. Он инициализируется получением Flow списка слов из Room;
// это можно сделать благодаря тому, как вы определили getAlphabetizedWords метод возврата Flow на шаге
// «Наблюдение за изменениями базы данных». Room выполняет все запросы в отдельном потоке.
//suspendМодификатор указывает компилятору , что это должен быть вызван из сопрограммы или другой функции подвешенной.
//Room выполняет запросы на приостановку вне основного потока.