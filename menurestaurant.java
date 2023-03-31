import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.sf.saxon.s9api.Axis;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;

public class menurestaurant extends JFrame {

    private JTable table;
    private Object[][] data;
    private MenuTableModel model;

    public menurestaurant() {
        super("Menu Restaurant");

        // Création d'un JPanel pour afficher le texte
        JPanel panel = new JPanel(new BorderLayout());

        //ajouter un texte et une liste déroulante pour le choix de la méthode de recherche
        JPanel searchPanel = new JPanel();
        JLabel methodLabel = new JLabel("Méthode de recherche :");
        searchPanel.add(methodLabel);

        String[] methods = {"XPath", "XQuery"};
        JComboBox<String> methodList = new JComboBox<>(methods);
        searchPanel.add(methodList);

        //ajouter un champ de saisie pour la requête
        JLabel queryLabel = new JLabel("Requête :");
        searchPanel.add(queryLabel);

        JTextField queryField = new JTextField(20);
        searchPanel.add(queryField);

        //ajouter un bouton pour lancer la recherche
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchButton);
        searchButton.addActionListener(e -> {
            String searchTerm = queryField.getText().trim();
            if (searchTerm.isEmpty()) {
                model = new MenuTableModel(data);
                table.setModel(model);
            } else {
                if ("XPath".equals(methodList.getSelectedItem())) {
                    try {
                        XmlUtils u = new XmlUtils();
                        List<org.jdom2.Element> elements = u.getElementByXPath(searchTerm);
                        if(elements.isEmpty()){
                            System.out.println("Vide");
                        }
                        Object[][] newData = new Object[elements.size()][6];
                        for (int i = 0; i < elements.size(); i++) {
                            org.jdom2.Element element = elements.get(i);
                            List<String> ingredientsList = new ArrayList<>();
                            List<org.jdom2.Element> ingredients = element.getChildren("ingredients").get(0).getChildren("ingredient");
                            for (org.jdom2.Element ingredient : ingredients) {
                                ingredientsList.add(ingredient.getText());
                            }
                            String ingredientsString = String.join(",", ingredientsList);
                            newData[i] = new Object[]{
                                    ((org.jdom2.Element) element).getChildText("nom"),
                                    ((org.jdom2.Element) element).getChildText("description"),
                                    ((org.jdom2.Element) element).getChildText("prix"),
                                    ((org.jdom2.Element) element).getChildText("categorie"),
                                    ingredientsString,
                                    new JButton("Supprimer")
                            };
                        }
                        model = new MenuTableModel(newData);
                        table.setModel(model);
                        // Ajouter le Renderer personnalisé à la colonne "Action" de votre table
                        TableColumnModel columnModel = table.getColumnModel();
                        columnModel.getColumn(5).setCellRenderer(new ButtonRenderer());
                        columnModel.getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if ("XQuery".equals(methodList.getSelectedItem())) {
                    try {
                        String nomValue = "", prixValue ="", descValue="", catValue="", ingValue="";
                        XmlUtils u = new XmlUtils();
                        List<XdmNode> elements = u.getElementByXQuery(searchTerm);
                        if (elements.isEmpty()) {
                            System.out.println("Vide");
                        } else {
                            Object[][] newData = new Object[elements.size()][6];
                            for (int i = 0; i < elements.size(); i++) {
                                XdmNode element = elements.get(i);
                                XdmSequenceIterator childrenNom = element.axisIterator(Axis.CHILD, new QName("nom"));
                                while (childrenNom.hasNext()) {
                                    XdmNode child = (XdmNode) childrenNom.next();
                                    nomValue = child.getStringValue();
                                    System.out.println(nomValue);
                                }
                                XdmSequenceIterator childrenDesc = element.axisIterator(Axis.CHILD, new QName("description"));
                                while (childrenDesc.hasNext()) {
                                    XdmNode child = (XdmNode) childrenDesc.next();
                                    descValue = child.getStringValue();
                                    System.out.println(descValue);
                                }

                                XdmSequenceIterator childrenPrix = element.axisIterator(Axis.CHILD, new QName("prix"));
                                while (childrenPrix.hasNext()) {
                                    XdmNode child = (XdmNode) childrenPrix.next();
                                    prixValue = child.getStringValue();
                                    System.out.println(prixValue);
                                }

                                XdmSequenceIterator childrencat = element.axisIterator(Axis.CHILD, new QName("categorie"));
                                while (childrencat.hasNext()) {
                                    XdmNode child = (XdmNode) childrencat.next();
                                    catValue = child.getStringValue();
                                    System.out.println(catValue);
                                }

                                XdmSequenceIterator childrening = element.axisIterator(Axis.CHILD, new QName("ingredients"));
                                while (childrening.hasNext()) {
                                    XdmNode child = (XdmNode) childrening.next();
                                    ingValue = child.getStringValue();
                                    System.out.println(catValue);
                                }

                                newData[i] = new Object[]{
                                    nomValue,
                                    descValue,
                                    prixValue,
                                    catValue,
                                    ingValue,
                                    new JButton("Supprimer")
                                };
                            }
                            model = new MenuTableModel(newData);
                            table.setModel(model);
                            // Ajouter le Renderer personnalisé à la colonne "Action" de votre table
                            TableColumnModel columnModel = table.getColumnModel();
                            columnModel.getColumn(5).setCellRenderer(new ButtonRenderer());
                            columnModel.getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                
            }
        });

        // Ajout du JPanel de recherche à la partie supérieure du JPanel principal
        panel.add(searchPanel, BorderLayout.NORTH);

        // Chargement des données du catalogue
        Object[][] data = loadCatalogueData();
        model = new MenuTableModel(data);
        table = new JTable(model);
        table.setSize(600, 200);
        // Ajouter le Renderer personnalisé à la colonne "Action" de votre table
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(5).setCellRenderer(new ButtonRenderer());
        columnModel.getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Ajout du tableau à un JPanel conteneur pour permettre le positionnement
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajout du JPanel conteneur à la partie centrale du JPanel principal
        panel.add(tablePanel, BorderLayout.CENTER);

        //ajouter un texte et une liste déroulante pour le choix du format de sortie
        JPanel exportPanel = new JPanel();
        JLabel exportLabel = new JLabel("Exporter le fichier en HTML : ");
        exportPanel.add(exportLabel);

        //ajouter un bouton pour l'exportation
        JButton exportButton = new JButton("Exporter");
        exportPanel.add(exportButton);
        exportButton.addActionListener(e -> {
            XmlUtils u = new XmlUtils();
            u.TransformXMLtoHTML();

        });

        JButton addButton = new JButton("Ajouter plat");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newplat newFrame = new newplat();
                newFrame.setVisible(true);
            }
        });
        exportPanel.add(addButton);

        // Ajouter la partie d'exportation à la partie inférieure du JPanel principal
        panel.add(exportPanel, BorderLayout.SOUTH);

        // Ajout du JPanel à la fenêtre
        setContentPane(panel);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
    }

    private Object[][] loadCatalogueData() {
        try {
            // Création d'un DocumentBuilderFactory pour lire le document XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Chargement du document XML en mémoire sous forme d'un objet Document
            Document document = builder.parse("restaurantCatalogue.xml");

            // Obtention de la racine du document
            Element racine = document.getDocumentElement();

            // Obtention de la liste de tous les éléments <plat>
            NodeList plats = racine.getElementsByTagName("plat");

            // Initialisation du tableau pour stocker les données
            data = new Object[plats.getLength()][6];

            // Parcours de la liste de plats et stockage des informations dans le tableau
            for (int i = 0; i < plats.getLength(); i++) {
                Element plat = (Element) plats.item(i);
                String nom = plat.getElementsByTagName("nom").item(0).getTextContent();
                String description = plat.getElementsByTagName("description").item(0).getTextContent();
                double prix = Double.parseDouble(plat.getElementsByTagName("prix").item(0).getTextContent());
                String categorie = plat.getElementsByTagName("categorie").item(0).getTextContent();

                // Obtention de la liste des ingrédients
                NodeList ingredients = plat.getElementsByTagName("ingredient");
                List<String> ingredientList = new ArrayList<>();
                for (int j = 0; j < ingredients.getLength(); j++) {
                    Element ingredient = (Element) ingredients.item(j);
                    ingredientList.add(ingredient.getTextContent());
                }

                String ing = String.join(", ", ingredientList);

                // Stockage des informations du plat dans le tableau
                data[i][0] = nom;
                data[i][1] = description;
                data[i][2] = prix;
                data[i][3] = categorie;
                data[i][4] = ing;
                // Ajout du bouton Supprimer à la dernière colonne de chaque ligne
                data[i][5] = new JButton("Supprimer");
            }

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new menurestaurant();
            }
        });
    }

    // Classe pour le rendu des boutons
    class ButtonRenderer extends JButton implements TableCellRenderer {
        
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText("Supprimer");
            return this;
        }
    }

    // Classe pour l'édition des boutons
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;
        Document xmlDocument = null;
    
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }
    
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText("Supprimer");
            this.row = row;
            isPushed = true;
            return button;
        }
    
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Action à réaliser lors du clic sur le bouton
                File xmlFile = new File("restaurantCatalogue.xml");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                try {
                    builder = factory.newDocumentBuilder();
                    xmlDocument = builder.parse(xmlFile);
                } catch (ParserConfigurationException | IOException e1) {
                    e1.printStackTrace();
                } catch (SAXException e1) {
                    e1.printStackTrace();
                }
        
                // extraire l'élément XML correspondant à cette ligne
                Element element = (Element) xmlDocument.getElementsByTagName("plat").item(row);
                            
                // supprimer l'élément XML
                 element.getParentNode().removeChild(element);
                            
                // mettre à jour la table et le document XML
                model.supprimerLigne(row);
                modifyXml m = new modifyXml();
                try {
                     m.saveXMLDocument(xmlDocument, xmlFile);
                } catch (TransformerException e1) {
                     e1.printStackTrace();
                }
            }
            isPushed = false;
            return new String(label);
        }
    }
    
}