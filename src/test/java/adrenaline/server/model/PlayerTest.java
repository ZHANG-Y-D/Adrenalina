package adrenaline.server.model;

import adrenaline.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import adrenaline.server.network.Client;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private static Player player;

    @BeforeAll
    static void initPlayer(){
        player = new Player(new Avatar("TESTER", Color.YELLOW),"Bob", new ArrayList<>());
    }

    @Test
    void createPlayerTest(){
        Player testPlayer = new Player(new Avatar("TESTER", Color.YELLOW),"Bob", new ArrayList<Client>());
        assertTrue(testPlayer.getDamageTrack().isEmpty());
        assertTrue(testPlayer.getMarks().isEmpty());
        assertTrue(testPlayer.getPowerupCards().isEmpty());
        assertTrue(testPlayer.getWeaponCards().isEmpty());
        int[] ammo = {1,1,1};
        assertArrayEquals(ammo,testPlayer.getAmmoBox());
        assertFalse(testPlayer.isAlive());
        assertEquals(-1,testPlayer.getPosition());
        assertEquals(0,testPlayer.getAdrenalineState());
        assertTrue(testPlayer.isFirstRound());
    }

    @Test
    void weaponCardTest(){
        DeckWeapon deckWeapon = new DeckWeapon();
        WeaponCard weaponCard = deckWeapon.cards.get(0);
        WeaponCard weaponCard2 = deckWeapon.cards.get(1);
        player.addWeaponCard(deckWeapon.cards.get(0));
        assertEquals(1,player.getWeaponHandSize());
        int weaponId = weaponCard2.getWeaponID();
        player.addWeaponCard(weaponCard2);
        assertEquals(weaponCard2,player.getWeaponCard(weaponId));
        assertTrue(player.removeWeaponCard(weaponCard));
        assertFalse(player.removeWeaponCard(weaponCard));
        assertNull(player.getWeaponCard(50));
        player.removeWeaponCard(weaponCard2);
    }

    @Test
    void powerupCardTest(){
        DeckPowerup deckPowerup = new DeckPowerup();
        PowerupCard powerupCard = deckPowerup.cards.get(0);
        PowerupCard powerupCard2 = deckPowerup.cards.get(1);
        player.addPowerupCard(powerupCard);
        assertEquals(1,player.getPowerupHandSize());
        int powerupId = powerupCard2.getPowerupID();
        player.addPowerupCard(powerupCard2);
        assertEquals(powerupCard2, player.getPowerupCard(powerupId));
        assertTrue(player.removePowerupCard(powerupCard));
        assertFalse(player.removePowerupCard(powerupCard));
        player.removePowerupCard(powerupCard2);
        player.addPowerupCard(powerupCard);
        player.consumePowerup(powerupCard);
        player.clearTempAmmo();
        assertEquals(0,player.getPowerupCards().size());
        assertNull(player.getPowerupCard(50));
    }

    @Test
    void positionTest(){
        //Test set new player position
        player.setPosition(3);
        assertEquals(3, player.getPosition());
        player.setPosition(4);
        assertEquals(3,player.getOldPosition());
    }

    @Test
    void ammoBoxTest(){
        //Test add ammo and max ammo
        int[] ammo = {1,2,1};
        int[] finalAmmo = new int[3];
        for(int i = 0; i < 3; i++) finalAmmo[i] = ammo[i] + player.getAmmoBox()[i];
        player.addAmmoBox(ammo);
        assertArrayEquals(finalAmmo, player.getAmmoBox());
        player.addAmmoBox(ammo);
        ammo = new int[]{3,3,3};
        assertArrayEquals(ammo,player.getAmmoBox());

        //Test pay cost
        assertTrue(player.canPayCost(ammo));
        player.payCost(ammo);
        finalAmmo = new int[] {0,0,0};
        assertArrayEquals(finalAmmo,player.getAmmoBox());
        DeckPowerup deckPowerup = new DeckPowerup();
        PowerupCard powerupCard = deckPowerup.cards.get(0);
        player.consumePowerup(powerupCard);
        switch (powerupCard.getColor()){
            case RED: ammo = new int[] {1,0,0}; break;
            case BLUE: ammo = new int[] {0,1,0}; break;
            case YELLOW: ammo = new int[] {0,0,1}; break;
        }
        player.payCost(ammo);
        assertArrayEquals(finalAmmo,player.getAmmoBox());

        //Test add temp ammo
        ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.RED,Color.BLUE,Color.YELLOW));
        ArrayList<PowerupCard> powerupCards = new ArrayList<>();
        while (!colors.isEmpty()){
            PowerupCard pwc = deckPowerup.draw();
            if(colors.contains(pwc.getColor())) {
                powerupCards.add(pwc);
                colors.remove(pwc.getColor());
            }
        }
        for(PowerupCard pw : powerupCards){ player.consumePowerup(pw); }
        ammo = new int[] {1,1,1};
        assertArrayEquals(ammo, player.getTempAmmoBox());
        player.clearTempAmmo();
    }

    @Test
    void applyDamageTest() {

        Color damageOrigin =  Color.RED;
        player.applyDamage(damageOrigin,1, false);
        assertEquals(damageOrigin,player.getDamageTrack().get(0));

        //Test apply damage from marks
        player.addMarks(damageOrigin,2);
        player.applyDamage(damageOrigin,1,false);
        assertEquals(4,player.getDamageTrack().size());
        assertTrue(player.getMarks().isEmpty());

        //Test adrenaline state and death
        assertEquals(1,player.getAdrenalineState());
        player.applyDamage(damageOrigin, 7, false);
        assertFalse(player.isAlive());
        player.clearDamage();
        assertEquals(0,player.getDamageTrack().size());
    }

    @Test
    void addMarkTest() {
        Color markOrigin = Color.WHITE;
        Player markTarget = new Player(new Avatar("TESTER", Color.YELLOW),"Bob", new ArrayList<Client>());
        markTarget.addMarks(markOrigin, 1);
        assertEquals(markOrigin, markTarget.getMarks().get(0));
    }
}
