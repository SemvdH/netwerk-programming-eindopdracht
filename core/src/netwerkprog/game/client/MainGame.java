package netwerkprog.game.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import netwerkprog.game.client.game.GAMESTATE;
import netwerkprog.game.client.game.characters.Agent;
import netwerkprog.game.client.game.characters.Hacker;
import netwerkprog.game.client.game.characters.Team;
import netwerkprog.game.client.game.characters.abilities.BodySwap;
import netwerkprog.game.client.game.connections.Client;
import netwerkprog.game.client.game.map.GameInputProcessor;
import netwerkprog.game.client.game.map.GameTile;
import netwerkprog.game.client.game.map.Map;
import netwerkprog.game.client.game.map.MapRenderer;
import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.DataCallback;
import netwerkprog.game.util.game.Faction;
import netwerkprog.game.util.game.GameCharacter;
import netwerkprog.game.util.graphics.FrameRate;
import netwerkprog.game.util.graphics.TextRenderer;

import java.awt.*;

public class MainGame extends ApplicationAdapter implements DataCallback {
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
    private long lastTimeCounted = 0;

    private Map map;
    public MapRenderer mapRenderer;
    public AssetManager assets;

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
        assets = new AssetManager();

        String[] strings = new String[]{
                "#########################",
                "#xxxx        #          #",
                "#   x        #          #",
                "#   xxxx     #xxxx      #",
                "#   xxxx     #xxxx      #",
                "#   xxxx     #x xx      #",
                "#       x    #xxxx      #",
                "#       x    #xxx       #",
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
//        this.tree.insert(new Hacker(,new BodySwap()));


//        playSong();


        connectToServer();
    }

    public void initCharacters() {
        assets.load("core/assets/characters.png", Texture.class);
        assets.finishLoading();
        Texture texture = assets.get("core/assets/characters.png");
        TextureRegion[][] characters = TextureRegion.split(texture, 32, 32);
        this.team = new Team();
        this.enemyTeam = new Team();

        System.out.println(this.chosenFaction);
        for (int i = 1; i <= 5; i++) {
            GameCharacter temp = new Hacker("hacker" + i, characters[5][0], new BodySwap("test"));
            mapRenderer.getGameTiles()[1][i].visit(temp);

            GameCharacter temp2 = new Agent("Agent" + i, characters[11][0], new BodySwap("Test"));
            int width = mapRenderer.getGameTiles()[0].length;
            mapRenderer.getGameTiles()[3][width - (i + 1)].visit(temp2);

            if (chosenFaction == Faction.HACKER) {
                System.out.println("adding " + temp);
                this.team.addMember(temp);
                this.enemyTeam.addMember(temp2);
            }  if (chosenFaction == Faction.MEGACORPORATION) {
                System.out.println("adding " + temp2);
                this.team.addMember(temp2);
                this.enemyTeam.addMember(temp);
            }
        }

        System.out.println(this.team);
        System.out.println(this.enemyTeam);
        this.setSelectedCharacter(this.team.get(0));

    }


    private void playSong() {
        // play music
        Music music = Gdx.audio.newMusic(Gdx.files.getFileHandle("core/assets/earrape.mp3", Files.FileType.Internal));
        music.setVolume(.1f);
        music.play();
        music.setLooping(true);

    }


    private void connectToServer() {
        client = new Thread(new Client("localhost", this));
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
            renderString("FACTION SELECT\nPress 1 for mega corporation, press 2 for hackers", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
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
        textRenderer.render(text, x - layout.width / 2f, x - layout.height / 2f);
    }

    /**
     * update method that does all calculation before something is being drawn
     */
    public void update() {

        frameRate.update();
        camera.update();
        this.gameInputProcessor.update();
        this.team.update(Gdx.graphics.getDeltaTime());
        this.enemyTeam.update(Gdx.graphics.getDeltaTime());
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
        assets.dispose();
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
        GameTile characterTile = mapRenderer.getTile(character);
        Point pos = mapRenderer.getPos(characterTile);
        mapRenderer.setSurroundedTilesOfCurrentCharacter(pos.x, pos.y);
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

    @Override
    public void onDataReceived(Data data) {
        
    }
}
