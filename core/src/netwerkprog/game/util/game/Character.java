package netwerkprog.game.util.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public abstract class Character implements Comparable<Character> {
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

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public void render(float x, float y) {
        batch.begin();
        batch.draw(this.textureRegion, x, y);
        batch.end();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Character)) return false;
        Character character = (Character) o;
        return override == character.override &&
                Objects.equals(name, character.name) &&
                faction == character.faction &&
                Objects.equals(abilities, character.abilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, faction, abilities, override);
    }

    @Override
    public int compareTo(Character o) {
        return this.name.compareTo(o.name);
    }
}
