package com.example.demo.Controller;

import com.example.demo.LoadNote;
import com.example.demo.Model.SaveState;
import com.example.demo.NoteContent;
import com.example.demo.NoteImageContent;
import com.example.demo.NoteTextContent;
import com.example.demo.UserInput.AlertBox;
import com.example.demo.UserInput.ConfirmBox;
import com.example.demo.UserInput.TextBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.Paragraph;

import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

//Tanner

public class NoteController implements Initializable {

    @FXML
    private InlineCssTextArea textFld;

    @FXML
    private Spinner<Integer> fontSpinner;

    @FXML
    private ColorPicker colorPicker;


    // sets up the spinner for changing font. min value is 12, max is 40, and the increment is 1
    SpinnerValueFactory<Integer> spinValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(12,40, 1);

    // Variable for the PDBox font, defaults to Helvetica
    private PDType1Font pdfFont = PDType1Font.HELVETICA;

    // just a default font size
    private float fontSize = 12;

    private String title;

    private FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG and PNGs", "*.png", "*.PNG", "*.jpg", "*.JPG");

    //The tabsController
    private TabsController tabsController;

    /**
     * This is a save which allows for pdf output to multiple lines
     * @param filePath where the file is being saved
     * @param filename name of not being saved
     * @throws IOException when there is an error in files
     */
    private void multiLineSave(String filePath, String filename) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        float pdfFontSize = (float) fontSpinner.getValue();
        float leading = 1.5f * fontSize;

        PDRectangle mBox = page.getMediaBox();
        float margin = 72;
        float width = mBox.getWidth() - 2*margin;
        float startX = mBox.getLowerLeftX() + margin;
        float startY = mBox.getUpperRightY() - margin;
        float height = (pdfFont.getFontDescriptor().getAscent()) / 1000 * pdfFontSize;
        height = height * 2.7f;
        System.out.println(height);
        System.out.println(pdfFont.getFontDescriptor().getCapHeight());
        System.out.println(pdfFont.getFontDescriptor().getAscent());
        System.out.println(leading);

