public abstract class Space {
    String name;
    int position;
    String type;
    private Player owner;
    private String colorGroup;

    public Space(String name, int position, String type) {
        this.name = name;
        this.position = position;
        this.type = type;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getColorGroup() {
//        return colorGroup;
//    }
//
//    public void setColorGroup(String colorGroup) {
//        this.colorGroup = colorGroup;
//    }

    // method for landing on a special space (go, jail, etc.)
    public void playerOnSpecialSpace(){
        System.out.println("Player landed on " );
    }

    // method for landing on a property
    // method for landing on a railroad
    // method for landing on a card space

    @Override
    public String toString() {
        return position + ": " + name + " (" + type + ")";
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
