package magicthegathering.impl;

import magicthegathering.game.AbstractCard;
import magicthegathering.game.LandCard;
import magicthegathering.game.LandCardType;
import magicthegathering.game.ManaType;

import java.util.Objects;

/**
 * @author Lukas Hajda.
 */

public class LandCardImpl extends AbstractCard implements LandCard {

    private LandCardType landType;

    /**
     *
     * @param landType landType (one of : WHITE, RED, GREEN, BLUE, BLACK).
     */

    public LandCardImpl(LandCardType landType) {
        if (landType == null) throw new IllegalArgumentException("Land type is null");
        this.landType = landType;
    }

    /**
     *
     * @return hash code.
     */

    @Override
    public int hashCode() {
        return Objects.hash(landType);
    }

    /**
     *
     * @param obj to be checked.
     * @return true if equals , false otherwise.
     */

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        LandCardImpl that = (LandCardImpl) obj;
        return that.landType == this.landType;
    }

    /**
     *
     * @return land type of current land card.
     */

    @Override
    public LandCardType getLandType() {
        return this.landType;
    }

    /**
     * PLAINS, MOUNTAIN, FOREST, ISLAND, SWAMP
     *    |       |         |       |      |
     *    v       v         v       v      v
     * WHITE,    RED,     GREEN,   BLUE, BLACK
     *
     * @return Mana type for specific land type.
     */

    @Override
    public ManaType getManaType() {
        switch (this.landType) {
            case SWAMP: return ManaType.BLACK;
            case ISLAND: return ManaType.BLUE;
            case PLAINS: return ManaType.WHITE;
            case FOREST: return ManaType.GREEN;
            case MOUNTAIN: return ManaType.RED;
            default: throw new IllegalArgumentException("Something went wrong");
        }
    }

    /**
     *
     * @return String to be printed in specific format.
     */

    @Override
    public String toString() {
        return "Land " + this.landType.name().toLowerCase() + ", " + this.getManaType().name();
    }
}
