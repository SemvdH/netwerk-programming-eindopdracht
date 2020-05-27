package netwerkprog.game.util.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Arrays;
import java.util.HashSet;

import java.util.Objects;

public abstract class GameCharacter extends Actor implements Comparable<GameCharacter> {
    protected String name;
    protected Faction faction;
    protected HashSet<Ability> abilities;
    protected boolean override;
    protected TextureRegion textureRegion;

    public GameCharacter(String name, Faction faction, TextureRegion textureRegion, Ability... abilities) {
        this.name = name;
        this.faction = faction;
        this.abilities = new HashSet<>(Arrays.asList(abilities));
        this.override = false;
        this.textureRegion = textureRegion;
    }

    public String getName() {
        return this.name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof GameCharacter)) return false;
        GameCharacter character = (GameCharacter) o;
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
    public int compareTo(GameCharacter o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "GameCharacter{" +
                "name='" + name + '\'' +
                ", faction=" + faction +
                '}';
    }
}
