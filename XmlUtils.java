import java.io.File;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class XmlUtils {
    
    public List<Element> getElementByXPath(String n) throws Exception {
        String xPath = "//plat[contains(translate(nom, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + n.toLowerCase() + "')]";
        Document document = new SAXBuilder().build(new File("restaurantCatalogue.xml"));
        XPathFactory xPathFactory = XPathFactory.instance();
        XPathExpression<Element> expression = xPathFactory.compile(xPath, Filters.element());
        System.out.println(xPath);
        return expression.evaluate(document);
    }
    
}