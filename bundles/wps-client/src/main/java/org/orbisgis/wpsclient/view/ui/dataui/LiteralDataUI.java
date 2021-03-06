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
import org.orbisgis.wpsclient.WpsClientImpl;
import org.orbisgis.wpsclient.view.utils.ToolBoxIcon;
import org.orbisgis.wpsservice.model.*;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * DataUI implementation for LiteralData.
 * This class generate an interactive UI dedicated to the configuration of a LiteralData.
 * The interface generated will be used in the ProcessEditor.
 *
 * @author Sylvain PALOMINOS
 **/

public class LiteralDataUI implements DataUI {

    /** Size constants **/
    private static final int MAX_ROW_NUMBER = 10;
    private static final int MIN_ROW_NUMBER = 3;

    /** Constant used to pass object as client property throw JComponents **/
    private static final String DATA_MAP_PROPERTY = "DATA_MAP_PROPERTY";
    private static final String URI_PROPERTY = "URI_PROPERTY";
    private static final String DATA_FIELD_PROPERTY = "DATA_FIELD_PROPERTY";
    private static final String IS_OPTIONAL_PROPERTY = "IS_OPTIONAL_PROPERTY";
    private static final String TOOLTIP_TEXT_PROPERTY = "TOOLTIP_TEXT_PROPERTY";
    private static final String TYPE_PROPERTY = "TYPE_PROPERTY";
    private static final String BOOLEAN_PROPERTY = "BOOLEAN_PROPERTY";
    private static final String TEXT_AREA_PROPERTY = "TEXT_AREA_PROPERTY";
    private static final String VERTICAL_BAR_PROPERTY = "VERTICAL_BAR_PROPERTY";

    /** WpsClient using the generated UI. */
    private WpsClientImpl wpsClient;

    public void setWpsClient(WpsClientImpl wpsClient){
        this.wpsClient = wpsClient;
    }

    @Override
    public Map<URI, Object> getDefaultValue(DescriptionType inputOrOutput) {
        Map<URI, Object> uriDefaultValueMap = new HashMap<>();
        DataDescription dataDescription = null;
        URI identifier = inputOrOutput.getIdentifier();

        //Gets the dataDescription
        if(inputOrOutput instanceof Input){
            Input input = (Input) inputOrOutput;
            dataDescription = input.getDataDescription();
        }
        if(inputOrOutput instanceof Output){
            Output output = (Output) inputOrOutput;
            dataDescription = output.getDataDescription();
        }

        if (dataDescription instanceof LiteralData) {
            LiteralData literalData = (LiteralData)dataDescription;
            //If the LiteralData already contains a Value, uses it, else, uses one of the LiteralDataDomain.
            if(literalData.getValue().getData() instanceof Value){
                uriDefaultValueMap.put(identifier, ((Value)literalData.getValue().getData()).getValue());
            }
            else{
                //Find in the dataDescription the default LiteralDataDomain an retrieve its default value
                for (LiteralDataDomain ldda : ((LiteralData) dataDescription).getLiteralDomainType()) {
                    if (ldda.isDefaultDomain()) {
                        //If the default value is a Range, get the minimum as default value
                        if (ldda.getDefaultValue() instanceof Range) {
                            uriDefaultValueMap.put(identifier, ((Range) ldda.getDefaultValue()).getMinimumValue());
                        } else if (ldda.getDefaultValue() instanceof Value) {
                            uriDefaultValueMap.put(identifier, ((Value) ldda.getDefaultValue()).getValue());
                        }
                    }
                }
            }
        }
        return uriDefaultValueMap;
    }

