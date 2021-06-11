package com.kla.flappybird

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import java.util.*


class FlappyBird : ApplicationAdapter() {
    private var batch: SpriteBatch? = null
    private var background: Texture? = null
    private var gameover: Texture? = null
    private var gameState = 0
    private val random = Random()

    private var birds = arrayOfNulls<Texture?>(2)
    private var birdPeri: Circle? = null
    private var birdY = 0f
    private var flapState = 0

    private var score = 0
    private var scoringTube = 0
    private var font: BitmapFont? = null

    private var velocity = 0
    private var gravity = 2

    private var topTube: Texture? = null
    private var bottomTube: Texture? = null

    private val numberOfTube = 4
    private var tubeVelocity = 4f
    private var distanceBetweenTubes = 0
    private var gap = 550f
    private var maxTubeOffset = 0f
    private var tubeX = arrayOfNulls<Float>(numberOfTube)
    private var tubeOffset = arrayOfNulls<Float>(numberOfTube)
    private var topTubeRect = arrayOfNulls<Rectangle>(numberOfTube)
    private var bottomTubeRect = arrayOfNulls<Rectangle>(numberOfTube)

    override fun create() {
        batch = SpriteBatch()
        background = Texture("bg.png")
        gameover = Texture("gameover.png")

        birds[0] = Texture("bird.png")
        birds[1] = Texture("bird2.png")
        birdPeri = Circle()

        topTube = Texture("toptube.png")
        bottomTube = Texture("bottomtube.png")
        maxTubeOffset = Gdx.graphics.height / 2 - gap - 220
        distanceBetweenTubes = Gdx.graphics.width * 4 / 6

        font = BitmapFont()
        font!!.color = Color.WHITE
        font!!.data?.scale(10f)

        startGame()
    }

    fun startGame() {
        birdY = Gdx.graphics.height.toFloat() / 2 - birds[0]!!.height / 2

        for (i in 0 until numberOfTube) {
            tubeOffset[i] = (random.nextFloat() - 0.5f) * maxTubeOffset
            tubeX[i] = Gdx.graphics.width / 2 - topTube!!.width.toFloat() / 2 + Gdx.graphics.width + i * distanceBetweenTubes
            topTubeRect[i] = Rectangle()
            bottomTubeRect[i] = Rectangle()
        }

    }

    override fun render() {

        batch!!.begin()
        batch!!.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        if (gameState == 1) {

            if (tubeX[scoringTube]!! < Gdx.graphics.width / 2) {
                score++

                if (scoringTube < numberOfTube - 1) {
                    scoringTube++
                } else {
                    scoringTube = 0
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = -30
            }

            for (i in 0 until numberOfTube) {
                if (tubeX[i]!! < -topTube!!.width) {
                    tubeX[i] = tubeX[i]!! + numberOfTube * distanceBetweenTubes
                    tubeOffset[i] = (random.nextFloat() - 0.5f) * maxTubeOffset

                } else {
                    tubeX[i] = tubeX[i]!! - tubeVelocity
                }

                batch!!.draw(topTube, tubeX[i]!!.toFloat(),
                        Gdx.graphics.height / 2 + gap / 2 + tubeOffset[i]!!)
                batch!!.draw(bottomTube, tubeX[i]!!.toFloat(),
                        Gdx.graphics.height / 2 - gap / 2 - bottomTube!!.height + tubeOffset[i]!!)

                topTubeRect[i] = Rectangle(tubeX[i]!!, Gdx.graphics.height / 2 + gap / 2 + tubeOffset[i]!!, topTube!!.width.toFloat(), topTube!!.height.toFloat())
                bottomTubeRect[i] = Rectangle(tubeX[i]!!,
                        Gdx.graphics.height / 2 - gap / 2 - bottomTube!!.height + tubeOffset[i]!!,
                        bottomTube!!.width.toFloat(), bottomTube!!.height.toFloat())
            }

            if (birdY > 0) {
                velocity += gravity
                birdY -= velocity
            } else {
                gameState = 2
            }

        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1
            }
        } else if (gameState == 2) {
            batch!!.draw(gameover, Gdx.graphics.width / 2f - gameover!!.width / 2f,
                    Gdx.graphics.height / 2f - gameover!!.height / 2f)
            if (Gdx.input.justTouched()) {
                gameState = 1
                startGame()
                score = 0
                scoringTube = 0
                velocity = 0
            }
        }

            flapState = if (flapState == 0) {
                1
            } else 0


            batch!!.draw(birds[flapState], Gdx.graphics.width.toFloat() / 2 - birds[flapState]!!.width / 2, birdY)
            birdPeri!!.set(Gdx.graphics.width / 2f, birdY + birds[flapState]!!.height / 2, birds[flapState]!!.width / 2f)
        font?.draw(batch, score.toString(), 100f, 200f)

//        shapeRenderer!!.begin(ShapeRenderer.ShapeType.Filled)
//        shapeRenderer!!.setColor(Color.RED)
//        shapeRenderer!!.circle(birdPeri!!.x, birdPeri!!.y, birdPeri!!.radius)

            for (i in 0 until numberOfTube) {
//            shapeRenderer!!.rect(tubeX[i]!!, Gdx.graphics.height / 2 + gap / 2 + tubeOffset[i]!!
//                    , topTube!!.width.toFloat(), topTube!!.height.toFloat())
//            shapeRenderer!!.rect(tubeX[i]!!,
//                    Gdx.graphics.height / 2 - gap / 2 - bottomTube!!.height + tubeOffset[i]!!,
//                    bottomTube!!.width.toFloat(), bottomTube!!.height.toFloat())

                if (Intersector.overlaps(birdPeri, topTubeRect[i]) || Intersector.overlaps(birdPeri, bottomTubeRect[i])) {
                    gameState = 2
                }
            }
            batch!!.end()
//        shapeRenderer!!.end()
    }
}

