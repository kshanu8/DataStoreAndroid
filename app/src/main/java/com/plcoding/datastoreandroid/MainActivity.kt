package com.plcoding.datastoreandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
//import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.plcoding.datastoreandroid.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val API_KEY = BuildConfig.APIKEY;

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //private lateinit var dataStore: DataStore<Preferences>
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //dataStore = createDataStore(name = "settings")

        Log.e("Testing","3")
        Log.e("Testing","4")

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(
                        binding.etSaveKey.text.toString(),
                        binding.etSaveValue.text.toString()
                )
            }
        }

        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(binding.etReadkey.text.toString())
                binding.tvReadValue.text = value ?: "No value found"
            }
        }
    }

    private suspend fun save(key: String, value: String) {
        dataStore.edit { settings ->
            val dataStoreKey = stringPreferencesKey(key)
            settings[dataStoreKey] = value
        }
        /*val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }*/
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]

       /* val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