    @Override
    public ImageIcon getIconFromData(DescriptionType inputOrOutput) {
        DataDescription dataDescription = null;
        if(inputOrOutput instanceof Input){
            dataDescription = ((Input) inputOrOutput).getDataDescription();
        }
        if(inputOrOutput instanceof Output){
            dataDescription = ((Output) inputOrOutput).getDataDescription();
        }
        if(dataDescription instanceof LiteralData) {
            LiteralData ld = (LiteralData)dataDescription;
            DataType dataType = DataType.STRING;
            if(ld.getValue() != null && ld.getValue().getDataType()!= null) {
                dataType = ld.getValue().getDataType();
            }
            switch (dataType) {
                case STRING:
                    return ToolBoxIcon.getIcon(ToolBoxIcon.STRING);
                case UNSIGNED_BYTE:
                case SHORT:
                case LONG:
                case BYTE:
                case INTEGER:
                case DOUBLE:
                case FLOAT:
                    return ToolBoxIcon.getIcon(ToolBoxIcon.NUMBER);
                case BOOLEAN:
                    return ToolBoxIcon.getIcon(ToolBoxIcon.BOOLEAN);
                default:
                    return ToolBoxIcon.getIcon(ToolBoxIcon.UNDEFINED);
            }
        }
        return ToolBoxIcon.getIcon(ToolBoxIcon.UNDEFINED);
    }

    @Override
    public JComponent createUI(DescriptionType inputOrOutput, Map<URI, Object> dataMap) {
        JPanel panel = new JPanel(new MigLayout("fill, ins 0, gap 0"));

        //If the descriptionType is an input, add a comboBox to select the input type and according to the type,
        // add a second JComponent to write the input value
        if(inputOrOutput instanceof Input){
            Input input = (Input)inputOrOutput;
            LiteralData literalData = (LiteralData)input.getDataDescription();
            //JComboBox with the input type
            JComboBox<String> comboBox = new JComboBox<>();
            comboBox.addItem(literalData.getValue().getDataType().name());

            //JPanel containing the component to set the input value
            JComponent dataField = new JPanel(new MigLayout("fill, ins 0, gap 0"));

            comboBox.putClientProperty(DATA_FIELD_PROPERTY, dataField);
            comboBox.putClientProperty(URI_PROPERTY, input.getIdentifier());
            comboBox.putClientProperty(DATA_MAP_PROPERTY, dataMap);
            comboBox.putClientProperty(IS_OPTIONAL_PROPERTY, input.getMinOccurs()==0);
            comboBox.putClientProperty(TOOLTIP_TEXT_PROPERTY, input.getResume());
            comboBox.addActionListener(EventHandler.create(ActionListener.class, this, "onBoxChange", "source"));
            comboBox.setBackground(Color.WHITE);

            onBoxChange(comboBox);

            if(comboBox.getItemCount() > 1){
                panel.add(comboBox, "growx, wrap");
            }
            panel.add(dataField, "growx, wrap");
            return panel;
        }
        return null;
    }

