/**
 * OrbisToolBox is an OrbisGIS plugin dedicated to create and manage processing.
 * <p/>
 * OrbisToolBox is distributed under GPL 3 license. It is produced by CNRS <http://www.cnrs.fr/> as part of the
 * MApUCE project, funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
 * <p/>
 * OrbisToolBox is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p/>
 * OrbisToolBox is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with OrbisToolBox. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p/>
 * For more information, please consult: <http://www.orbisgis.org/> or contact directly: info_at_orbisgis.org
 */

package org.orbisgis.wpsclient.view.ui.dataui;

import net.miginfocom.swing.MigLayout;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.components.OpenPanel;
import org.orbisgis.wpsclient.WpsClientImpl;
import org.orbisgis.wpsclient.view.utils.ToolBoxIcon;
import org.orbisgis.wpsservice.model.DescriptionType;
import org.orbisgis.wpsservice.model.Input;
import org.orbisgis.wpsservice.model.Output;
import org.orbisgis.wpsservice.model.RawData;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.IOException;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * DataUI associated to the RawData type.
 * This class generate an interactive UI dedicated to the configuration of a RawData.
 * The interface generated will be used in the ProcessEditor.
 *
 * @author Sylvain PALOMINOS
 **/

public class RawDataUI implements DataUI {

    /** Constant used to pass object as client property throw JComponents **/
    private static final String DATA_MAP_PROPERTY = "DATA_MAP_PROPERTY";
    private static final String URI_PROPERTY = "URI_PROPERTY";
    private static final String TEXT_FIELD_PROPERTY = "TEXT_FIELD_PROPERTY";
    private static final String OPEN_PANEL_PROPERTY = "OPEN_PANEL_PROPERTY";
    private static final String MULTI_SELECTION_PROPERTY = "MULTI_SELECTION_PROPERTY";

    /** WpsClient using the generated UI. */
    private WpsClientImpl wpsClient;

    @Override
    public void setWpsClient(WpsClientImpl wpsClient){
        this.wpsClient = wpsClient;
    }

    @Override
    public JComponent createUI(DescriptionType inputOrOutput, Map<URI, Object> dataMap) {
        //Create the main panel
        JComponent component = new JPanel(new MigLayout("fill"));
        //Display the SourceCA into a JTextField
        JTextField jtf = new JTextField();
        //"Save" the CA inside the JTextField
        jtf.getDocument().putProperty(DATA_MAP_PROPERTY, dataMap);
        jtf.getDocument().putProperty(URI_PROPERTY, inputOrOutput.getIdentifier());
        //add the listener for the text changes in the JTextField
        jtf.getDocument().addDocumentListener(EventHandler.create(DocumentListener.class, this,
                "saveDocumentText", "document"));

        RawData rawData = null;
        String action = null;
        if(inputOrOutput instanceof Input){
            rawData = (RawData) ((Input)inputOrOutput).getDataDescription();
            action = OpenPanel.ACTION_OPEN;
        }
        else if(inputOrOutput instanceof Output){
            rawData = (RawData) ((Output)inputOrOutput).getDataDescription();
            action = OpenPanel.ACTION_SAVE;
        }
        if(rawData == null){
            return component;
        }

        String dataAccepted;
        if(rawData.isDirectory() && !rawData.isFile()) {
            dataAccepted = OpenPanel.ACCEPT_DIRECTORY;
        }
        else if(!rawData.isDirectory() && rawData.isFile()) {
            dataAccepted = OpenPanel.ACCEPT_FILE;
        }
        else {
            dataAccepted = OpenPanel.ACCEPT_BOTH;
        }

        OpenPanel openPanel = new OpenPanel("RawData.OpenPanel", "Make your selection", action, dataAccepted);
        openPanel.setAcceptAllFileFilterUsed(true);
        openPanel.setSingleSelection(!rawData.multiSelection());


        if(dataMap.get(inputOrOutput.getIdentifier()) != null)
            jtf.setText(dataMap.get(inputOrOutput.getIdentifier()).toString());
        else {
            jtf.setText(openPanel.getCurrentDirectory().getAbsolutePath());
        }
        component.add(jtf, "growx");

        JPanel buttonPanel = new JPanel(new MigLayout());
        //Create the button Browse
        JButton browseButton = new JButton(ToolBoxIcon.getIcon(ToolBoxIcon.BROWSE));
        //"Save" the sourceCA and the JTextField in the button
        browseButton.putClientProperty(MULTI_SELECTION_PROPERTY, rawData.multiSelection());
        browseButton.putClientProperty(TEXT_FIELD_PROPERTY, jtf);
        browseButton.putClientProperty(OPEN_PANEL_PROPERTY, openPanel);
        browseButton.putClientProperty(DATA_MAP_PROPERTY, dataMap);
        browseButton.putClientProperty(URI_PROPERTY, inputOrOutput.getIdentifier());
        browseButton.setBorderPainted(false);
        browseButton.setContentAreaFilled(false);
        browseButton.setMargin(new Insets(0, 0, 0, 0));
        //Add the listener for the click on the button
        browseButton.addActionListener(EventHandler.create(ActionListener.class, this, "openLoadPanel", ""));
        buttonPanel.add(browseButton);

        //Create the button Browse
        JButton pasteButton = new JButton(ToolBoxIcon.getIcon(ToolBoxIcon.PASTE));
        //"Save" the sourceCA and the JTextField in the button
        pasteButton.putClientProperty(TEXT_FIELD_PROPERTY, jtf);
        pasteButton.setBorderPainted(false);
        pasteButton.setContentAreaFilled(false);
        pasteButton.setMargin(new Insets(0, 0, 0, 0));
        //Add the listener for the click on the button
        pasteButton.addActionListener(EventHandler.create(ActionListener.class, this, "onPaste", ""));
        buttonPanel.add(pasteButton);

        component.add(buttonPanel, "dock east");

        return component;
    }

