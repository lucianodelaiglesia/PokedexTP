package com.example.pokedextp.android.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedextp.DatabaseDriverFactory
import com.example.pokedextp.PokedexSDK
import com.example.pokedextp.android.ui.adapters.PokedexAdapter
import com.example.pokedextp.android.ui.viewmodel.PokedexViewModel
import com.example.pokedextp.android.ui.viewmodel.PokedexViewModelFactory
import com.example.pokedextp.android.databinding.ActivityMainBinding
import com.example.pokedextp.db.model.PokedexResults
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var pokedexAdapter: PokedexAdapter
    private lateinit var viewModel: PokedexViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var sdk: PokedexSDK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sdk = PokedexSDK(DatabaseDriverFactory(this))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Listen to Retrofit response
        viewModel = ViewModelProvider(this, PokedexViewModelFactory())[PokedexViewModel::class.java]
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.screenState.collect {
                    when (it) {
                        PokedexScreenState.Loading -> showLoading()
                        PokedexScreenState.Error -> {
                            showPokedex(sdk.getPokemon(false))
                            handlerError()
                        }
                        is PokedexScreenState.ShowPokedex -> {
                            sdk.getPokemon(true)
                            showPokedex(it.pokedex.results)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        pokedexAdapter = PokedexAdapter()

        val gridLayoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        with(binding.rvPokedex) {
            this.layoutManager = gridLayoutManager
            this.setHasFixedSize(true)
            this.adapter = pokedexAdapter
        }
    }

    private fun showPokedex(pokedex: List<PokedexResults>) {
        binding.pokedexProgressBar.visibility = View.GONE
        pokedexAdapter.updatePokedex(pokedex)
    }

    private fun handlerError() {
        Toast.makeText(this, "Error buscando la informacion\nCargando memoria caché", Toast.LENGTH_LONG).show()
    }

    private fun showLoading() {
        binding.pokedexProgressBar.visibility = View.VISIBLE
    }
}