    /**
     * Call on selecting the type of data to use.
     * For each type registered in the JComboBox adapts the dataField panel.
     * Also add a listener to save the data value set by the user
     * @param source The comboBox containing the data type to use.
     */
    public void onBoxChange(Object source){
        JComboBox comboBox = (JComboBox) source;
        Map<URI, Object> dataMap = (Map<URI, Object>) comboBox.getClientProperty(DATA_MAP_PROPERTY);
        URI uri = (URI) comboBox.getClientProperty(URI_PROPERTY);
        boolean isOptional = (boolean)comboBox.getClientProperty(IS_OPTIONAL_PROPERTY);
        String s = (String) comboBox.getSelectedItem();
        JComponent dataComponent;
        switch(DataType.valueOf(s.toUpperCase())){
            case BOOLEAN:
                //Instantiate the component
                dataComponent = new JPanel(new MigLayout("ins 0, gap 0"));
                JRadioButton falseButton = new JRadioButton("FALSE");
                JRadioButton trueButton = new JRadioButton("TRUE");
                ButtonGroup group = new ButtonGroup();
                group.add(falseButton);
                group.add(trueButton);
                dataComponent.add(trueButton);
                dataComponent.add(falseButton);
                //Put the data type, the dataMap and the uri as properties
                falseButton.putClientProperty(TYPE_PROPERTY, DataType.BOOLEAN);
                falseButton.putClientProperty(DATA_MAP_PROPERTY,dataMap);
                falseButton.putClientProperty(URI_PROPERTY, uri);
                falseButton.putClientProperty(BOOLEAN_PROPERTY, false);
                trueButton.putClientProperty(TYPE_PROPERTY, DataType.BOOLEAN);
                trueButton.putClientProperty(DATA_MAP_PROPERTY,dataMap);
                trueButton.putClientProperty(URI_PROPERTY, uri);
                trueButton.putClientProperty(BOOLEAN_PROPERTY, true);
                dataComponent.putClientProperty(TYPE_PROPERTY, DataType.BOOLEAN);
                dataComponent.putClientProperty(DATA_MAP_PROPERTY,dataMap);
                dataComponent.putClientProperty(URI_PROPERTY, uri);
                //Set the default value and adds the listener for saving the value set by the user
                if(dataMap.get(uri) != null){
                    if((Boolean)dataMap.get(uri)){
                        trueButton.setSelected(true);
                    }
                    else{
                        falseButton.setSelected(true);
                    }
                }
                falseButton.addActionListener(EventHandler.create(
                        ActionListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                trueButton.addActionListener(EventHandler.create(
                        ActionListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                onDataChanged(dataComponent);
                break;
            case BYTE:
                //Instantiate the component
                dataComponent = new JSpinner(new SpinnerNumberModel(0, Byte.MIN_VALUE, Byte.MAX_VALUE, 1));
                //Put the data type, the dataMap and the uri as properties
                dataComponent.putClientProperty(TYPE_PROPERTY, DataType.BYTE);
                dataComponent.putClientProperty(DATA_MAP_PROPERTY,comboBox.getClientProperty(DATA_MAP_PROPERTY));
                dataComponent.putClientProperty(URI_PROPERTY,comboBox.getClientProperty(URI_PROPERTY));
                //Set the default value and adds the listener for saving the value set by the user
                if(dataMap.get(uri)!=null) {
                    ((JSpinner) dataComponent).setValue(dataMap.get(uri));
                }
                ((JSpinner)dataComponent).addChangeListener(EventHandler.create(
                        ChangeListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                onDataChanged(dataComponent);
                break;
            case INTEGER:
                //Instantiate the component
                dataComponent = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
                //Put the data type, the dataMap and the uri as properties
                dataComponent.putClientProperty(TYPE_PROPERTY, DataType.INTEGER);
                dataComponent.putClientProperty(DATA_MAP_PROPERTY,comboBox.getClientProperty(DATA_MAP_PROPERTY));
                dataComponent.putClientProperty(URI_PROPERTY,comboBox.getClientProperty(URI_PROPERTY));
                //Set the default value and adds the listener for saving the value set by the user
                if(dataMap.get(uri)!=null) {
                    ((JSpinner) dataComponent).setValue(dataMap.get(uri));
                }
                ((JSpinner)dataComponent).addChangeListener(EventHandler.create(
                        ChangeListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                onDataChanged(dataComponent);
                break;
            case LONG:
                //Instantiate the component
                dataComponent = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
                //Put the data type, the dataMap and the uri as properties
                dataComponent.putClientProperty(TYPE_PROPERTY, DataType.LONG);
                dataComponent.putClientProperty(DATA_MAP_PROPERTY,comboBox.getClientProperty(DATA_MAP_PROPERTY));
                dataComponent.putClientProperty(URI_PROPERTY,comboBox.getClientProperty(URI_PROPERTY));
                //Set the default value and adds the listener for saving the value set by the user
                if(dataMap.get(uri)!=null) {
                    ((JSpinner) dataComponent).setValue(dataMap.get(uri));
                }
                ((JSpinner)dataComponent).addChangeListener(EventHandler.create(
                        ChangeListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                onDataChanged(dataComponent);
                break;
            case SHORT:
                //Instantiate the component
                dataComponent = new JSpinner(new SpinnerNumberModel(0, Short.MIN_VALUE, Short.MAX_VALUE, 1));
                //Put the data type, the dataMap and the uri as properties
                dataComponent.putClientProperty(TYPE_PROPERTY, DataType.SHORT);
                dataComponent.putClientProperty(DATA_MAP_PROPERTY,comboBox.getClientProperty(DATA_MAP_PROPERTY));
                dataComponent.putClientProperty(URI_PROPERTY,comboBox.getClientProperty(URI_PROPERTY));
                //Set the default value and adds the listener for saving the value set by the user
                if(dataMap.get(uri)!=null) {
                    ((JSpinner) dataComponent).setValue(dataMap.get(uri));
                }
                ((JSpinner)dataComponent).addChangeListener(EventHandler.create(
                        ChangeListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                onDataChanged(dataComponent);
                break;
            case UNSIGNED_BYTE:
                //Instantiate the component
                dataComponent = new JSpinner(new SpinnerNumberModel(0, 0, Character.MAX_VALUE, 1));
                //Put the data type, the dataMap and the uri as properties
                dataComponent.putClientProperty(TYPE_PROPERTY, DataType.UNSIGNED_BYTE);
                dataComponent.putClientProperty(DATA_MAP_PROPERTY,comboBox.getClientProperty(DATA_MAP_PROPERTY));
                dataComponent.putClientProperty(URI_PROPERTY,comboBox.getClientProperty(URI_PROPERTY));
                //Set the default value and adds the listener for saving the value set by the user
                if(dataMap.get(uri)!=null) {
                    ((JSpinner) dataComponent).setValue(dataMap.get(uri));
                }
                ((JSpinner)dataComponent).addChangeListener(EventHandler.create(
                        ChangeListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                onDataChanged(dataComponent);
                break;
            case DOUBLE:
                //Instantiate the component
                dataComponent = new JSpinner(new SpinnerNumberModel(0D, Integer.MIN_VALUE, Integer.MAX_VALUE, 0.1));
                //Put the data type, the dataMap and the uri as properties
                dataComponent.putClientProperty(TYPE_PROPERTY, DataType.DOUBLE);
                dataComponent.putClientProperty(DATA_MAP_PROPERTY,comboBox.getClientProperty(DATA_MAP_PROPERTY));
                dataComponent.putClientProperty(URI_PROPERTY, comboBox.getClientProperty(URI_PROPERTY));
                //Set the default value and adds the listener for saving the value set by the user
                if(dataMap.get(uri)!=null) {
                    ((JSpinner) dataComponent).setValue(dataMap.get(uri));
                }
                ((JSpinner)dataComponent).addChangeListener(EventHandler.create(
                        ChangeListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                onDataChanged(dataComponent);
                break;
            case FLOAT:
                //Instantiate the component
                dataComponent = new JSpinner(new SpinnerNumberModel(0F, Float.MIN_VALUE, Float.MAX_VALUE, 1));
                //Put the data type, the dataMap and the uri as properties
                dataComponent.putClientProperty(TYPE_PROPERTY, DataType.FLOAT);
                dataComponent.putClientProperty(DATA_MAP_PROPERTY,comboBox.getClientProperty(DATA_MAP_PROPERTY));
                dataComponent.putClientProperty(URI_PROPERTY,comboBox.getClientProperty(URI_PROPERTY));
                //Set the default value and adds the listener for saving the value set by the user
                if(dataMap.get(uri)!=null) {
                    ((JSpinner) dataComponent).setValue(dataMap.get(uri));
                }
                ((JSpinner)dataComponent).addChangeListener(EventHandler.create(
                        ChangeListener.class,
                        this,
                        "onDataChanged",
                        "source"));
                onDataChanged(dataComponent);
                break;
            case STRING:
            default:
                //Instantiate the component
                JTextArea textArea = new JTextArea();
                textArea.setLineWrap(true);
                textArea.setRows(MIN_ROW_NUMBER);
                //Put the data type, the dataMap and the uri as properties
                Document doc = textArea.getDocument();
                doc.putProperty(DATA_MAP_PROPERTY, comboBox.getClientProperty(DATA_MAP_PROPERTY));
                doc.putProperty(URI_PROPERTY, comboBox.getClientProperty(URI_PROPERTY));
                //Set the default value and adds the listener for saving the value set by the user
                textArea.setText((String)dataMap.get(uri));
                doc.addDocumentListener(EventHandler.create(
                        DocumentListener.class,
                        this,
                        "onDocumentChanged",
                        "document",
                        "insertUpdate"));
                doc.addDocumentListener(EventHandler.create(
                        DocumentListener.class,
                        this,
                        "onDocumentChanged",
                        "document",
                        "removeUpdate"));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.getViewport().addChangeListener(EventHandler.create(
                        ChangeListener.class, this, "onViewportStateChange", ""));
                scrollPane.getViewport().putClientProperty(TEXT_AREA_PROPERTY, textArea);
                scrollPane.getViewport().putClientProperty(VERTICAL_BAR_PROPERTY, scrollPane.getVerticalScrollBar());
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(scrollPane, BorderLayout.CENTER);
                JButton paste = new JButton(ToolBoxIcon.getIcon(ToolBoxIcon.PASTE));
                paste.putClientProperty(TEXT_AREA_PROPERTY, textArea);
                paste.addActionListener(EventHandler.create(ActionListener.class, this, "onPaste", ""));
                paste.setBorderPainted(false);
                paste.setContentAreaFilled(false);
                panel.add(paste, BorderLayout.LINE_END);
                dataComponent = panel;
                textArea.setText("");
                break;
        }
        dataComponent.setToolTipText(comboBox.getClientProperty(TOOLTIP_TEXT_PROPERTY).toString());
        //Adds to the dataField the dataComponent
        JPanel panel = (JPanel) comboBox.getClientProperty(DATA_FIELD_PROPERTY);
        panel.removeAll();
        panel.add(dataComponent, "growx, wrap");
        if(isOptional) {
            dataMap.remove(uri);
        }
    }

    /**
     * Action done on clicking on the paste button.
     * @param ae ActionEvent get on clicking on the paste button.
     */
    public void onPaste(ActionEvent ae){
        JTextArea textArea = ((JTextArea)((JButton)ae.getSource()).getClientProperty(TEXT_AREA_PROPERTY));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                textArea.setText((String)contents.getTransferData(DataFlavor.stringFlavor));
            }
            catch (UnsupportedFlavorException | IOException ignored){
            }
        }
    }

    /**
     * Call when the state of the viewport of the JScrollPane of the textArea state change.
     * It uses the vertical bar properties to detect when the user need more lines to write.
     * @param e ChangeEvent.
     */
    public void onViewportStateChange(ChangeEvent e){
        JViewport vp = (JViewport)e.getSource();
        JTextArea textArea = (JTextArea)vp.getClientProperty(TEXT_AREA_PROPERTY);
        JScrollBar vertical = (JScrollBar)vp.getClientProperty(VERTICAL_BAR_PROPERTY);
        if(textArea.getRows()<MAX_ROW_NUMBER && vertical.getValue()>0 && vertical.getMaximum()>vertical.getVisibleAmount()){
            textArea.setRows(textArea.getRows()+1);
        }
    }

    /**
     * Call if the TextArea for the String type is changed and save the new text in the dataMap.
     * @param document TextArea document.
     */
    public void onDocumentChanged(Document document){

        Map<URI, Object> dataMap = (Map<URI, Object>) document.getProperty(DATA_MAP_PROPERTY);
        URI uri = (URI) document.getProperty(URI_PROPERTY);
        try {
            String text = document.getText(0, document.getLength());
            if(text.isEmpty()){
                text = null;
            }
            dataMap.put(uri, text);
        } catch (BadLocationException e) {
            LoggerFactory.getLogger(LiteralDataUI.class).error(e.getMessage());
            dataMap.put(uri, "");
        }
    }

    /**
     * Call if the JComponent where the value is defined is changed and save the new value in the dataMap.
     * @param source Source JComponent.
     */
    public void onDataChanged(Object source){
        Map<URI, Object> dataMap = (Map<URI, Object>) ((JComponent)source).getClientProperty(DATA_MAP_PROPERTY);
        URI uri = (URI) ((JComponent)source).getClientProperty(URI_PROPERTY);

        switch((DataType)((JComponent)source).getClientProperty(TYPE_PROPERTY)){
            case BOOLEAN:
                dataMap.put(uri, ((JComponent)source).getClientProperty(BOOLEAN_PROPERTY));
                break;
            case BYTE:
            case INTEGER:
            case LONG:
            case SHORT:
            case UNSIGNED_BYTE:
            case DOUBLE:
            case FLOAT:
                JSpinner spinner = (JSpinner)source;
                dataMap.put(uri, spinner.getValue());
                break;
        }
    }
}
