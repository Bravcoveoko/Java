package magicthegathering.impl;

import magicthegathering.game.AbstractCard;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.ManaType;

import java.util.List;
import java.util.Objects;

/**
 * @author Lukas Hajda
 */

public class CreatureCardImpl extends AbstractCard implements CreatureCard {

    private String name;
    private List<ManaType> cost;
    private int power;
    private int toughness;
    private boolean sickness = true;

    /**
     * @param name      name of creature.
     * @param cost      how much will current creature costs.
     * @param power     power of current creature.
     * @param toughness toughness of current creature.
     */

    public CreatureCardImpl(String name, List<ManaType> cost, int power, int toughness) {
        if (name != null &&
                cost != null &&
                name.length() != 0 &&
                power >= 0 &&
                toughness > 0) {
            this.name = name;
            this.cost = cost;
            this.power = power;
            this.toughness = toughness;
        } else {
            throw new IllegalArgumentException("Invalid inputs");
        }
    }

    /**
     * @return hash code.
     */

    @Override
    public int hashCode() {
        return Objects.hash(name, cost, power, toughness);
    }

    /**
     * @param obj object to be checked.
     * @return true if equals, false otherwise.
     */

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        CreatureCardImpl that = (CreatureCardImpl) obj;
        return that.toughness == this.toughness &&
                that.power == this.power &&
                this.cost.equals(that.cost) &&
                this.name.equals(that.name);
    }

    /**
     * @return length of cost list.
     */

    @Override
    public int getTotalCost() {
        return this.cost.size();
    }

    /**
     * @param mana mana how many given mana are.
     * @return amount of given mana type.
     */

    @Override
    public int getSpecialCost(ManaType mana) {
        int amount = 0;
        for (ManaType manaT : cost) {
            if (manaT == mana) {
                amount += 1;
            }
        }
        return amount;
    }

    /**
     * @return name of current creature.
     */

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return power of current creature.
     */

    @Override
    public int getPower() {
        return this.power;
    }

    /**
     * @return toughness of current creature.
     */

    @Override
    public int getToughness() {
        return this.toughness;
    }

    /**
     * Summoning sicknes = creature can not attack.
     *
     * @return able to fight.
     */

    @Override
    public boolean hasSummoningSickness() {
        return this.sickness;
    }

    /**
     * At the beginning every new creature has summoning sickness.
     * Set summoning sickness.
     */

    @Override
    public void setSummoningSickness() {
        this.sickness = true;
    }

    /**
     * Unset summoing sicknes = Able to fight.
     */

    @Override
    public void unsetSummoningSickness() {
        this.sickness = false;
    }

    /**
     * @return String to be printed in specific format.
     */

    @Override
    public String toString() {
        String canAttack = "can attack ";
        String tapped = "TAPPED ";
        String result = name + " " + cost + " " + power + " / " + toughness + " ";
        if (!this.hasSummoningSickness()) result += canAttack;
        if (this.isTapped()) result += tapped;
        return result.trim();
    }
}
