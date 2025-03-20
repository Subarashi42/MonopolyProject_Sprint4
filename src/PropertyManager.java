import java.util.ArrayList;
import java.util.List;

public class PropertyManager {
    private List<Property> properties;

    public PropertyManager() {
        this.properties = new ArrayList<>();
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void removeProperty(int position) {
        properties.removeIf(property -> property.getPosition() == position);
    }

    public Property getProperty(int position) {
        return properties.stream()
                .filter(property -> property.getPosition() == position)
                .findFirst()
                .orElse(null);
    }

    public void displayProperties() {
        properties.forEach(System.out::println);
    }
}
