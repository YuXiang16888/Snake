package com.xiang.snake

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

class SnakeViewModel(application: Application) : AndroidViewModel(application) {
    private val snakeBody = MutableLiveData<List<Position>>()
    val body : LiveData<List<Position>> = snakeBody

    private val snakeApple = MutableLiveData<Position>()
    val apple : LiveData<Position> = snakeApple
    private lateinit var snakeApplePos : Position

    private val score = MutableLiveData(0)
    val gameScore : LiveData<Int> = score
    private var point = 0

    private val snakeBodyList = mutableListOf<Position>()

    var direction : Direction = Direction.LEFT

    private lateinit var gameTimer : Timer

    val gameState = MutableLiveData<GameState>()

    private val backgroundMusic = MediaPlayer.create(application.applicationContext, R.raw.game_music)
    private val eatSoundeffect = MediaPlayer.create(application.applicationContext, R.raw.eat_music)

    fun start() {
        backgroundMusic.start()
        gameState.value = GameState.GAMING
        score.value = point
        snakeBodyList.apply {
            add(Position(10,10))
            add(Position(11,10))
            add(Position(12,10))
            add(Position(13,10))
        }.also {
            snakeBody.value = it
        }
        generateApple()
        gameTimer = fixedRateTimer("GameTimer", true, 500, 500) {
            val pos = snakeBodyList.first().copy().apply {
                when(direction) {
                    Direction.UP -> y--
                    Direction.DOWN -> y++
                    Direction.LEFT -> x--
                    Direction.RIGHT -> x++
                }
                if(snakeBodyList.contains(this) || x < 0 || x >=20 || y < 0 || y >= 20) {
                    cancel()
                    gameState.postValue(GameState.GAMEOVER)
                }
            }
            snakeBodyList.add(0,pos)
            if(pos != snakeApplePos) {
                snakeBodyList.removeLast()
            } else {
                val nowPoint = score.value
                eatSoundeffect.start()
                score.postValue(nowPoint!! + 100)
                generateApple()
            }
            snakeBody.postValue(snakeBodyList)
        }
    }

    fun reset() {
        point = 0
        snakeBodyList.clear()
        snakeApplePos.x = 0
        snakeApplePos.y = 0
        direction = Direction.LEFT
        gameTimer.cancel()
        backgroundMusic.stop()
        backgroundMusic.prepare()
        start()
    }

    fun move(dir : Direction) {
        if((direction == Direction.UP && dir != Direction.DOWN) ||
            (direction == Direction.DOWN && dir != Direction.UP) ||
            (direction == Direction.LEFT && dir != Direction.RIGHT) ||
            (direction == Direction.RIGHT && dir != Direction.LEFT)) {
            direction = dir
        }
    }

    fun generateApple() {
        val allPos = mutableListOf<Position>().apply {
            for(i in 0..19) {
                for(j in 0..19) {
                    add(Position(i,j))
                }
            }
        }
        allPos.removeAll(snakeBodyList)
        allPos.shuffle()
        snakeApplePos = allPos[0]
        snakeApple.postValue(snakeApplePos)
    }
}

data class Position(var x:Int, var y:Int)

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

enum class GameState {
    STOP,
    GAMING,
    GAMEOVER
}