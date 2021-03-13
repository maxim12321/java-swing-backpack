package backpacks.parsers;

import exceptions.FullBackpackException;
import exceptions.InvalidBackpackFileException;
import exceptions.OpenFileException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import shapes.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class XMLBackpackParser extends BackpackParser {
    @Override
    protected boolean validateFile(File file) throws InvalidBackpackFileException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            Schema schema = factory.newSchema(new File("res/files/backpack_schema.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(file));
        } catch (SAXException e) {
            throw new InvalidBackpackFileException();
        } catch (IOException e) {
            throw new OpenFileException();
        }
        return true;
    }

    @Override
    protected void parseFile(File file)
            throws InvalidBackpackFileException, OpenFileException, FullBackpackException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new FileReader(file)));

            Element rootElement = document.getDocumentElement();
            String capacity = rootElement.getElementsByTagName("capacity").item(0).getTextContent();
            createBackpack(BigDecimal.valueOf(Double.parseDouble(capacity)));

            NodeList shapeNodes = rootElement.getElementsByTagName("shape");
            for (int shapeIndex = 0; shapeIndex < shapeNodes.getLength(); shapeIndex++) {
                Node shape = shapeNodes.item(shapeIndex);
                NamedNodeMap attributes = shape.getAttributes();

                String shapeType = attributes.getNamedItem("type").getNodeValue();
                if (!shapeTypes.containsKey(shapeType)) {
                    throw new InvalidBackpackFileException();
                }
                Shape currentShape = shapeTypes.get(shapeType);
                currentShape.setName(attributes.getNamedItem("name").getNodeValue());

                NodeList parameters = shape.getChildNodes();

                String[] parameterNames = currentShape.getParametersList();
                BigDecimal[] parameterValues = new BigDecimal[parameterNames.length];
                Map<String, Integer> nameToIndex = new HashMap<>();
                for (int i = 0; i < parameterNames.length; i++) {
                    nameToIndex.put(parameterNames[i], i);
                }

                for (int i = 0; i < parameters.getLength(); i++) {
                    if (!parameters.item(i).hasAttributes()) {
                        continue;
                    }
                    String parameterName = parameters.item(i).getAttributes().getNamedItem("name").getNodeValue();

                    if (!nameToIndex.containsKey(parameterName)) {
                        throw new InvalidBackpackFileException();
                    }
                    int index = nameToIndex.get(parameterName);
                    String parameterValue = parameters.item(i).getTextContent();
                    parameterValues[index] = BigDecimal.valueOf(Double.parseDouble(parameterValue));
                }

                currentShape.load(parameterValues);
                addShape(currentShape);
            }
        } catch (ParserConfigurationException | SAXException e) {
            throw new InvalidBackpackFileException();
        } catch (IOException e) {
            throw new OpenFileException();
        }
    }
}
