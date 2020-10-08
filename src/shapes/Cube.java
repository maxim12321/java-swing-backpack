package shapes;

import exceptions.IncorrectParametersException;

import java.math.BigDecimal;

public class Cube extends Shape {
    BigDecimal side;

    @Override
    public BigDecimal getVolume() {
        return side.pow(3);
    }

    @Override
    public String[] getParametersList() {
        return new String[]{"Side"};
    }

    @Override
    public void load(BigDecimal[] parameters) throws IncorrectParametersException {
        if (parameters.length != 1) {
            throw new IncorrectParametersException();
        }
        side = parameters[0];
    }

    @Override
    public String getTypeName() {
        return "Cube";
    }
}
