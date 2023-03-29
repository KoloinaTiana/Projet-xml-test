import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.ElementFilter;
import org.jdom2.filter.Filter;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class XmlUtils {
    
    public List<Element> getElementByXPath(String n) {
        String xPath = "//plat[contains(lower-case(nom), '" + n.toLowerCase() + "')]";
        Document document;
        try {
            document = new SAXBuilder().build(new File("restaurantCatalogue.xml"));
            XPathFactory xpfac = XPathFactory.instance();
            XPathExpression<Element> xp = xpfac.compile(xPath, Filters.element());
        
            List<Element> elements = xp.evaluate(document);
            List<Element> filteredElements = new ArrayList<>();
            for (Element element : elements) {
                if (element.getName().equals("plat")) {
                    filteredElements.add(element);
                }
            }
    
            if (!filteredElements.isEmpty()) {
                for (Element element : filteredElements) {
                    String nom = element.getChildText("nom");
                    System.out.println(nom);
                }
            } else {
                System.out.println("Aucun plat trouvé pour la recherche : " + n);
            }
    
            return filteredElements;
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }     
    
}
