package com.bignerdranch.android.roomwordsample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        wordViewModel.allWords.observe(owner = this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let { reply ->
                val word = Word(reply)
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
// Добавляем наблюдателя к LiveData, возвращаемому getAlphabetizedWords.
// Метод onChanged () срабатывает, когда наблюдаемые данные изменяются и активность
// на переднем плане.


// Обновляем кешированную копию слов в адаптере.
//<!-- android:allowBackup="true" -->
//<!-- android:icon="@mipmap/ic_launcher" -->
//<!-- android:label="@string/app_name" -->
//<!-- android:roundIcon="@mipmap/ic_launcher_round" -->
//<!-- android:supportsRtl="true" -->
//<!-- android:theme="@style/Theme.RoomWordSample"> -->


//<manifest xmlns:android="http://schemas.android.com/apk/res/android"
//package="com.bignerdranch.android.roomwordsample" >
//
//<application
//android:name=".WordsApplication"
//android:allowBackup="true"
//android:icon="@mipmap/ic_launcher"
//android:label="@string/app_name"
//android:roundIcon="@mipmap/ic_launcher_round"
//android:supportsRtl="true"
//android:theme="@style/AppTheme" >
//
//<activity
//android:name=".MainActivity"
//android:label="@string/app_name">
//<intent-filter>
//<action android:name="android.intent.action.MAIN" />
//
//<category android:name="android.intent.category.LAUNCHER" />
//</intent-filter>
//</activity>
//<activity android:name=".NewWordActivity"></activity>
//</application>
//
//</manifest>