public abstract class Space {
    String name;
    int position;
    String type;
    private Player owner;
    private String colorGroup;

    public Space(String name, int position, String property) {
        this.name = this.name;
        this.position = this.position;
        this.type = type;
    }

    public String getType() {
        return type;
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
   public String getColorGroup() {
        return colorGroup;
    }

    // method for landing on a special space (go, jail, etc.)
    public void playerOnSpecialSpace(){
        System.out.println("Player landed on "+ name );
    }

    // method for landing on a property
    public void playerOnProperty(){
        System.out.println("Player landed on property " + name);
    }
    // method for landing on a railroad
    public void playerOnRailroad(){
        System.out.println("Player landed on railroad " + name);
    }
    // method for landing on a card space
    public void playerOnCardSpace(){
        System.out.println("Player landed on card space " + name);
    }

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
