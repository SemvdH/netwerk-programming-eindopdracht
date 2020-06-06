package netwerkprog.game.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import netwerkprog.game.client.game.GAMESTATE;
import netwerkprog.game.client.game.characters.Hacker;
import netwerkprog.game.client.game.characters.Team;
import netwerkprog.game.client.game.characters.abilities.BodySwap;
import netwerkprog.game.client.game.map.Map;
import netwerkprog.game.client.game.map.MapRenderer;
import netwerkprog.game.client.game.map.GameInputProcessor;
import netwerkprog.game.util.game.Faction;
import netwerkprog.game.util.game.GameCharacter;
import netwerkprog.game.util.graphics.FrameRate;
import netwerkprog.game.util.graphics.TextRenderer;

public class MainGame extends ApplicationAdapter {
    SpriteBatch batch;
    float screenWidth;
    float screenHeight;
    private FrameRate frameRate;
    private Thread client;
    private OrthographicCamera camera;
    private GameInputProcessor gameInputProcessor;
    private GameCharacter selectedCharacter;
    private Team team;
    private Team enemyTeam;
    private TextRenderer textRenderer;
    private BitmapFont font;
    private GlyphLayout layout;
    private GAMESTATE gamestate;
    private Faction chosenFaction;


    private Map map;
    public MapRenderer mapRenderer;


    public GameCharacter testCharacter;

    private static MainGame INSTANCE;

    private MainGame() {
    }

    public static MainGame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainGame();
        }
        return INSTANCE;
    }


    @Override
    public void create() {
        batch = new SpriteBatch();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        frameRate = new FrameRate();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        textRenderer = new TextRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();

        String[] strings = new String[]{
                "#########################",
                "#xxxx                   #",
                "#   x                   #",
                "#   xxxx     xxxxx      #",
                "#   xxxx     xxxxx      #",
                "#   xxxx     xx xx      #",
                "#       x    xxxxx      #",
                "#       x    xxxx       #",
                "#       x               #",
                "#           xxxxxx      #",
                "#            x          #",
                "#       x      xxxx x x #",
                "#########################"
        };
        map = new Map(strings);
        gameInputProcessor = new GameInputProcessor(camera);
        Gdx.input.setInputProcessor(gameInputProcessor);
        mapRenderer = new MapRenderer(map, 32, batch, camera);
        camera.position.set(screenWidth / 2, screenHeight / 2, 0);
        camera.viewportWidth = screenWidth / 2;
        camera.viewportHeight = screenHeight / 2;
        camera.update();
        setGamestate(GAMESTATE.SELECTING_FACTION);

        initCharacters();
//        this.tree.insert(new Hacker(,new BodySwap()));


//        playSong();


//        connectToServer();
    }

    private void initCharacters() {
        Texture texture = new Texture(Gdx.files.internal("core/assets/characters.png"));
        TextureRegion[][] characters = TextureRegion.split(texture, 32, 32);
        this.testCharacter = new Hacker("harryyyyyyyyyy", characters[1][0], new BodySwap("test"));
        GameCharacter character2 = new Hacker("test2", characters[2][0], new BodySwap("test"));
        this.setSelectedCharacter(testCharacter);
        mapRenderer.getGameTiles()[1][1].visit(testCharacter);
        mapRenderer.getGameTiles()[1][2].visit(character2);
        this.team = new Team();
        this.team.addMember(this.testCharacter, character2);

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
        if (this.gamestate == GAMESTATE.PLAYING) {
            update();
            // clear screen
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            mapRenderer.render();
            frameRate.render();
            renderText();
        } else if (this.gamestate == GAMESTATE.SELECTING_FACTION) {
            renderString("FACTION SELECT\nPress 1 for mega corporation, press 2 for hackers",Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f);
        }

    }

    private void renderText() {
        String text = "FACION: " + chosenFaction;
        text += "\nSelected character: " + selectedCharacter.getName();
        text += "\nHealth: " + selectedCharacter.getHealth();
        layout.setText(font, text);
        textRenderer.render(text, Gdx.graphics.getWidth() - layout.width - 5, Gdx.graphics.getHeight() - 3);
    }

    private void renderString(String text) {
        layout.setText(font, text);
        textRenderer.render(text, Gdx.graphics.getWidth() - layout.width - 5, Gdx.graphics.getHeight() - 3);
    }

    private void renderString(String text, float x, float y) {
        layout.setText(font, text);
        textRenderer.render(text, x - layout.width/2f, x - layout.height/2f);
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
        textRenderer.resize(width, height);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        batch.dispose();
        textRenderer.dispose();
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

    public void setSelectedCharacter(GameCharacter character) {
        this.selectedCharacter = character;
        System.out.println("selected character set to : " + character);
    }

    public GAMESTATE getGamestate() {
        return gamestate;
    }

    public void setGamestate(GAMESTATE gamestate) {
        this.gamestate = gamestate;
    }

    public Faction getChosenFaction() {
        return chosenFaction;
    }

    public void setChosenFaction(Faction chosenFaction) {
        this.chosenFaction = chosenFaction;
    }

    public GameCharacter getSelectedCharacter() {
        return selectedCharacter;
    }

    public boolean hasCharacterSelected() {
        return selectedCharacter != null;
    }

    public Team getTeam() {
        return team;
    }

}
