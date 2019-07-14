package magicthegathering.impl;

import magicthegathering.game.CreatureCard;
import magicthegathering.game.Game;
import magicthegathering.game.Generator;
import magicthegathering.game.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lukas Hajda.
 */

public class GameImpl implements Game {

    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player secondPlayer;

    /**
     *
     * @param player1 player1
     * @param player2 player2
     */

    public GameImpl(Player player1, Player player2) {
        if (player1 == null || player2 == null) throw new IllegalArgumentException("Null argument is detected.");

        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.secondPlayer = player2;
    }

    /**
     * Generates cards to every player.
     */

    @Override
    public void initGame() {
        this.player1.initCards(Generator.generateCards());
        this.player2.initCards(Generator.generateCards());
    }

    /**
     * recognize which player is on turn.
     */

    @Override
    public void changePlayer() {
        if (this.player1 == this.currentPlayer) {
            this.currentPlayer = this.player2;
            this.secondPlayer = this.player1;
        } else {
            this.currentPlayer = this.player1;
            this.secondPlayer = this.player2;
        }
    }

    /**
     * Prepare cards:
     * 1) untapp all cards
     * 2) unset summoning sickness of every creature which are on table.
     */

    @Override
    public void prepareCurrentPlayerForTurn() {
        this.currentPlayer.untapAllCards();
        for (CreatureCard card : this.currentPlayer.getCreaturesOnTable()) {
            card.unsetSummoningSickness();
        }
    }

    /**
     *
     * @return current player.
     */

    @Override
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     *
     * @return second player.
     */

    @Override
    public Player getSecondPlayer() {
        return this.secondPlayer;
    }

    /**
     *
     * @param creatures creatures which are going to attack
     */

    @Override
    public void performAttack(List<CreatureCard> creatures) {
        for (CreatureCard card : creatures) {
            if (card == null) continue;
            card.tap();
        }
    }

    /**
     *
     * @param attackingCreatures list of attacking creatures
     * @return true if no rule is break, false otherwise.
     */

    @Override
    public boolean isCreaturesAttackValid(List<CreatureCard> attackingCreatures) {
        // Check current players cards:
        if (!currentPlayer.getCreaturesOnTable().containsAll(attackingCreatures)) {
            return false;
        }
        //------------------------------------------------------------------
        // Check summoning sickness:
        for (CreatureCard card : attackingCreatures) {
            if (card.hasSummoningSickness() || card.isTapped()) return false;
        }
        //------------------------------------------------------------------
        // Check duplicate:
        Set<CreatureCard> creatureCards = new HashSet<>(attackingCreatures);
        return creatureCards.size() == attackingCreatures.size();
    }

    /**
     *
     * @param attackingCreatures list of attacking creatures
     * @param blockingCreatures  list of blocking creatures
     * @return true if no rule is brea, false otherwise.
     */

    @Override
    public boolean isCreaturesBlockValid(List<CreatureCard> attackingCreatures, List<CreatureCard> blockingCreatures) {
        // Check different length:
        if (attackingCreatures.size() != blockingCreatures.size()) return false;
        //---------------------------------------------------------------------
        // Check current/second player:
        if (!currentPlayer.getCreaturesOnTable().containsAll(attackingCreatures)) return false;
        for (CreatureCard card : blockingCreatures) {
            if (card != null) {
                if (!secondPlayer.getCreaturesOnTable().contains(card)) {
                    return false;
                }
            }
        }
        //---------------------------------------------------------------------
        // Check tapped card:
        for (CreatureCard card : blockingCreatures) {
            if (card != null) {
                if (card.isTapped()) return false;
            }
        }
        //---------------------------------------------------------------------
        // Check duplicates:

        List<CreatureCard> attacking = removeAllNull(new ArrayList<>(attackingCreatures));
        List<CreatureCard> blocking = removeAllNull(new ArrayList<>(blockingCreatures));

        return (new HashSet<>(attacking)).size() == attacking.size() &&
                (new HashSet<>(blocking)).size() == blocking.size();
    }

    /**
     *
     * @param deck list where null will be removed.
     * @return list without null items.
     */

    private List<CreatureCard> removeAllNull(List<CreatureCard> deck) {
        while (deck.contains(null)) {
            deck.remove(null);
        }
        return new ArrayList<>(deck);
    }

    /**
     *
     * @param attackingCreatures list of attacking creatures
     * @param blockingCreatures  list of blocking creatures
     */

    @Override
    public void performBlockAndDamage(List<CreatureCard> attackingCreatures, List<CreatureCard> blockingCreatures) {
        int len = attackingCreatures.size();
        for(int i = 0; i < len; i++) {
            CreatureCard attackingCreature = attackingCreatures.get(i);
            if(blockingCreatures.get(i) == null) {
                this.secondPlayer.subtractLives(attackingCreature.getPower());
                continue;
            }
            CreatureCard blockingCreature = blockingCreatures.get(i);
            int defenderLive = blockingCreature.getToughness() - attackingCreature.getPower();
            int attackerLive = attackingCreature.getToughness() - blockingCreature.getPower();

            if (defenderLive <= 0 && attackerLive > 0) {
                this.secondPlayer.destroyCreature(blockingCreature);
                continue;
            }
            if (defenderLive > 0 && attackerLive <= 0) {
                this.currentPlayer.destroyCreature(attackingCreature);
                continue;
            }
            if (defenderLive <= 0 && attackerLive <= 0) {
                this.currentPlayer.destroyCreature(attackingCreature);
                this.secondPlayer.destroyCreature(blockingCreature);
            }

        }
    }
}
