package frc.team7170.lib.data.property;

abstract class BaseProperty implements Property {

    private final String name;
    private final PropertyType type;

    BaseProperty(String name, PropertyType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PropertyType getType() {
        return type;
    }
}
