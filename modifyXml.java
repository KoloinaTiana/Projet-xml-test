import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class modifyXml {

    public void newXml(String id, String nom, String desc, String cat, String prix, String ingredients, String images) {
        try {
            // Charger le document XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("restaurantCatalogue.xml"));
    
            // Créer un nouvel élément plat
            Element plat = doc.createElement("plat");
            plat.setAttribute("id", id);
    
            // Ajouter l'élément nom
            Element nomElement = doc.createElement("nom");
            nomElement.setTextContent(nom);
            plat.appendChild(nomElement);
    
            // Ajouter l'élément description
            Element descElement = doc.createElement("description");
            descElement.setTextContent(desc);
            plat.appendChild(descElement);
    
            // Ajouter l'élément prix
            Element prixElement = doc.createElement("prix");
            prixElement.setTextContent(prix);
            plat.appendChild(prixElement);
    
            // Ajouter l'élément categorie
            Element catElement = doc.createElement("categorie");
            catElement.setTextContent(cat);
            plat.appendChild(catElement);
    
            // Ajouter l'élément ingredients
            Element ingredientsElement = doc.createElement("ingredients");
            String[] ingredientsArray = ingredients.split(",");
            for (String ingredient : ingredientsArray) {
                Element ingredientElement = doc.createElement("ingredient");
                ingredientElement.setTextContent(ingredient.trim());
                ingredientsElement.appendChild(ingredientElement);
            }
            plat.appendChild(ingredientsElement);
    
            // Ajouter l'élément image
            Element imageElement = doc.createElement("image");
            imageElement.setTextContent(images);
            plat.appendChild(imageElement);
    
            // Ajouter le nouvel élément plat à la racine du document
            doc.getDocumentElement().appendChild(plat);
    
            // Enregistrer les modifications dans le fichier XML en formatant le document
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");  // activer le formatage
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");  // spécifier l'indentation à utiliser
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("restaurantCatalogue.xml"));
            transformer.transform(source, result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveXMLDocument(Document doc, File xmlFile) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);
    }
    
}
