package magicthegathering.impl;

import magicthegathering.game.CreatureCard;
import magicthegathering.game.Card;
import magicthegathering.game.LandCard;
import magicthegathering.game.ManaType;
import magicthegathering.game.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lukas Hajda.
 */

public class PlayerImpl implements Player {

    private final String name;
    private int lives;
    private List<Card> allCards;

    /**
     *
     * @param name Players name.
     */

    public PlayerImpl(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Incorrect name");
        }
        this.name = name;
        this.lives = this.INIT_LIVES;

    }

    /**
     *
     * @return players name.
     */

    @Override
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return players current health.
     */

    @Override
    public int getLife() {
        return this.lives;
    }

    /**
     *
     * @param lives lives
     */

    @Override
    public void subtractLives(int lives) {
        this.lives -= lives;
    }

    /**
     *
     * @return less or equal than 0 is dead, not otherwise
     */

    @Override
    public boolean isDead() {
        return this.lives <= 0;
    }

    /**
     *
     * @param cards to be put into player's hand
     */

    @Override
    public void initCards(List<Card> cards) {
        List<Card> cardsOnH = new ArrayList<>(cards);
        for(Card card : cardsOnH) {
            if (card.isOnTable()) {
                cardsOnH.remove(card);
            }
        }
        this.allCards = new ArrayList<>(cardsOnH);
    }

    /**
     * Logic: What is not in table is in players hand.
     * @return cards in players hand.
     */

    @Override
    public List<Card> getCardsInHand() {
        List<Card> result = inHandFilter();
        return Collections.unmodifiableList(result);
    }

    /**
     *
     * @return Cards on table.
     */

    @Override
    public List<Card> getCardsOnTable() {
        return  Collections.unmodifiableList(this.onTableFilter());
    }

    /**
     * Call one filter method. {@link #landCardFilter()}
     * @return list of land which are on table.
     */

    @Override
    public List<LandCard> getLandsOnTable() {
        List<LandCard> result = new ArrayList<>();
        List<LandCard> allLands = landCardFilter();

        for(LandCard card : allLands) {
            if (card.isOnTable()) result.add(card);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Call one filter method. {@link #creatureCardfilter()}.
     * @return list of creatures which are on table.
     */

    @Override
    public List<CreatureCard> getCreaturesOnTable() {
        List<CreatureCard> result = new ArrayList<>();
        List<CreatureCard> allCreatures = creatureCardfilter();

        for(CreatureCard card : allCreatures) {
            if (card.isOnTable()) result.add(card);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     *
     * @return list of lands which are in players hand.
     */

    @Override
    public List<LandCard> getLandsInHand() {
        List<LandCard> result = new ArrayList<>();

        for(Card card : this.allCards) {
            if(card instanceof LandCard && !card.isOnTable()) result.add((LandCard) card);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Call one filter method. {@link #creatureCardfilter()}.
     * @return list of creatures which are in players hand.
     */

    @Override
    public List<CreatureCard> getCreaturesInHand() {
        List<CreatureCard> list = creatureCardfilter();
        List<CreatureCard> result = new ArrayList<>();

        for(CreatureCard card : list) {
            if (!card.isOnTable()){
                result.add(card);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * That cards which are on table will be untapped.
     */

    @Override
    public void untapAllCards() {
        for(Card card : this.allCards) {
            if(card.isOnTable()) {
                card.untap();
            }
        }
    }

    /**
     * That creature cards which are on table will be able to fight.
     */

    @Override
    public void prepareAllCreatures() {
        List<CreatureCard> creatureCards = creatureCardfilter();

        for(CreatureCard card : creatureCards) {
            if (card.isOnTable()) card.unsetSummoningSickness();
        }
    }

    /**
     *
     * @param landCard land to be put on the table
     * @return true if card is put on table , false otherwise.
     */

    @Override
    public boolean putLandOnTable(LandCard landCard) {
        List<LandCard> cards = landCardFilter();

        for(LandCard card : cards) {
            if (card == landCard){
                if (!landCard.isOnTable()) {
                    landCard.putOnTable();
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     *
     * @param creatureCard creature card to be put on the table
     * @return true if creature is put on table, false otherwise.
     */

    @Override
    public boolean putCreatureOnTable(CreatureCard creatureCard) {
        List<CreatureCard> creatureCards = creatureCardfilter();

        if (!creatureCards.contains(creatureCard) ||
                creatureCard.isOnTable() ||
                !hasManaForCreature(creatureCard)) {
            return false;
        }

        creatureCard.putOnTable();
        tapManaForCreature(creatureCard);
        return true;
    }

    /**
     *
     * @param creature creature to be checked
     * @return if we are able to buy given creature.
     */

    @Override
    public boolean hasManaForCreature(CreatureCard creature) {
        int[] allMana = calculateUntappedLands();
        int len = ManaType.values().length;
        for(int i = 0; i < len; i++) {
            if (creature.getSpecialCost(ManaType.values()[i]) > allMana[i]) return false;
        }

        return true;
    }

    /**
     *
     * @return array of int items which every index represent untapped land card in this form:
     * WHITE, RED, GREEN, BLUE, BLACK
     */

    @Override
    public int[] calculateUntappedLands() {
        int[] result = new int[] {0, 0, 0, 0, 0};
        List<LandCard> landCards = landCardFilter();

        for(LandCard land : landCards) {
            if (land.isOnTable() && !land.isTapped()) {
                switch (land.getManaType()) {
                    case WHITE: result[0] += 1; break;
                    case RED: result[1] += 1; break;
                    case GREEN: result[2] += 1; break;
                    case BLUE: result[3] += 1; break;
                    case BLACK: result[4] += 1; break;
                    default: throw new IllegalArgumentException("Unexpected error");
                }
            }
        }
        return result;
    }

    /**
     *
     * @param creature creature which price needs to be paid
     */

    @Override
    public void tapManaForCreature(CreatureCard creature) {
        List<LandCard> lands = landCardFilter();
        for(ManaType mana : ManaType.values()) {
            for(int i = 0; i < creature.getSpecialCost(mana); i++) {
                for(int j = 0; j < 5; j++) {
                    if (!lands.get(j).isTapped() && lands.get(j).getManaType() == mana) {
                        lands.get(j).tap();
                        break;
                    }
                }
            }
        }

    }

    /**
     *
     * @param creature creature to be removed.
     */

    @Override
    public void destroyCreature(CreatureCard creature) {
        this.allCards.remove(creature);
    }

    /**
     *
     * @return name of player and his/her health.
     */

    @Override
    public String toString() {
        return name + "(" + lives + ")";
    }

    /**
     * Filters cards which are on table.
     * @return List of cards which we want (all cards on table).
     */

    private List<Card> onTableFilter() {
        List<Card> cards = new ArrayList<>();
        for(Card card : this.allCards) {
            if (card.isOnTable()) {
                cards.add(card);
            }
        }
        return Collections.unmodifiableList(cards);
    }

    /**
     *
     * @return list of cards which we want. (all cards in hand).
     */

    private List<Card> inHandFilter() {
        List<Card> result = new ArrayList<>();
        for(Card card : this.allCards) {
            if (!card.isOnTable()) {
                result.add(card);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     *
     * @return list of land card.
     */

    private List<LandCard> landCardFilter() {
        List<LandCard> landCards = new ArrayList<>();
        for(Card lCard : this.allCards) {
            if (lCard instanceof LandCard) landCards.add((LandCard) lCard);
        }
        return Collections.unmodifiableList(landCards);
    }

    /**
     *
     * @return list of creature cards.
     */

    private List<CreatureCard> creatureCardfilter() {
        List<CreatureCard> creatureCards = new ArrayList<>();
        for(Card lCard : this.allCards) {
            if (lCard instanceof CreatureCard) creatureCards.add((CreatureCard) lCard);
        }
        return Collections.unmodifiableList(creatureCards);
    }
}
