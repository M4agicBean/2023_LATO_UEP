package pl.psi;

import java.util.List;
import java.util.Optional;

import pl.psi.WarMachines.WarMachineStatistic;
import org.junit.jupiter.api.Test;


import pl.psi.creatures.CastleCreatureFactory;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatistic;
import pl.psi.warmachines.WarMachine;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class GameEngineTest
{
    @Test
    void shoudWorksHeHe()
    {
        final CastleCreatureFactory creatureFactory = new CastleCreatureFactory();
        final GameEngine gameEngine =
            new GameEngine(new Hero( List.of( creatureFactory.create( 1, false, 5 )), List.of(new WarMachine.Builder().statistic(WarMachineStatistic.CATAPULT).amount(1).build())),
                    new Hero(List.of( creatureFactory.create( 1, false, 5 )), List.of(new WarMachine.Builder().statistic(WarMachineStatistic.BALLISTA).amount(1).build())));

        gameEngine.attack( new Point( 1, 1 ) );
    }

    @Test
    void attack() throws Exception {
        WarMachine ballista = new WarMachine.Builder().statistic(WarMachineStatistic.BALLISTA).build();
        WarMachine catapult = new WarMachine.Builder().statistic(WarMachineStatistic.CATAPULT).build();
        assertEquals(250, ballista.getCurrentHp());
        assertEquals(1000, catapult.getCurrentHp());
        ballista.attack(catapult);
        assertTrue(catapult.getCurrentHp() < 1000);
    }

    @Test
    void heal() throws Exception {
        WarMachine firstAidTent = new WarMachine.Builder().statistic(WarMachineStatistic.FIRST_AID_TENT).build();
        WarMachine catapult = new WarMachine.Builder().statistic(WarMachineStatistic.CATAPULT).build();
        WarMachine ballista = new WarMachine.Builder().statistic(WarMachineStatistic.BALLISTA).build();
        assertEquals(250, ballista.getCurrentHp());
        catapult.attack(ballista);
        catapult.attack(ballista);
        assertTrue(ballista.getCurrentHp() < 250);
        firstAidTent.heal(ballista);
        firstAidTent.heal(ballista);
        assertEquals(250, ballista.getCurrentHp());
    }

    @Test
    void canAttack() {
        WarMachine ballista = new WarMachine.Builder().statistic(WarMachineStatistic.BALLISTA).build();
        Creature skeleton = new Creature.Builder().statistic(CreatureStatistic.SKELETON).build();
        WarMachine catapult = new WarMachine.Builder().statistic(WarMachineStatistic.CATAPULT).build();

        //currentMapObject will be taken from queue created by this game engine instance
        //currentMapObject will perform attack
        GameEngine gameEngine = new GameEngine(new Hero(List.of(), List.of(catapult)),
                new Hero(List.of(skeleton), List.of(ballista)));
        // (3,1) is default attacker position in game engine
        assertTrue(gameEngine.isCurrentMapObject(new Point(3,1)));
        assertEquals(Optional.of(catapult), gameEngine.getMapObject(new Point(3,1)));
        assertEquals(Optional.of(skeleton), gameEngine.getMapObject(new Point(11,1)));
        assertEquals(Optional.of(ballista), gameEngine.getMapObject(new Point(11,3)));
        assertTrue(gameEngine.canAttack(new Point(11,3)));
        assertFalse(gameEngine.canAttack(new Point(3,1)));
        assertTrue(gameEngine.canAttack(new Point(11,1)));
        assertFalse(gameEngine.canAttack(new Point(3,3)));

        gameEngine.pass();
        assertTrue(gameEngine.isCurrentMapObject(new Point(11,1)));
        assertFalse(gameEngine.canAttack(new Point(11,1)));
        assertFalse(gameEngine.canAttack(new Point(11,3)));
        assertFalse(gameEngine.canAttack(new Point(3,1)));

        gameEngine.pass();
        assertTrue(gameEngine.isCurrentMapObject(new Point(11,3)));
        assertTrue(gameEngine.canAttack(new Point(3, 1)));
        assertFalse(gameEngine.canAttack(new Point(11, 1)));
        assertFalse(gameEngine.canAttack(new Point(11, 3)));
    }


    @Test
    void canHeal() {
        Creature zombie = new Creature.Builder().statistic(CreatureStatistic.ZOMBIE).build();
        WarMachine firstAidTent = new WarMachine.Builder().statistic(WarMachineStatistic.FIRST_AID_TENT).build();
        WarMachine ballista = new WarMachine.Builder().statistic(WarMachineStatistic.BALLISTA).build();
        Creature skeleton = new Creature.Builder().statistic(CreatureStatistic.SKELETON).build();
        WarMachine catapult = new WarMachine.Builder().statistic(WarMachineStatistic.CATAPULT).build();

        GameEngine gameEngine = new GameEngine(new Hero(List.of(zombie), List.of(firstAidTent, ballista)),
                new Hero(List.of(skeleton), List.of(catapult)));
        assertEquals(Optional.of(zombie), gameEngine.getMapObject(new Point(3,1)));
        assertEquals(Optional.of(firstAidTent), gameEngine.getMapObject(new Point(3,3)));
        assertEquals(Optional.of(ballista), gameEngine.getMapObject(new Point(3,5)));
        assertEquals(Optional.of(skeleton), gameEngine.getMapObject(new Point(11,1)));
        assertEquals(Optional.of(catapult), gameEngine.getMapObject(new Point(11,3)));
        assertTrue(gameEngine.isCurrentMapObject(new Point(3,1)));
        assertFalse(gameEngine.canHeal(new Point(3,1)));

        gameEngine.pass();
        assertTrue(gameEngine.isCurrentMapObject(new Point(3,3)));
        assertTrue(gameEngine.canHeal(new Point(3,1)));
        assertFalse(gameEngine.canHeal(new Point(3,3)));
        assertTrue(gameEngine.canHeal(new Point(3,5)));
        assertFalse(gameEngine.canHeal(new Point(11,1)));
        assertFalse(gameEngine.canHeal(new Point(11,3)));
    }
}
