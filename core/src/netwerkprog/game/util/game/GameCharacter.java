package netwerkprog.game.util.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Arrays;
import java.util.HashSet;

public abstract class GameCharacter extends Actor {
    protected String name;
    protected Faction faction;
    protected HashSet<Ability> abilities;
    protected TextureRegion[][] sprites;
    protected boolean override;

    public GameCharacter(String name, Faction faction, String spriteSheet, Ability... abilities) {
        this.name = name;
        this.faction = faction;
        this.abilities = new HashSet<>(Arrays.asList(abilities));
        this.override = false;
        this.sprites = TextureRegion.split(new Texture(spriteSheet),32,32);
        super.setX(0);
        super.setY(0);
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

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(this.sprites[0][0],super.getX(),super.getY());
        batch.end();
    }

}
