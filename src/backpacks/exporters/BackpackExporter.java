package backpacks.exporters;

import exceptions.OpenFileException;
import shapes.*;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

public abstract class BackpackExporter {
    public void export(String filePath) throws OpenFileException {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(getSaveData());
            file.flush();
        } catch (IOException e) {
            throw new OpenFileException();
        }
    }

    public abstract void beginSavingBackpack(BigDecimal backpackCapacity);
    public abstract void addShape(Shape shape);

    protected abstract String getSaveData();
}
