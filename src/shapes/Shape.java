package shapes;

import java.math.BigDecimal;

public abstract class Shape {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract String[] getParametersList();
    public abstract void load(BigDecimal[] parameters);

    public abstract BigDecimal getVolume();
    public abstract String getTypeName();
}
