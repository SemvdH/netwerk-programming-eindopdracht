package netwerkprog.game.util.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Arrays;
import java.util.HashSet;

public abstract class Character extends Actor {
    protected String name;
    protected Faction faction;
    protected HashSet<Ability> abilities;
    protected TextureRegion[] sprites;
    protected boolean override;
    protected int x;
    protected int y;
    private final int maxSize = 1;


    public Character(String name, Faction faction, Texture spriteSheet, Ability... abilities) {
        this.name = name;
        this.faction = faction;
        this.sprites = new TextureRegion[this.maxSize];
        this.abilities = new HashSet<>(Arrays.asList(abilities));
        this.override = false;
        loadSprites(spriteSheet);
    }

    public void addAbilities(Ability ability) {
        this.abilities.add(ability);
    }

    public void removeAbility(Ability ability) {
        this.abilities.remove(ability);
    }

    public void changeControl() {
        this.override = !this.override;
    }

    private void loadSprites(Texture spriteSheet) {
        int counter = 0;
        TextureRegion[][] temp = TextureRegion.split(spriteSheet,32,32);
        for (TextureRegion[] array : temp) {
            for (TextureRegion sprite : array) {
                this.sprites[counter] = sprite;
                counter++;
                if (counter >= this.maxSize) {
                    break;
                }
            }
        }
    }
}
