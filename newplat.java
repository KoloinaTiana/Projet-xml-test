import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class newplat extends JFrame {
    private JTextField idField, prixField, nomField, descriptionField, ingredientsField, imageField;
    private JComboBox categorieField;
    File selectedFile;
    String fileName;

    public newplat() {

        // Définir les propriétés de la fenêtre
        setTitle("Nouveau plat");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Créer un JPanel pour contenir les champs et les boutons
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ajouter un champ de saisie pour l'id
        JLabel idLabel = new JLabel("ID :");
        idField = new JTextField(10);
        idField.setDocument(new NumericDocument()); // Autoriser uniquement les chiffres
        panel.add(idLabel);
        panel.add(idField);

        // Ajouter un champ de saisie pour le nom
        JLabel nomLabel = new JLabel("Nom :");
        nomField = new JTextField(20);
        panel.add(nomLabel);
        panel.add(nomField);

        // Ajouter un champ de saisie pour la description
        JLabel descriptionLabel = new JLabel("Description :");
        descriptionField = new JTextField(20);
        panel.add(descriptionLabel);
        panel.add(descriptionField);

        // Ajouter un champ de saisie pour la catégorie
        JLabel categorieLabel = new JLabel("Catégorie :");
        String[] categories = {"Entrée", "Plat principal", "Boisson", "Dessert"};
        categorieField = new JComboBox(categories);
        panel.add(categorieLabel);
        panel.add(categorieField);

        // Ajouter un champ de saisie pour les ingrédients
        JLabel ingredientsLabel = new JLabel("Ingrédients :");
        ingredientsField = new JTextField(20);
        panel.add(ingredientsLabel);
        panel.add(ingredientsField);

        // Ajouter un champ de saisie pour l'image
        JLabel imageLabel = new JLabel("Image :");
        imageField = new JTextField(20);
        panel.add(imageLabel);

        // Ajouter un bouton Parcourir pour sélectionner l'image
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.add(imageField);
        JButton parcourirButton = new JButton("Parcourir");
        parcourirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ouvrir une boîte de dialogue de sélection de fichier
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(new JFrame());
                if (result == JFileChooser.APPROVE_OPTION) {
                    // L'utilisateur a sélectionné un fichier
                    selectedFile = fileChooser.getSelectedFile();
                    String imagePath = selectedFile.getAbsolutePath();
                    // Mettre à jour le champ d'entrée d'image avec le chemin d'accès au fichier
                    imageField.setText(imagePath);
                }
            }
        });
        imagePanel.add(parcourirButton);
        panel.add(imagePanel);

        // Ajouter un champ de saisie pour le prix
        JLabel prixLabel = new JLabel("Prix :");
        prixField = new JTextField(10);
        prixField.setDocument(new NumericDocument()); // Autoriser uniquement les chiffres
        prixField.setPreferredSize(new Dimension(100, 20));
        panel.add(prixLabel);
        panel.add(prixField);

        // Ajouter un bouton Ajouter
        JButton ajouterButton = new JButton("Ajouter");
        JButton annulerButton = new JButton("Annuler");
        annulerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Fermer la fenêtre
                dispose();
            }
        });
        ajouterButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // Validation des champs avant l'ajout du plat
                boolean valide = true;
                if (idField.getText().isEmpty() || nomField.getText().isEmpty()
                        || descriptionField.getText().isEmpty() || ingredientsField.getText().isEmpty()
                        || imageField.getText().isEmpty() || prixField.getText().isEmpty()) {
                    valide = false;
                }
                if (valide) {
                    // Créer une nouvelle instance de la classe addXml
                    modifyXml xml = new modifyXml();

                    // Récupérer le nom de fichier
                    fileName = selectedFile.getName();
                    
                    // Enregistrer l'image dans le dossier "images" du projet avec le même nom de fichier
                    String path = "./images/" + fileName;
                    File destFile = new File(path);
                    try {
                        Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Appeler la méthode newXml en passant toutes les données nécessaires
                    xml.newXml(idField.getText(),nomField.getText(),descriptionField.getText(),categorieField.getSelectedItem().toString(),prixField.getText(),ingredientsField.getText(),path);
                    JOptionPane.showMessageDialog(null, "Le plat a été ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    // Réinitialiser les champs du formulaire
                    idField.setText("");
                    nomField.setText("");
                    descriptionField.setText("");
                    ingredientsField.setText("");
                    imageField.setText("");
                    prixField.setText("");
                    categorieField.setSelectedIndex(0);
                }
            }
        });
        panel.add(ajouterButton);
        panel.add(annulerButton);
        setContentPane(panel);
    }
    


        public class NumericDocument extends PlainDocument {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null) {
                    return;
                }
        
                char[] chars = str.toCharArray();
                boolean isNumeric = true;
        
                for (int i = 0; i < chars.length; i++) {
                    if (!Character.isDigit(chars[i]) && chars[i] != '.') {
                        isNumeric = false;
                        break;
                    }
                }
        
                if (isNumeric) {
                    super.insertString(offset, str, attr);
                }
            }
        }
    
}
