package com.javarush.task.task32.task3209;

import javafx.scene.web.HTMLEditor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view){
        this.view = view;
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();


    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void exit(){
        System.exit(0);
    }

    public void init(){
        createNewDocument();
    }

    public View getView() {
        return view;
    }

    public void resetDocument(){
        if(document != null)
        document.removeUndoableEditListener(view.getUndoListener());

        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        document = (HTMLDocument) htmlEditorKit.createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    public void setPlainText(String text){
        try {
            resetDocument();
            StringReader stringReader = new StringReader(text);
            HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
            htmlEditorKit.read(stringReader, document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText(){
        StringWriter stringWriter = new StringWriter();
        try {
            new HTMLEditorKit().write(stringWriter, document, 0,document.getLength());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }

        return stringWriter.toString();
    }

    public void createNewDocument(){
        view.selectHtmlTab();
        this.resetDocument();
        view.setTitle("HTML редактор");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument(){
        try {
            view.selectHtmlTab();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new HTMLFileFilter());
            fileChooser.setDialogTitle("Open File");
            int result = fileChooser.showOpenDialog(view);

            if (result == 0) {

                currentFile = fileChooser.getSelectedFile();
                resetDocument();
                view.setTitle(currentFile.getName());

                FileReader reader = new FileReader(currentFile);
                new HTMLEditorKit().read(reader, document, 0);
                view.resetUndo();
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }


    }

    public void saveDocument(){
        view.selectHtmlTab();
        if (currentFile == null)
            saveDocumentAs();
        else {
            try {
                view.setTitle(currentFile.getName());
                FileWriter writer = new FileWriter(currentFile);
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }

        view.resetUndo();
    }

    public void saveDocumentAs() {
        view.selectHtmlTab();
        JFileChooser jFileChooser = new JFileChooser();
        HTMLFileFilter htmlFileFilter = new HTMLFileFilter();
        jFileChooser.setFileFilter(htmlFileFilter);
        if (jFileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION){
            currentFile = jFileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
        try {
            FileWriter fileWriter = new FileWriter(currentFile);
            new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
            fileWriter.close();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }
    }

}
