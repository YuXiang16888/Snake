package com.xiang.snake

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.xiang.snake.databinding.ContentGameBinding
import com.xiang.snake.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var contentGameBinding: ContentGameBinding
    private lateinit var snakegameViewModel: SnakeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        contentGameBinding = mainBinding.contentLayout

        snakegameViewModel = ViewModelProvider(this)[SnakeViewModel::class.java]
        mainBinding.fab.setOnClickListener {
            snakegameViewModel.reset()
        }
        snakegameViewModel.body.observe(this, Observer {
            contentGameBinding.apply {
                viewGame.snakeBody = it
                viewGame.direction = snakegameViewModel.direction
                viewGame.invalidate()
            }
        })

        snakegameViewModel.apple.observe(this, Observer {
            contentGameBinding.apply {
                viewGame.apple = it
                viewGame.invalidate()
            }
        })

        snakegameViewModel.gameScore.observe(this, Observer {
            contentGameBinding.tvScore.text = it.toString()
        })

        snakegameViewModel.gameState.observe(this, Observer {
            if(it == GameState.GAMEOVER) {
                AlertDialog.Builder(MainActivity@this)
                    .setTitle("Game State")
                    .setMessage("Game Over")
                    .setPositiveButton("OK",null)
                    .show()
            }
        })

        contentGameBinding.imgUpArrow.setOnClickListener {
            snakegameViewModel.move(Direction.UP)
        }
        contentGameBinding.imgDownArrow.setOnClickListener {
            snakegameViewModel.move(Direction.DOWN)
        }
        contentGameBinding.imgLeftArrow.setOnClickListener {
            snakegameViewModel.move(Direction.LEFT)
        }
        contentGameBinding.imgRightArrow.setOnClickListener {
            snakegameViewModel.move(Direction.RIGHT)
        }

        snakegameViewModel.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}