package netwerkprog.game.util.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.HashSet;

public abstract class Character {
    protected String name;
    protected Faction faction;
    protected HashSet<Ability> abilities;
    protected boolean override;
    protected TextureRegion textureRegion;
    protected SpriteBatch batch;

    public Character(String name, Faction faction, TextureRegion textureRegion, Ability... abilities) {
        this.name = name;
        this.faction = faction;
        this.abilities = new HashSet<>(Arrays.asList(abilities));
        this.override = false;
        this.textureRegion = textureRegion;
        batch = new SpriteBatch();
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

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void render(float x, float y) {
        batch.begin();
        batch.draw(this.textureRegion, x, y);
        batch.end();
    }
}
