package shapes;

import exceptions.IncorrectParametersException;

import java.math.BigDecimal;

public class Parallelepiped extends Shape {
    BigDecimal width;
    BigDecimal height;
    BigDecimal depth;

    @Override
    public BigDecimal getVolume() {
        return width.multiply(height).multiply(depth);
    }

    @Override
    public String[] getParametersList() {
        return new String[]{"Width", "Height", "Depth"};
    }

    @Override
    public void load(BigDecimal[] parameters) throws IncorrectParametersException {
        if (parameters.length != 3) {
            throw new IncorrectParametersException();
        }
        width = parameters[0];
        height = parameters[1];
        depth = parameters[2];
    }

    @Override
    public String getTypeName() {
        return "Parallelepiped";
    }
}
