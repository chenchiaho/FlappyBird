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
    var batch: SpriteBatch? = null
    var background: Texture? = null
//    var shapeRenderer: ShapeRenderer? = null

    var birds = arrayOfNulls<Texture?>(2)
    var flapState = 0
    var birdY = 0f
    var velocity = 0
    var birdPeri: Circle? = null
    var score = 0
    var scoringTube = 0
    var font: BitmapFont? = null

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
    var topTubeRect = arrayOfNulls<Rectangle>(numberOfTube)
    var bottomTubeRect = arrayOfNulls<Rectangle>(numberOfTube)


    override fun create() {
        batch = SpriteBatch()
        background = Texture("bg.png")
//        shapeRenderer = ShapeRenderer()
        birdPeri = Circle()
        font = BitmapFont()
        font?.color = Color.WHITE
        font?.data?.scale(10f)

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
        tubeX[i] = Gdx.graphics.width / 2 - topTube!!.width.toFloat() / 2 + Gdx.graphics.width + i * distanceBetweenTubes
        topTubeRect[i] = Rectangle()
        bottomTubeRect[i] = Rectangle()
        }
    }

    override fun render() {

        batch!!.begin()
        batch!!.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        if (gameState != 0) {

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
                if (tubeX[i]!! < - topTube!!.width) {
                    tubeX[i] = tubeX[i]!! + numberOfTube * distanceBetweenTubes
                    tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.height - gap - 200)

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
        font?.draw(batch, score.toString(), 100f, 200f)


        batch!!.end()

        birdPeri!!.set(Gdx.graphics.width / 2f, birdY + birds[flapState]!!.height / 2, birds[flapState]!!.width / 2f)

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

            }
        }

//        shapeRenderer!!.end()
    }

}

