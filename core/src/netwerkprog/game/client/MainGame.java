package netwerkprog.game.client;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
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
import netwerkprog.game.client.game.GAMESTATE;
import netwerkprog.game.client.game.characters.Agent;
import netwerkprog.game.client.game.characters.Hacker;
import netwerkprog.game.client.game.characters.Team;
import netwerkprog.game.client.game.characters.abilities.BodySwap;
import netwerkprog.game.client.game.connections.Client;
import netwerkprog.game.client.game.connections.ClientCallback;
import netwerkprog.game.client.game.map.GameInputProcessor;
import netwerkprog.game.client.game.map.GameTile;
import netwerkprog.game.client.game.map.Map;
import netwerkprog.game.client.game.map.MapRenderer;
import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.character.DamageData;
import netwerkprog.game.util.data.character.MoveData;
import netwerkprog.game.util.data.connection.NameData;
import netwerkprog.game.util.data.connection.TeamData;
import netwerkprog.game.util.data.connection.TurnData;
import netwerkprog.game.util.game.Faction;
import netwerkprog.game.util.game.GameCharacter;
import netwerkprog.game.util.graphics.FrameRate;
import netwerkprog.game.util.graphics.TextRenderer;

import java.awt.*;

/**
 * Main game class
 */
public class MainGame extends Game implements ClientCallback {
    private static MainGame INSTANCE;

