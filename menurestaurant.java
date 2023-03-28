import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
                loadCatalogueData();
            } else {
                if ("XPath".equals(methodList.getSelectedItem())) {
                    try {
                        XmlUtils u = new XmlUtils();
                        List<org.jdom2.Element> elements = u.getElementByXPath(searchTerm);
                        Object[][] newData = new Object[elements.size()][6];
                        for (int i = 0; i < elements.size(); i++) {
                            Element element = (Element) elements.get(i);
                            System.out.println("Hey:"+element);
                            newData[i] = new Object[]{
                                    ((org.jdom2.Element) element).getChildText("nom"),
                                    ((org.jdom2.Element) element).getChildText("description"),
                                    ((org.jdom2.Element) element).getChildText("prix"),
                                    ((org.jdom2.Element) element).getChildText("categorie"),
                                    ((org.jdom2.Element) element).getChildText("ingredients"),
                                    new JButton("Supprimer")
                            };
                        }
                        data = newData;
                        model.fireTableDataChanged();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if ("XQuery".equals(methodList.getSelectedItem())) {
                    // TODO : Appeler la méthode pour récupérer les éléments avec XQuery
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
        JLabel exportLabel = new JLabel("Exporter le résultat en :");
        exportPanel.add(exportLabel);

        String[] formats = {"HTML", "PDF"};
        JComboBox<String> formatList = new JComboBox<>(formats);
        exportPanel.add(formatList);

        //ajouter un bouton pour l'exportation
        JButton exportButton = new JButton("Exporter");
        exportPanel.add(exportButton);

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
            isPushed = true;
            this.row = row;
            return button;
        }
    
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Action à réaliser lors du clic sur le bouton
                System.out.println("Button clicked for row " + row);
            }
            isPushed = false;
            return new String(label);
        }
    }
}