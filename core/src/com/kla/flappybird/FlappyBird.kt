package com.kla.flappybird

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.util.*

class FlappyBird : ApplicationAdapter() {
    var batch: SpriteBatch? = null
    var background: Texture? = null

    var birds = arrayOfNulls<Texture?>(2)
    var flapState = 0
    var birdY = 0f
    var velocity = 0
    var gameState = 0
    var gravity = 2


    override fun create() {
        batch = SpriteBatch()
        background = Texture("bg.png")

//        birds = arrayOfNulls<Texture>(2)
        birds[0] = Texture("bird.png")
        birds[1] = Texture("bird2.png")
        birdY = Gdx.graphics.height.toFloat() / 2 - birds[0]!!.height / 2

    }

    override fun render() {

        if (gameState != 0) {

            if (Gdx.input.justTouched()) {
                velocity = -30
            }

            if (birdY > 0 || velocity < 0) {
                velocity += gravity
                birdY -= velocity
            }
        } else {
            if (Gdx.input.justTouched()) {
                gameState = 1
            }
        }

        flapState = if (flapState == 0) {
            1
        } else 0

        batch!!.begin()
        batch!!.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch!!.draw(birds[flapState], Gdx.graphics.width.toFloat() / 2 - birds[flapState]!!.width / 2, birdY)
        batch!!.end()
    }
}

