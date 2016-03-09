package author_tool;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;

public class ATController {
    @FXML private TableView<Keyword> keywordTable = new TableView<Keyword>();
    @FXML private TextField articleTitle, metaTitle, metaDesc, artWordCountField;
    @FXML private TextField totWordCountField, costPerWordField, articleCostField;
    @FXML private TextField sentParaField, glField, addKeywordField;
    @FXML private TextArea articleArea;
    @FXML private Button addKeyBtn;

    private Article articleText = new Article();
    private ArticleStats articleData = new ArticleStats();
    private Stage myStage;

    private String kW = "";
    ObservableList<Keyword> keyData = FXCollections.observableArrayList();

    // Constructor
    public ATController() {}

    // @param Stage
    public void setStage(Stage stage) {
        myStage = stage;
    }

    // Sets article text, title, and META information
    public void setArticleText() {
        articleText.setText(articleArea.getText());
        articleText.setTitle(articleTitle.getText());
        articleText.setMetaTitle(metaTitle.getText());
        articleText.setMetaDescription(metaDesc.getText());
    }

    // Exits application
    @FXML
    private void exitAT(ActionEvent event) {
        System.exit(0);
    }

    // Sets the title of the content
    @FXML
    private void updateTitle() {
        articleTitle.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    articleText.setTitle(articleTitle.getText());
                }
            }
        });
    }

    // Sets the META title of the content
    @FXML
    private void updateMetaT() {
        metaTitle.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (metaTitle.getText().length() >= 60) {
                        metaTitle.setText(metaTitle.getText().substring(0, 60));
                    }
                }
            }
        });

        metaTitle.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    articleText.setMetaTitle(metaTitle.getText());
                }
            }
        });
    }

    // Sets the META description of the content
    @FXML
    private void updateMetaD() {
        metaDesc.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (metaDesc.getText().length() >= 160) {
                        metaDesc.setText(metaDesc.getText().substring(0, 160));
                    }
                }
            }
        });

        metaDesc.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    articleText.setMetaDescription(metaDesc.getText());
                }
            }
        });
    }

    // Adds keyword to keyword list
    @FXML
    private void addKeyword() {
        // Add Keyword if Keyword button is pressed
        addKeyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newKeyword();
            }
        });

        // Add Keyword if Enter is pressed while in Add Keyword field
        addKeywordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    newKeyword();
                }
            }
        });
    }

    // Updates data related to keywords
    @FXML
    private void updateKeyData() {
        keyData = keywordTable.getItems();
        if (keyData.size() > 0) {
            articleText.setText(articleArea.getText());
            articleData.calcArticleData(articleText.getText(), articleTitle.getText(), metaTitle.getText(),
                    metaDesc.getText());
            keyData.forEach(keyword -> {
                keyword.setCount(keyword.calcKeyData(articleText));
                keyword.setDensity(keyword.getCount(), articleData.getWordCount());
            });
        } else {
            Alert keyAlert = new Alert(Alert.AlertType.WARNING);
            keyAlert.setTitle("Warning: Keyword List");
            keyAlert.setHeaderText("No Keywords Identified");
            keyAlert.setContentText("You have not specified a keyword.\nPlease add a keyword to the keyword list");
            keyAlert.showAndWait();
        }
    }

    // updates data related to the article
    @FXML
    private void updataArticleData() {
        validatePrice();
    }

    // updates the Article Price field with total cost of article based on total word count
    @FXML
    private void updatePrice() {
        costPerWordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    validatePrice();
                }
            }
        });
    }

    // Checks to make sure entered price includes only numbers and one decimal
    private void validatePrice() {
        boolean containsInvChar = true;
        int decimalCount = 0;
        for (int index = 0; index < (costPerWordField.getText().length()); index++) {
            if (costPerWordField.getText().charAt(index) >= '0' && costPerWordField.getText().charAt(index) <= '9'
                    || (costPerWordField.getText().charAt(index) == '.')) {
                containsInvChar = false;
                if (costPerWordField.getText().charAt(index) == '.') {
                    decimalCount++;
                    if (decimalCount > 1) {
                        costPerWordField.clear();
                        costPerWordField.setText("0.00");
                        break;
                    }
                }
            }
            else {
                containsInvChar = true;
                costPerWordField.clear();
                costPerWordField.setText("0.00");
                break;
            }
        }
        if (!containsInvChar && decimalCount < 2) {
            articleData.setCostPerWord(Double.parseDouble(costPerWordField.getText()));
            artDataUpdate();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Invalid");
            alert.setHeaderText("Invalid price per word");
            alert.setContentText("Please re-enter your price per word (X.XX)");
            alert.showAndWait();
        }
    }

    // Clears all fields, the text area, and the keyword list to create a new document
    @FXML
    private void newDocument(ActionEvent event) {
        articleArea.clear();
        keyData.clear();
        articleTitle.clear();
        metaTitle.clear();
        metaDesc.clear();
        costPerWordField.setText("0.00");
        keywordTable.refresh();
        articleText = new Article();
        articleData = new ArticleStats();
        artDataUpdate();
    }

    // Deletes the specified keyword
    @FXML
    private void deleteKeyword(ActionEvent event) {
        int selIndex = keywordTable.getSelectionModel().getSelectedIndex();
        if (selIndex >= 0) {
            keywordTable.getItems().remove(selIndex);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Keyword Selected");
            alert.setHeaderText("No Keyword Selected");
            alert.setContentText("Please select a keyword from the table");
            alert.showAndWait();
        }
    }

    // Prompts user to save their article file
    @FXML
    private void saveDoc() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName(articleTitle.getText());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Document Files", "*.doc"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File savedFile = fileChooser.showSaveDialog(myStage);

        if (savedFile != null) {
            try {
                saveArticle(savedFile);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    // Saves user file with layout
    private void saveArticle(File file) throws IOException {
        file.createNewFile();
        PrintWriter pw = new PrintWriter(new FileWriter(file), true);
        pw.println("Article Title: " + articleText.getTitle());
        pw.println("META Title: " + articleText.getMetaTitle());
        pw.println("META Description: " + articleText.getMetaDescription());
        pw.println();
        pw.println("Reading Grade Level: " + articleData.getReadingGradeLevel());
        pw.println("Article Word Count: " + articleData.getWordCount());
        pw.println("Total Word Count (Includes META and Title): " + articleData.getTotalWordCount());
        pw.println("Total Cost of Article: $" + articleData.getArticlePrice());
        pw.println();
        pw.println("Article Text");
        pw.println();
        String[] lines = articleArea.getText().split("\\n");
        for(int i = 0 ; i < lines.length; i++) {
            pw.println(lines[i]);
        }
        pw.println();
        pw.println("Keywords");
        pw.println();
        keyData.forEach(keyword -> {
            pw.println("Keyword: " + keyword.getKeyword());
            pw.println("Keyword Count: " + keyword.getCount());
            BigDecimal kD = new BigDecimal(keyword.getDensity() *100);
            kD = kD.setScale(2, RoundingMode.CEILING);
            pw.println("Keyword Density: " + kD + "%");
            pw.println();
        });
        pw.close();
    }

    // Checks to make sure keyword has been entered, and then adds it to table
    private void newKeyword() {
        keyData = keywordTable.getItems();
        kW = addKeywordField.getText();
        if (kW.length() > 0) {
            articleText.setText(articleArea.getText());
            articleData.calcArticleData(articleText.getText(), articleTitle.getText(), metaTitle.getText(),
                    metaDesc.getText());
            keyData.add(new Keyword(kW));
            if (articleData.getWordCount() > 0) {
                keyData.forEach(keyword -> {
                    keyword.setCount(keyword.calcKeyData(articleText));
                    keyword.setDensity(keyword.getCount(), articleData.getWordCount());
                });
            }
            addKeywordField.clear();
            keyData = keywordTable.getItems();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Keyword");
            alert.setHeaderText("Add Keyword Field Empty");
            alert.setContentText("No keyword entered. Please enter a keyword and try again.");
            alert.showAndWait();
        }
    }

    // Updates all the data for the current article content
    @FXML
    private void artDataUpdate() {
        setArticleText();
        String text = articleText.getText();
        ObservableList<CharSequence> paragraphs = articleArea.getParagraphs();
        articleData.calcParagraphCount(paragraphs);
        articleData.calcArticleData(text, articleTitle.getText(), metaTitle.getText(), metaDesc.getText());
        sentParaField.setText(Double.toString(articleData.getSpp()));
        artWordCountField.setText(Integer.toString(articleData.getWordCount()));
        glField.setText(Double.toString(articleData.getReadingGradeLevel()));
        displayPrice();
    }

    // Displays total cost of Article in Price field
    private void displayPrice() {
        BigDecimal cpw, wCount, aPrice;
        cpw = new BigDecimal(articleData.getCostPerWord());
        totWordCountField.setText(Integer.toString(articleData.getTotalWordCount()));
        wCount = new BigDecimal(Integer.toString(articleData.getTotalWordCount()));
        aPrice = cpw.multiply(wCount);
        articleData.setArticlePrice(aPrice.doubleValue());
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        articleCostField.setText(currency.format(aPrice));
    }

    // Opens default web browser to my LinkedIn page
    @FXML
    public void linkClick(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.linkedin.com/in/shaun-fyffe-950a9720"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // Opens the about scene
    @FXML
    public void aboutAT(ActionEvent event) throws IOException {
        final Stage about = new Stage();
        about.initModality(Modality.APPLICATION_MODAL);
        about.initOwner(myStage);
        about.setResizable(false);
        AnchorPane aboutScene = FXMLLoader.load(getClass().getResource("AboutAuthorTool.fxml"));
        about.setScene(new Scene(aboutScene));
        about.setTitle("ECHO Content Author Tool");
        about.show();
    }
}