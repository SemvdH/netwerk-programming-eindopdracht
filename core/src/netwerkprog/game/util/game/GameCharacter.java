package netwerkprog.game.util.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import netwerkprog.game.client.game.map.GameTile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public abstract class GameCharacter extends Actor implements Comparable<GameCharacter>, Serializable {
    protected String name;
    protected Faction faction;
    protected HashSet<Ability> abilities;
    protected boolean override;
    protected TextureRegion textureRegion;
    protected int health;
    protected List<GameTile> allowedToMove;
    protected boolean damageAnimation;
    protected double hitTimout = 0;

    public GameCharacter(String name, Faction faction, TextureRegion textureRegion, Ability... abilities) {
        super();
        this.name = name;
        this.faction = faction;
        this.abilities = new HashSet<>(Arrays.asList(abilities));
        this.override = false;
        this.textureRegion = textureRegion;
        this.health = 100;
        this.damageAnimation = false;
        this.allowedToMove = new ArrayList<>();
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > 100) this.health = 10;
    }
    public int getDamageAmount() {
        return 10;
    }

    public void damage(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }

        this.damageAnimation = true;

        System.out.println("OUCH character " + name + " was damaged for " + amount + ", animation: " + this.isShowingAnimation());
    }

    public boolean isDead() {
        return this.health <= 0;
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

    public void update(double deltaTime) {
        if (this.damageAnimation) {
            this.hitTimout += deltaTime;
        }
        if (this.hitTimout >= 0.4) {
            this.damageAnimation = false;
            this.hitTimout = 0;
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(name, faction, abilities, override);
    }

    @Override
    public int compareTo(GameCharacter o) {
        return (this.health - o.health) + this.name.compareTo(o.name) + this.faction.compareTo(o.faction);
    }

    @Override
    public String toString() {
        return "GameCharacter{" +
                "name='" + name + '\'' +
                ", faction=" + faction +
                ", x=" + super.getX() +
                ", y=" + super.getY() +
                '}';
    }

    public List<GameTile> getAllowedToMove() {
        return allowedToMove;
    }

    public void setAllowedToMove(List<GameTile> allowedToMove) {
        this.allowedToMove = allowedToMove;
    }

    public Faction getFaction() {
        return faction;
    }

    public boolean isShowingAnimation() {
        return this.damageAnimation;
    }

    public void setShowingDamageAnimation(boolean damageAnimation) {
        this.damageAnimation = damageAnimation;
    }
}
