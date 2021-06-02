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

    var topTube: Texture? = null
    var bottomTube: Texture? = null
    var gap = 500f
    var maxTubeOffset = 0f
    private val random = Random()
    var tubeVelocity = 4f
    private val numberOfTube = 4
    var tubeX = arrayOfNulls<Float>(numberOfTube)
    var tubeOffset = arrayOfNulls<Float>(numberOfTube)
    var distanceBetweenTubes = 0

    override fun create() {
        batch = SpriteBatch()
        background = Texture("bg.png")

//        birds = arrayOfNulls<Texture>(2)
        birds[0] = Texture("bird.png")
        birds[1] = Texture("bird2.png")
        birdY = Gdx.graphics.height.toFloat() / 2 - birds[0]!!.height / 2

        topTube = Texture("toptube.png")
        bottomTube = Texture("bottomtube.png")
        maxTubeOffset = Gdx.graphics.height / 2 - gap
        distanceBetweenTubes = Gdx.graphics.width * 3/5

        for (i in 0 until numberOfTube) {
        tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.height - gap - 200)
        tubeX[i] = Gdx.graphics.width / 2 - topTube!!.width.toFloat() / 2 + i * distanceBetweenTubes
        }
    }

    override fun render() {

        batch!!.begin()
        batch!!.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        if (gameState != 0) {

            if (Gdx.input.justTouched()) {
                velocity = -30
            }

            for (i in 0 until numberOfTube) {
                if (tubeX[i]!! < - topTube!!.width) {
                    tubeX[i] = tubeX[i]!! + numberOfTube * distanceBetweenTubes

                } else {
                    tubeX[i] = tubeX[i]!! - tubeVelocity
                }

            batch!!.draw(topTube, tubeX[i]!!.toFloat(),
                    Gdx.graphics.height / 2 + gap / 2 + tubeOffset[i]!!)
            batch!!.draw(bottomTube, tubeX[i]!!.toFloat(),
                    Gdx.graphics.height / 2 - gap / 2 - bottomTube!!.height + tubeOffset[i]!!)
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


        batch!!.draw(birds[flapState], Gdx.graphics.width.toFloat() / 2 - birds[flapState]!!.width / 2, birdY)
        batch!!.end()
    }

}

