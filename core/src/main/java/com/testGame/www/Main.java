package com.testGame.www;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    // 1. variables for game resources
    private Texture image;
    Texture backgroundTexture;
    Texture heroTexture;
    Music music;
    Music biggerEffect;

    // 2. viewport : the way we see the game world
    Viewport viewport;

    // resources control
    Sprite heroSprite; // Declare a new Sprite variable

    // collision
    Rectangle effect1Rectangle;  // effect1
    Rectangle heroRectangle; // hero
    Rectangle doorRectangle; // door
    Boolean isEffect1Collision;
    boolean doorClicked;
    Boolean isDoorCollision;


    @Override
    public void create() {
        batch = new SpriteBatch(); // frame
        viewport = new FitViewport(1280 , 960); // view

        // resources
        image = new Texture("libgdx.png");
        backgroundTexture = new Texture("map.png");
        heroTexture = new Texture("imposter.png");
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        biggerEffect = Gdx.audio.newMusic(Gdx.files.internal("big.mp3"));

        // control
        heroSprite = new Sprite(heroTexture); // Initialize the sprite based on the texture
        heroSprite.setSize(160, 160); // Define the size of the sprite
        heroSprite.setPosition(160,160);

        // colision
        heroRectangle = new Rectangle();
        effect1Rectangle = new Rectangle();
        effect1Rectangle.set(1060, 400, 200, 20);
        isEffect1Collision = false;
        doorRectangle = new Rectangle();
        doorRectangle.set(720, 720, 80, 80);
        isDoorCollision = false;

        // music
        music.setLooping(true);
        music.setVolume(.5f);
        music.play();

        // door Detect
        doorClicked = false;

    }

    @Override
    public void render() {
        /* For each X second, render the game to animate it */
        input();
        logic();
        draw();

        // UI
        //stage.act(Gdx.graphics.getDeltaTime());
        //stage.draw();

        // Door Check
        if (doorClicked){
            changeMap_draw();
        }
    }
    private void input() {

        float speed = 6f *160;
        float delta = Gdx.graphics.getDeltaTime();
        // move
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            heroSprite.translateX(speed* delta); // Move the bucket right
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            heroSprite.translateX(-speed* delta);
        }else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            heroSprite.translateY(speed* delta);
        }else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            heroSprite.translateY(-speed* delta);
        } else if (isEffect1Collision && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            heroSprite.setSize(heroSprite.getWidth() + 1, heroSprite.getHeight() +1);
            biggerEffect.play();
        }else if(isDoorCollision && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            doorClicked = true;
        }
    }

    private void logic() {
        // Store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // Store the imposter size for brevity
        float heroW = heroSprite.getWidth();
        float heroH = heroSprite.getHeight();

        // Clamp x to values between 0 and worldWidth
        heroSprite.setX(MathUtils.clamp(heroSprite.getX(), 160, worldWidth - heroW - 160));
        heroSprite.setY(MathUtils.clamp(heroSprite.getY(), 160, worldHeight - heroH - 160));

        // Detect collision
        heroRectangle.set(heroSprite.getX(), heroSprite.getY(), heroW, heroH);
        isEffect1Collision = heroRectangle.overlaps(effect1Rectangle);
        isDoorCollision = heroRectangle.overlaps(doorRectangle);
        heroSprite.setColor(isEffect1Collision ||isDoorCollision ? new Color(Color.BLUE) : new Color(Color.RED));
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        // map
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        batch.draw(backgroundTexture, 0, 0, worldW, worldH);

        // character
        // batch.draw(heroTexture, 160, 160, 160, 160); // x,y= position w,h = size   old version
        heroSprite.draw(batch);


        batch.end();
    }

    private void changeMap_draw(){
        ScreenUtils.clear(Color.BLACK);
        // change all settings (ps : we can group variables in a class named by its map)
    }




    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }


    // resize resources when modifying the game window size
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }



}