        int lastSpace = -1;
        ArrayList<String> lines = new ArrayList<>();
        String line;

//        for (Paragraph<String, String, String> charSeq: textFld.getParagraphs()){
//            line = charSeq.toString();
//            float size = pdfFontSize * pdfFont.getStringWidth(line) / 1000;
//            if(size > width){
//                while(!line.isEmpty()){
//                    int spaceIndex = line.indexOf(' ', lastSpace + 1);
//                    if(spaceIndex < 0){
//                        spaceIndex = line.length();
//                    }
//                    String subString = line.substring(0, spaceIndex);
//                    size = pdfFontSize * pdfFont.getStringWidth(subString) / 1000;
//                    //System.out.printf("'%s' - %f of %f\n", subString, size, width);
//                    if(size > width){
//                        if(lastSpace < 0){
//                            lastSpace = spaceIndex;
//                        }
//                        subString = line.substring(0, lastSpace);
//                        lines.add(subString);
//                        line = line.substring(lastSpace).trim();
//                        //System.out.printf("'%s' is line\n", subString);
//                        lastSpace = -1;
//                    }
//                    else if(spaceIndex == line.length()){
//                        lines.add(line);
//                        line = "";
//                    }
//                    else{
//                        lastSpace = spaceIndex;
//                    }
//                }
//            }
//            else{
//                lines.add(line);
//            }
//        }
        try{
            contentStream.beginText();
            contentStream.setFont(pdfFont, (float) fontSpinner.getValue());
            contentStream.setNonStrokingColor((float) colorPicker.getValue().getRed(), (float) colorPicker.getValue().getGreen(), (float) colorPicker.getValue().getBlue());
            contentStream.newLineAtOffset(startX,startY);
            contentStream.setLeading(leading);
            for( String lineOfText: lines){
                /**Kayden start*/
                if(lineOfText.contains("**")){
                    String tempPicName = lineOfText.substring(lineOfText.indexOf("**") + 2);
                    tempPicName = tempPicName.substring(0, tempPicName.indexOf("**"));
                    System.out.println(tempPicName);

                    PDImageXObject pdImage = PDImageXObject.createFromFile(NoteTaker.GetImageFilePath() + "\\"
                            + tempPicName + ".png", doc);

                    float picWidth = pdImage.getWidth();
                    float picHeight = pdImage.getHeight();

                    float scaleFactor = 0;

                    if (picWidth > width) {
                        scaleFactor = width / picWidth;
                        picWidth = width;
                        picHeight *= scaleFactor;
                    }
                    contentStream.endText();
                    contentStream.drawImage(pdImage, startX, startY, picWidth, picHeight);
                    contentStream.beginText();
                }
                /**Kayden ends*/
                else{
                    contentStream.showText(lineOfText);
                }
                contentStream.newLine();

                startY = startY - leading*4/3;
            }
            contentStream.endText();
            contentStream.close();

            ListView<String> notes = tabsController.getNotesListView();
            doc.save(filePath + "\\" + filename + ".pdf");

            File note_txt = new File(filePath + "\\" + filename + ".txt");
            FileWriter fWriter = new FileWriter(note_txt);
            note_txt.setWritable(true);
            BufferedWriter writer = new BufferedWriter(fWriter);
            writer.write(textFld.getText());
            writer.close();
            if(!notes.getItems().contains(filename)){
                tabsController.getNotesListView().getItems().add(filename);
            }
        }
        catch (IllegalArgumentException e){
            AlertBox.display("Could not save", "Either the PDF is already open, or the file contains characters that cannot be saved");
        }

    }

    /**
     * Loads text from textfile into the textArea
     * @param filePath the filePath of the pdf we want to load from
     * @throws IOException when the is an error with the file being read
     */
    public void load(String filePath, String filename) throws IOException{
        File file = new File(filePath + "\\" + filename + ".pdf");
        File notes_txt = new File(filePath + "\\" + filename + ".txt");
        File notes_doc = new File(filePath + "\\" + filename + ".docx");
        if(file.exists() && notes_txt.exists()){
            FileReader fReader = new FileReader(notes_txt);
            BufferedReader reader = new BufferedReader(fReader);

            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = reader.readLine();
            }
            String text = sb.toString();
            reader.close();

            System.out.println(text);
            textFld.appendText(text);
            title = filename;
        }
        else if(notes_doc.exists()){
            textFld.clear();
            ArrayList<ArrayList<NoteContent>> output = LoadNote.LoadDocx(notes_doc.getPath());
            int runStart;
            int runEnd;

            for (int paragraphNum = 0; paragraphNum < output.size(); paragraphNum++){
                runStart = 0;
                runEnd = 0;

                for (int runIndex = 0; runIndex < output.get(paragraphNum).size(); runIndex++){

                    if (output.get(paragraphNum).get(runIndex) instanceof NoteImageContent){
                        System.out.println("image: " + (((NoteImageContent) output.get(paragraphNum).get(runIndex)).imageFileName));
                    }
                    else if (output.get(paragraphNum).get(runIndex) instanceof NoteTextContent){
                        NoteTextContent runContext = ((NoteTextContent) output.get(paragraphNum).get(runIndex));
                        String style = "";
                        System.out.println("text: " + ((NoteTextContent) output.get(paragraphNum).get(runIndex)).text);
                        System.out.println("fontfamily: " + runContext.fontFamily );
                        System.out.println("textColour: " + runContext.textColour );
                        System.out.println("fontSize: " + runContext.fontSize );
                        System.out.println("highlightColor: " + runContext.highlightColor );
                        runStart = runEnd;
                        runEnd += runContext.text.length();
                        style += "-fx-font-size: " + runContext.fontSize + ";";
                        textFld.appendText(runContext.text );
                        if(runContext.textColour != null){
                            int red = Integer.parseInt(runContext.textColour.substring(0,2), 16);
                            int green = Integer.parseInt(runContext.textColour.substring(2,4), 16);
                            int blue = Integer.parseInt(runContext.textColour.substring(4), 16);
                            style += "-fx-fill: rgb("+red+ "," +green +"," +blue +");";
                        }
                        if(runContext.fontFamily != null){
                            if(runContext.fontFamily.equals("Times New Roman")){
                                style += "-fx-font-family: serif;";
                            }
                            else if(runContext.fontFamily.equals("Courier New")){
                                style += "-fx-font-family: monospace;";
                            }
                            else if(runContext.fontFamily.equals("Helvetica")){
                                style += "-fx-font-family: sans-serif;";
                            }
                        }
                        if(!runContext.highlightColor.equals("none")){
                            style += "-rtfx-background-color: " + runContext.highlightColor + ";";
                        }
                        // set style of the run at the end of each run
                        textFld.setStyle(paragraphNum, runStart, runEnd,style );
                    }
                }

                System.out.println();// to add newlines for paragraphs with no runs (blank lines in word doc)
                textFld.appendText("\n");
            }

            title = filename;
        }
        else{
            AlertBox.display("Could not load file", "Selected file no longer exists");
        }
    }

    /**
     * sets the fontSize for the PDF
     * @param newPDFFontSize new size of font
     */
    private void setPDFFontSize(float newPDFFontSize){
        fontSize = newPDFFontSize;
    }

    /**
     * sets the font for the pdf
     * @param newFont the font we will change to
     */
    private void setPdfFont(PDType1Font newFont){
        pdfFont = newFont;
    }

    /**
     * General method to change the font in the textArea the user is typing in
     * @param newTextFont the font that we will change to
     */
    private void setTextFont(String newTextFont){
        if(textFld.getSelectedText() != null){
            String style = textFld.getStyleAtPosition(textFld.getSelection().getStart()); // get style of selection
            style = style.replaceFirst("-fx-font-family:.+;", ""); // remove existing font style if it exists
            style += "-fx-font-family: " + newTextFont + ";" ; // add new style
            System.out.println(style);
            textFld.setStyle(textFld.getSelection().getStart(),textFld.getSelection().getEnd(),style );
        }
        else{
            textFld.setStyle(textFld.getCurrentParagraph(), "-fx-font-family: " + newTextFont + ";");
        }

    }

    /**
     * General method to change the font size in the textArea the user is typing in
     * @param newTextFontSize
     */
    private void setTextFontSize(float newTextFontSize){
        if(textFld.getSelectedText() != null){
            String style = textFld.getStyleAtPosition(textFld.getSelection().getStart()); // get style of selection
            style = style.replaceFirst("-fx-font-size:.+;", ""); // remove existing font style if it exists
            style += "-fx-font-size: " + newTextFontSize + ";" ; // add new style
            System.out.println(style);
            textFld.setStyle(textFld.getSelection().getStart(),textFld.getSelection().getEnd(),style);
        }
        else{
            textFld.setStyle(textFld.getCurrentParagraph(), "-fx-font-size: " + newTextFontSize + ";");
        }
        setPDFFontSize(newTextFontSize);
    }


    /**
     * when save is pressed in notes menubar
     */
    @FXML
    private void fileSaveHit() throws IOException {
        if(title == null){
            title = TextBox.display("Pick a title", "Pick a title for your notes");
            ListView<String> notes = tabsController.getNotesListView();
            if(notes.getItems().contains(title)){
                if(!ConfirmBox.display("Overwriting previous noted titled" + title, "Are you sure you want to overwrite the other file with this name?")){
                    title = TextBox.display("Pick a title", "Pick a title for your notes");
                }
            }
        }
        multiLineSave(NoteTaker.GetNotesFilePath(), title);
    }


    /**
     * when load is pressed on notes menu bar
     */
    @FXML
    void onFileLoad() throws IOException {
        if(title == null){
            AlertBox.display("Error in Loading", "Must Save before a load");
        }
        else{
            load(NoteTaker.GetNotesFilePath(), title);
        }
    }

    //Tommy
    /**
     * Insert Image into notes at end of page
     * @throws IOException if Files.copy fails
     */
    @FXML
    void onInsertImage() throws  IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Import Image");
        Stage stage = new Stage();
        File myFile = fileChooser.showOpenDialog(stage);
        if (myFile != null){
            //checking if the file already exists
            String tempString = NoteTaker.GetImageFilePath();
            String image_filepath =  ".\\" + tempString.replace("/", "\\") + "\\";
            if (SaveState.FileExists(myFile.getName(), NoteTaker.GetPDFFilePath())){
                if(!ConfirmBox.display("Warning","A file with the name " + myFile.getName() + " already exists,\n would you like to overwrite it?")){
                    System.out.println("No image added");
                    return;//user doesn't want to overwrite their existing file
                }
            }

            // copies file from chosen path to our myFiles package, REPLACE_EXISTING overwrites file if same name
            Files.copy(myFile.toPath(), Path.of(image_filepath+myFile.getName()), REPLACE_EXISTING );
            File source = new File(image_filepath+myFile.getName());

            boolean success = source.exists();
            // prints message that file was imported and saved
            System.out.println("Operation success " + success);
            if(success){
                // save cursor position
                int caretPosition = textFld.getCaretPosition();
                // removing extension
                String filename = myFile.getName().replace(".png", "");
                filename = filename.replace(".PNG", "");
                filename = filename.replace(".jpg", "");
                filename = filename.replace(".JPG", "");
                textFld.appendText("**"+filename+"**");
            }
        }
        else{
            System.out.println("No file imported");
        }
    }

    @FXML
    void setTextFontCourier(ActionEvent event) {
        setTextFont("monospace");
        setPdfFont(PDType1Font.COURIER);
    }

    @FXML
    void setTextFontDefault(ActionEvent event) {
        setTextFont("sans-serif");
        setPdfFont(PDType1Font.HELVETICA);
    }

    @FXML
    void setTextFontTNR(ActionEvent event) {
        setTextFont("serif");
        setPdfFont(PDType1Font.TIMES_ROMAN);
    }


    // Tommy
    public void getTabsController(TabsController tabsController) {
        this.tabsController = tabsController;
    }

    //Tommy
    public void NewSession(){
        title = null;
        textFld.clear();
    }

    //Tommy
    /**
     * Returns string to convert from Color object to css colour
     */
    private String getRGBValue(Color color){
        return "rgb(" + ColorInt(color.getRed())
                +"," + ColorInt(color.getGreen())
                +"," + ColorInt(color.getBlue())+ ")" ;
    }

    //Tommy
    /**
     * Helper function for getRGBValue
     */
    private int ColorInt(double val){
        return (int) (val*255);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontSpinner.setValueFactory(spinValFac);
        fontSpinner.setEditable(false);

        fontSpinner.valueProperty().addListener( (v, oldValue, newValue) ->{
            setTextFontSize(newValue);
        });

        textFld.setWrapText(true);

        // for colour change
        colorPicker.valueProperty().addListener((obs, oldColor, newColor) ->{
            textFld.setStyle(
                    "-fx-fill: " + getRGBValue(newColor) + ";"
            );
            System.out.println(getRGBValue(newColor));
        });
    }
}