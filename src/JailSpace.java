public class JailSpace {
    public void sendToJail(Player player) {
    }

    public String isInJail(Player player) {
        if (player.isInJail()) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public void getOutOfJail(Player player) {
        if (player.isInJail()) {
            player.getOutOfJail();
        }
    }
}
