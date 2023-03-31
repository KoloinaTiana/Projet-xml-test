import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import net.sf.saxon.s9api.Axis;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;


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

    public List<XdmNode> getElementByXQuery(String n) throws Exception {
        String query = "for $plat in //plat[contains(lower-case(nom), '" + n.toLowerCase() + "')]\n" +
                "return $plat";
    
        Processor processor = new Processor(false);
        XQueryCompiler xqc = processor.newXQueryCompiler();
        XQueryExecutable xqe = xqc.compile(query);
    
        XQueryEvaluator xqe1 = xqe.load();
        File inputFile = new File("restaurantCatalogue.xml");
        net.sf.saxon.s9api.DocumentBuilder builder = processor.newDocumentBuilder();
        builder.setDTDValidation(false);
        XdmNode doc = builder.build(inputFile);
        xqe1.setSource(doc.asSource());
        List<XdmNode> results = new ArrayList<>();
        for (XdmItem item : xqe1.evaluate()) {
            results.add((XdmNode) item);
        }
        if (!results.isEmpty()) {
            for (XdmNode node : results) {
                XdmSequenceIterator children = node.axisIterator(Axis.CHILD, new QName("nom"));
                while (children.hasNext()) {
                    XdmNode child = (XdmNode) children.next();
                    String nomValue = child.getStringValue();
                    System.out.println(nomValue);
                }
            }
        } else {
            System.out.println("Aucun plat trouvé pour la recherche : " + n);
        }
    
        return results;
    }
    
    public void TransformXMLtoHTML(){
        String xmlFile = "restaurantCatalogue.xml";
        String xslFile = "restaurantCatalogue.xsl";
        String htmlFile = "restaurantCatalogue.html";

        // Création des objets Source pour le fichier XML et le fichier XSLT
        Source xmlSource = new StreamSource(xmlFile);
        Source xslSource = new StreamSource(xslFile);

        // Création de l'objet Result pour le fichier HTML
        Result htmlResult = new StreamResult(htmlFile);

        // Création de l'objet Transformer
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = factory.newTransformer(xslSource);
            transformer.transform(xmlSource, htmlResult);
            System.out.println("Transformation en HTML réussie!");
        } catch (TransformerException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        
    }    
    
}
