package netwerkprog.game.util;

import java.util.Arrays;
import java.util.HashSet;

public abstract class Character {
    protected String name;
    protected Faction faction;
    protected HashSet<Ability> abilities;
    protected boolean override;

    Character(String name, Faction faction, Ability... abilities) {
        this.name = name;
        this.faction = faction;
        this.abilities = new HashSet<>(Arrays.asList(abilities));
        this.override = false;
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
}
