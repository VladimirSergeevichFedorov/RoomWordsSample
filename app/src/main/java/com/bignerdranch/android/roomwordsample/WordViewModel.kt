package com.bignerdranch.android.roomwordsample

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class WordViewModel(private val repository: WordRepository): ViewModel() {

    // Использование LiveData и кеширование того, что возвращает allWords, имеет несколько преимуществ:
    // - Мы можем поместить наблюдателя на данные (вместо опроса на предмет изменений) и только обновить
    // пользовательский интерфейс при фактическом изменении данных.
    // - Репозиторий полностью отделен от UI через ViewModel.
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()
    /**
     *Запуск новой сопрограммы для вставки данных неблокирующим способом
    */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}
class WordViewModelFactory(private val repository: WordRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
//создал класс с именем, WordViewModelкоторый получает в WordRepositoryкачестве параметра и расширяется ViewModel.
// Репозиторий - единственная зависимость, которая нужна ViewModel. Если бы потребовались другие классы,
// они также были бы переданы в конструктор.
//добавлена общедоступная LiveDataпеременная-член для кеширования списка слов.
//инициализируется LiveDataс allWordsпотоком из хранилища. Затем вы преобразовали Flow в LiveData, вызвавasLiveData().
//создал insert()метод- оболочку, который вызывает метод репозитория insert (). Таким образом, реализация insert()
// инкапсулируется из пользовательского интерфейса. Мы запускаем новую сопрограмму и вызываем вставку репозитория,
// которая является функцией приостановки. Как уже упоминалось, у ViewModels есть вызываемая область сопрограмм,
// основанная на их жизненном цикле viewModelScope, которую вы здесь будете использовать.
//создано ViewModeland реализовано , ViewModelProvider.Factoryкоторый получает в качестве параметра зависимостей ,
// необходимых для создания WordViewModel: WordRepository.