import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        Tokens.initializeTokens(); // Reset tokens before each test
        player = new Player("John");
    }

    @Test
    void testInitialValues() {
        assertEquals("John", player.getName());
        assertEquals(1500, player.getMoney());
        assertEquals(0, player.getPosition());
        assertNull(player.getToken());
    }

    @Test
    void testChooseToken() {
        assertTrue(player.chooseToken("Race Car"));
        assertEquals("Race Car", player.getToken());
        assertFalse(player.chooseToken("Race Car")); // Should fail since it's taken
    }

    @Test
    void testAddMoney() {
        player.addMoney(500);
        assertEquals(2000, player.getMoney());
    }

    @Test
    void testSubtractMoney() {
        assertTrue(player.subtractMoney(200));
        assertEquals(1300, player.getMoney());
        assertFalse(player.subtractMoney(2000));
    }

    @Test
    void testBuyProperty() {
        Property property = new Property("Boardwalk", 400, 50);
        player.buyProperty(property);
        assertEquals(1450, player.getMoney());
    }

    @Test
    void testPayRent() {
        Player owner = new Player("Alice");
        owner.addMoney(500);
        player.payRent(owner, 200);
        assertEquals(1300, player.getMoney());
        assertEquals(2200, owner.getMoney());
    }

    @Test
    void testGetBalance() {
        assertEquals(player.getMoney(), player.getBalance());
    }
    @Test
    void testGetTokens() {
        assertEquals("John has $1500 and is on space 0", player.getTokens());
    }

    @Test
    void testGetandsetPosition() {
        //move player to position 5
        player.setPosition(5);
        assertEquals(5, player.getPosition());
        player.setPosition(10);
        assertEquals(10, player.getPosition());
        player.setPosition(24);
        assertEquals(24, player.getPosition());
    }
    @Test
    void testGetandsetMoney() {
        //add 500 to player's money
        player.addMoney(500);
        assertEquals(2000, player.getMoney());
        //subtract 500 from player's money
        player.subtractMoney(500);
        assertEquals(1500, player.getMoney());
    }
    @Test
    void testShouldGoToJail() {
        //player should not go to jail
        assertFalse(player.shouldGoToJail());
    }
    @Test
    void testReceiveRent() {
        //player receives rent of 200
        player.receiveRent(200);
        assertEquals(1700, player.getMoney());
    }
}
