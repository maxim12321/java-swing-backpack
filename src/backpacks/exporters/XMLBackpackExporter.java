package backpacks.exporters;

import com.sun.corba.se.spi.ior.IdentifiableContainerBase;
import org.w3c.dom.*;
import shapes.Shape;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.math.BigDecimal;

public class XMLBackpackExporter extends BackpackExporter {
    private Document document;
    private Element backpackElement;
    private Element shapesElement;

    @Override
    public void beginSavingBackpack(BigDecimal backpackCapacity) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        document = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            backpackElement = document.createElement("backpack");

            Element capacityElement = document.createElement("capacity");
            capacityElement.appendChild(document.createTextNode(backpackCapacity.toString()));
            backpackElement.appendChild(capacityElement);

            shapesElement = document.createElement("shapes");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addShape(Shape shape) {
        if (document == null) {
            return;
        }

        Element shapeElement = document.createElement("shape");
        shapeElement.setAttribute("name", shape.getName());
        shapeElement.setAttribute("type", shape.getTypeName());

        String[] parameterNames = shape.getParametersList();
        BigDecimal[] parameters = shape.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Element parameterElement = document.createElement("parameter");
            parameterElement.setAttribute("name", parameterNames[i]);
            parameterElement.appendChild(document.createTextNode(parameters[i].toString()));

            shapeElement.appendChild(parameterElement);
        }

        shapesElement.appendChild(shapeElement);
    }

    @Override
    protected String getSaveData() {
        if (document == null) {
            return "";
        }

        backpackElement.appendChild(shapesElement);
        document.appendChild(backpackElement);
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return "";
    }
}