    @Override
    public Map<URI, Object> getDefaultValue(DescriptionType inputOrOutput) {
        return new HashMap<>();
    }

    @Override
    public ImageIcon getIconFromData(DescriptionType inputOrOutput) {
        return ToolBoxIcon.getIcon(ToolBoxIcon.RAW_DATA);
    }

    public void onPaste(ActionEvent ae){
        Object sourceObj = ae.getSource();
        if(sourceObj instanceof JButton){
            JButton pasteButton = (JButton) sourceObj;
            JTextField textField = (JTextField) pasteButton.getClientProperty(TEXT_FIELD_PROPERTY);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            //odd: the Object param of getContents is not currently used
            Transferable contents = clipboard.getContents(null);
            boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
            if (hasTransferableText) {
                try {
                    textField.setText((String)contents.getTransferData(DataFlavor.stringFlavor));
                }
                catch (UnsupportedFlavorException | IOException ignored){
                }
            }
        }
    }

    /**
     * Opens an LoadPanel to permit to the user to select the file to load.
     * @param event
     */
    public void openLoadPanel(ActionEvent event){
        JButton source = (JButton) event.getSource();
        OpenPanel openPanel = (OpenPanel) source.getClientProperty(OPEN_PANEL_PROPERTY);
        if (UIFactory.showDialog(openPanel, true, true)) {
            JTextField textField = (JTextField) source.getClientProperty(TEXT_FIELD_PROPERTY);
            boolean multiSelection = (boolean) source.getClientProperty((MULTI_SELECTION_PROPERTY));
            if(multiSelection){
                String str = "";
                for(File f : openPanel.getSelectedFiles()){
                    if(str.isEmpty()){
                        str+="\""+f.getAbsolutePath()+"\"";
                    }
                    else{
                        str+=","+"\""+f.getAbsolutePath()+"\"";
                    }
                }
                Map<URI, Object> dataMap = (Map<URI, Object>) source.getClientProperty(DATA_MAP_PROPERTY);
                URI uri = (URI) source.getClientProperty(URI_PROPERTY);
                dataMap.put(uri, str);
                textField.setText(str);
            }
            else {
                textField.setText(openPanel.getSelectedFile().getAbsolutePath());
            }
        }
    }

    /**
     * Save the text contained by the Document in the dataMap set as property.
     * @param document
     */
    public void saveDocumentText(Document document){
        try {
            String name = document.getText(0, document.getLength());
            if(new File(name).exists()) {
                Map<URI, Object> dataMap = (Map<URI, Object>) document.getProperty(DATA_MAP_PROPERTY);
                URI uri = (URI) document.getProperty(URI_PROPERTY);
                dataMap.put(uri, name);
            }
        } catch (BadLocationException e) {
            LoggerFactory.getLogger(RawData.class).error(e.getMessage());
        }
    }
}
