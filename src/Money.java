public class Money {
    public void transfer(Player player1, Player player2, int i) {
        player1.decreaseBalance(i);
        player2.increaseBalance(i);
    }
}
