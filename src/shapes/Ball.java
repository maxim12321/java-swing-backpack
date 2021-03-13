package shapes;

import exceptions.IncorrectParametersException;

import java.math.BigDecimal;

public class Ball extends Shape {
    BigDecimal radius;

    @Override
    public BigDecimal getVolume() {
        return radius.pow(3).multiply(BigDecimal.valueOf(4. / 3. * Math.PI));
    }

    @Override
    public String[] getParametersList() {
        return new String[]{"Radius"};
    }

    @Override
    public BigDecimal[] getParameters() {
        return new BigDecimal[]{radius};
    }

    @Override
    public void load(BigDecimal[] parameters) throws IncorrectParametersException {
        if (parameters.length != 1) {
            throw new IncorrectParametersException();
        }
        radius = parameters[0];
    }

    @Override
    public String getTypeName() {
        return "Ball";
    }
}