    /**
     * return the instance of the main game.
     * @return the main game
     */
    public static MainGame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainGame();
        }
        return INSTANCE;
    }

    private GAMESTATE gamestate;
    private Client client;
    private FrameRate frameRate;
    private OrthographicCamera camera;
    private GameInputProcessor gameInputProcessor;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private TextRenderer textRenderer;
    private Map map;
    public MapRenderer mapRenderer; //todo public?
    public AssetManager assets; //todo public?
    private Faction chosenFaction;
    private Team team;
    private Team enemyTeam;
    private GameCharacter selectedCharacter;
    private float screenWidth;
    private float screenHeight;
    private String username;
    private int turn = 0;
    private boolean playersTurn = true;
    private boolean ready = false;
    private boolean enemyReady = false;
    private boolean gameOver = false;

    private MainGame() {
    }

    @Override
    public void create() {
       init();
    }

    public void init() {
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
        connectToServer();
//        playSong();
    }

    /**
     * initialize all characters.
     */
    public void initCharacters() {
        assets.load("core/assets/characters.png", Texture.class);
        assets.finishLoading();

        Texture texture = assets.get("core/assets/characters.png", Texture.class);
        TextureRegion[][] characters = TextureRegion.split(texture, 32, 32);
        this.team = new Team();
        this.enemyTeam = new Team();
        for (int i = 1; i <= 5; i++) {
            GameCharacter temp = new Hacker("hacker" + i, characters[5][0], new BodySwap("test"));
            mapRenderer.getGameTiles()[1][i].visit(temp);

            GameCharacter temp2 = new Agent("Agent" + i, characters[11][0], new BodySwap("Test"));
            int width = mapRenderer.getGameTiles()[0].length;
            mapRenderer.getGameTiles()[3][width - (i + 1)].visit(temp2);

            if (chosenFaction == Faction.HACKER) {
                this.team.addMember(temp);
                this.enemyTeam.addMember(temp2);
            }
            if (chosenFaction == Faction.MEGACORPORATION) {
                this.team.addMember(temp2);
                this.enemyTeam.addMember(temp);
            }
        }

        this.setSelectedCharacter(this.team.get(0));
    }


    private void playSong() {
        // play music
        Music music = Gdx.audio.newMusic(Gdx.files.getFileHandle("core/assets/sound/beat.mp3", Files.FileType.Internal));
        music.setVolume(.1f);
        music.play();
        music.setLooping(true);

    }


    private void connectToServer() {
        client = new Client("localhost", this);
        Thread t = new Thread(client);
        try {
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearRender() {
        clearRender(0, 0, 0, 1);
    }

    private void clearRender(float r, float g, float b, float alpha) {
        Gdx.gl.glClearColor(r / 255f, g / 255f, b / 255f, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    /**
     * render method that is called after the update method
     */
    @Override
    public void render() {
        if (this.gamestate == GAMESTATE.PLAYING) {
            update();
            // clear screen
            clearRender();
            mapRenderer.render();
            frameRate.render();
            renderText();
            renderTurnText();
        } else if (this.gamestate == GAMESTATE.SELECTING_FACTION) {
            clearRender(67, 168, 186, 1);
            String text = username == null ? "Connecting to server..." : "FACTION SELECT\nYou are: " + username + "\nPress 1 for mega corporation, press 2 for hackers";
            renderString(text, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
            if (this.ready && this.enemyReady) {
                if (this.chosenFaction == Faction.HACKER) {
                    chooseHacker();
                } else if (this.chosenFaction == Faction.MEGACORPORATION){
                    chooseMegaCorp();
                }
            }
        } else if (this.gamestate == GAMESTATE.ENDED) {
            clearRender(67, 168, 186, 1);
            String text = "Game ended!\n";
            if (this.enemyTeam.isDead()) {
                text += "Congratulations! You won!";
            } else if (this.team.isDead()) {
                text += "Too bad! You lost!";
            }
            text += "\nPress ESC to exit the game, or ENTER to start a new game";
            renderString(text, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        }

    }

    private void renderText() {
        String text = "FACTION: " + chosenFaction;
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

    private void renderTurnText() {
        String text = playersTurn ? "Your turn, moves left: " + (3 - this.turn) : "Other player's turn";
        layout.setText(font, text);
        textRenderer.render(text, (Gdx.graphics.getWidth() / 2f) - layout.width / 2f, Gdx.graphics.getHeight() - 3);
    }

    /**
     * update method that does all calculation before something is being drawn
     */
    public void update() {

        frameRate.update();
        camera.update();
        this.gameInputProcessor.update();

        if (this.team.isDead() || this.enemyTeam.isDead()) {
            this.setGameOver(true);
        }
        if (this.isGameOver()) {
            this.setGamestate(GAMESTATE.ENDED);
        }

        if (selectedCharacter.isDead()) {
            nextCharacter(selectedCharacter);
        }
        this.team.update(Gdx.graphics.getDeltaTime());
        this.enemyTeam.update(Gdx.graphics.getDeltaTime());
    }

    private void nextCharacter(GameCharacter c) {
        for (GameCharacter character : this.team.getMembers()) {
            if (!character.equals(c)) this.setSelectedCharacter(character);
        }
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
        textRenderer.dispose();
        mapRenderer.dispose();
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

    public void exit() {
        client.disconnect();
        dispose();
        Gdx.app.exit();
    }

    public void setSelectedCharacter(GameCharacter character) {
        if  (!character.isDead()) {
            this.selectedCharacter = character;
            GameTile characterTile = mapRenderer.getTile(character);
            Point pos = mapRenderer.getPos(characterTile);
            mapRenderer.setSurroundedTilesOfCurrentCharacter(pos.x, pos.y);
        }
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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void increaseTurn() {
        this.turn++;
        if (turn == 3) {
            this.turn = 0;
            this.setPlayersTurn(false);
            send(new TurnData());
        }
    }

    public boolean isPlayersTurn() {
        return this.playersTurn;
    }

    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }

    public void send(Data data) {
        this.client.writeData(data);
    }

    @Override
    public void onDataReceived(Data data) {
        if (data instanceof NameData) {
            this.username = ((NameData) data).getName();
        } else if (data instanceof TeamData) {
            // check if it is not our own message
            if (!((TeamData) data).getUsername().equals(this.username)) {
                // if we have not yet chosen a faction, select the opposing faction
                TeamData teamData = (TeamData) data;
                Faction enemyFaction = teamData.getFaction();
                if (this.chosenFaction == null) {
                    if (enemyFaction == Faction.HACKER) {
                        this.chosenFaction = Faction.MEGACORPORATION;
                    } else {
                        this.chosenFaction = Faction.HACKER;
                    }
                    this.enemyReady = true;
                    this.ready = true;
                }
            }
        } else if (data instanceof MoveData) {
            MoveData moveData = (MoveData) data;
            if (!moveData.getUsername().equals(this.username)) {
                GameTile tile = mapRenderer.getGameTile(moveData.getPos());
                GameCharacter character = enemyTeam.get(moveData.getCharacterName());
                mapRenderer.removeCharacterFromTile(character);
                tile.visit(character);
            }
        } else if (data instanceof DamageData) {
            DamageData damageData = (DamageData) data;
            team.get(damageData.getName()).damage(10);
        } else if (data instanceof TurnData) {
            this.playersTurn = !this.playersTurn;
        }

    }

    public void chooseHacker() {
        setChosenFaction(Faction.HACKER);
        send(new TeamData(Faction.MEGACORPORATION, getUsername()));

        initCharacters();
        camera.translate(-400, 0);

        this.playersTurn = true;
        setGamestate(GAMESTATE.PLAYING);
    }

    public void chooseMegaCorp() {
        setChosenFaction(Faction.MEGACORPORATION);
        send(new TeamData(Faction.MEGACORPORATION, getUsername()));
        initCharacters();
        this.playersTurn = false;
        setGamestate(GAMESTATE.PLAYING);
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnemyReady() {
        return enemyReady;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setEnemyReady(boolean enemyReady) {
        this.enemyReady = enemyReady;
    }
}
