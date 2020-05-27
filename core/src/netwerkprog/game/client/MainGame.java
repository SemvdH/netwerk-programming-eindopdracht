package netwerkprog.game.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import netwerkprog.game.client.game.characters.Agent;
import netwerkprog.game.client.game.characters.Hacker;
import netwerkprog.game.client.game.characters.abilities.BodySwap;
import netwerkprog.game.client.game.characters.abilities.Implant;
import netwerkprog.game.client.game.characters.abilities.Scrambler;
import netwerkprog.game.client.game.map.Map;
import netwerkprog.game.client.game.map.MapRenderer;
import netwerkprog.game.client.game.map.GameInputProcessor;
import netwerkprog.game.util.game.Character;
import netwerkprog.game.util.graphics.FrameRate;
import netwerkprog.game.util.tree.BST;

public class MainGame extends ApplicationAdapter{
    SpriteBatch batch;
    float screenWidth;
    float screenHeight;
    private FrameRate frameRate;
    private Thread client;
    private OrthographicCamera camera;
    private GameInputProcessor gameInputProcessor;

    private Map map;
    public MapRenderer mapRenderer;

    private BST<Character> tree;
    public Character testCharacter;



    @Override
    public void create() {
        batch = new SpriteBatch();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        frameRate = new FrameRate();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        String[] strings = new String[]{
                "#########################",
                "#xxxx                   #",
                "#   x                   #",
                "#   xxxx                #",
                "#      xx               #",
                "#       x               #",
                "#       x               #",
                "#       x               #",
                "#       x               #",
                "#           xxxxxx      #",
                "#            x          #",
                "#       x      xxxx x x #",
                "#########################"
        };
        map = new Map(strings);
        gameInputProcessor = new GameInputProcessor(camera, this);
        Gdx.input.setInputProcessor(gameInputProcessor);
        mapRenderer = new MapRenderer(map, 32, batch, camera);
        camera.position.set(screenWidth/2,screenHeight/2,0);
        camera.viewportWidth = screenWidth / 2;
        camera.viewportHeight = screenHeight / 2;
        camera.update();
        this.tree = new BST<>();
        initCharaters();
//        this.tree.insert(new Hacker(,new BodySwap()));


//        playSong();


//        connectToServer();
    }

    private void initCharaters() {
        Texture texture = new Texture(Gdx.files.internal("core/assets/characters.png"));
        TextureRegion[][] characters = TextureRegion.split(texture,32,32);
        this.testCharacter = new Hacker(characters[1][0],new BodySwap("test"));
        this.tree.insert(testCharacter);
        this.tree.insert(new Agent(characters[2][0],new Implant("test")));
    }


    private void playSong() {
        // play music
        Music music = Gdx.audio.newMusic(Gdx.files.getFileHandle("core/assets/music.mp3", Files.FileType.Internal));
        music.setVolume(.1f);
        music.play();
        music.setLooping(true);

        connectToServer();
    }


    private void connectToServer() {
        client = new Thread(new Client("localhost"));
        try {
            client.start();
        } catch (Exception e) {
            System.out.println("There was an error connecting : " + e.getMessage());
        }
    }

    /**
     * render method that is called after the update method
     */
    @Override
    public void render() {
        update();
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        frameRate.render();
    }

    /**
     * update method that does all calculation before something is being drawn
     */
    public void update() {
        frameRate.update();
        camera.update();
        this.gameInputProcessor.update();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        screenHeight = height;
        screenWidth = width;
        frameRate.resize(width, height);
        mapRenderer.resize(width, height);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public int getVerticalTileAmount() {
        return map.getHeight();
    }

    public int getHorizontalTileAmount() {
        return map.getWidth();
    }

    public BST<Character> getTree() {
        return tree;
    }

}
