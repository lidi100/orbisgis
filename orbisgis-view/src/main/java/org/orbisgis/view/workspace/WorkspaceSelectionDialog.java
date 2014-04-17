/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. 
 * 
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 * 
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
 * 
 * This file is part of OrbisGIS.
 * 
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 * 
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.view.workspace;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.orbisgis.core.workspace.CoreWorkspaceImpl;
import org.orbisgis.sif.multiInputPanel.DirectoryComboBoxChoice;
import org.orbisgis.view.icons.OrbisGISIcon;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.orbisgis.sif.components.CustomButton;

/**
 * GUI for Workspace selection.
 *
 * @author Nicolas Fortin
 * @author Adam Gouge
 */
public class WorkspaceSelectionDialog extends JPanel {

    private static final I18n I18N = I18nFactory.getI18n(WorkspaceSelectionDialog.class);
    private static final Logger LOGGER = Logger.getLogger(WorkspaceSelectionDialog.class);

   
    private DirectoryComboBoxChoice comboBox;
    private JCheckBox defaultCheckBox;
    private DatabaseSettingsPanel databaseSettingsPanel;

    private WorkspaceSelectionDialog() {
        super(new MigLayout("wrap 1"));
    }

    private void init(Component parent,
                      CoreWorkspaceImpl coreWorkspace) {

        // Get the list of known workspaces
        List<File> knownWorkspaces = coreWorkspace.readKnownWorkspacesPath();

        // Remove the currently loaded workspace from the list
        String currentWorkspacePath = coreWorkspace.getWorkspaceFolder();
        if (currentWorkspacePath != null && !currentWorkspacePath.isEmpty()) {
            knownWorkspaces.remove(new File(currentWorkspacePath));
        }
        // Initialize components
        JLabel chooseLabel = new JLabel(I18N.tr("Choose the workspace folder"));
        String defaultFontName = chooseLabel.getFont().getName();
        Font largeFont = new Font(defaultFontName, Font.BOLD, 16);
        Font smallFont = new Font(defaultFontName, Font.PLAIN, 10);
        chooseLabel.setFont(largeFont);
        JLabel subChooseLabel = new JLabel(
                "<html><body><p style='width: 200px;'>" +
                        I18N.tr("Choose a previous OrbisGIS workspace or create a new one")
                        + "</p></body></html>");
        subChooseLabel.setFont(smallFont);
        comboBox = new DirectoryComboBoxChoice(knownWorkspaces);
        if (!knownWorkspaces.isEmpty()) {
            // Select the default workspace on the combo box
            comboBox.setValue(knownWorkspaces.get(0).getAbsolutePath());
        }
        ActionListener selectionDone = EventHandler.create(ActionListener.class, this, "onWorkspaceFolderChange");
        comboBox.getComboBox().addActionListener(selectionDone);
        defaultCheckBox = new JCheckBox(I18N.tr("Set as default?"));
        JLabel subCheckBox = new JLabel("<html><body><p style='width: 200px;'>" +
                I18N.tr("Setting this workspace as default will allow you to " +
                        "skip this dialog next time") + "</p></body></html>");
        subCheckBox.setFont(smallFont);
        // Add components
        add(chooseLabel);
        add(subChooseLabel);
        add(comboBox.getComponent());
        add(Box.createGlue());
        add(defaultCheckBox);
        add(subCheckBox);
        CustomButton customDataBase = new CustomButton(OrbisGISIcon.getIcon("database"));
        customDataBase.setText("Customize your database");
        customDataBase.setToolTipText(I18N.tr("Click to customize your database."));
        customDataBase.addActionListener(
                EventHandler.create(ActionListener.class, this, "onOpenDBPanel"));
        add(customDataBase);
        onWorkspaceFolderChange();
    }   
   
    /**
     * The user click on add open button
     */
    public void onOpenDBPanel() {
        if (databaseSettingsPanel == null) {
            databaseSettingsPanel = new DatabaseSettingsPanel((JDialog) getTopLevelAncestor());
        }
        databaseSettingsPanel.setAlwaysOnTop(true);
        databaseSettingsPanel.setVisible(true);
    }

    /**
     * Get the database settings panel.
     * 
     * @return 
     */
    public DatabaseSettingsPanel getDatabaseSettingsPanel() {
        return databaseSettingsPanel;
    }
    
    
    
    


    /**
     * @return The workspace selection combo box
     */
    public DirectoryComboBoxChoice getComboBox() {
        return comboBox;
    }

    /**
     * @return The default workspace check box
     */
    public JCheckBox getDefaultCheckBox() {
        return defaultCheckBox;
    }    

    /**
     * Shows a dialog to choose the workspace folder
     *
     * @param parent        Parent component
     * @param coreWorkspace Core workspace
     *
     * @return True if the user validate workspace change
     */
    public static boolean showWorkspaceFolderSelection(Component parent,
                                                    CoreWorkspaceImpl coreWorkspace) {
        if(!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Not on swing dispatch thread");
        }
        String oldWorkspace = coreWorkspace.getWorkspaceFolder();
        // Initialize a panel to contain the dialog
        WorkspaceSelectionDialog panel = new WorkspaceSelectionDialog();
        panel.init(parent, coreWorkspace);
        // Show the dialog and get the user's choice.
        int userChoice = JOptionPane.showConfirmDialog(parent,
                panel,
                I18N.tr("Workspace Manager"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                OrbisGISIcon.getIcon("sidebar"));

        // If the user clicked OK, then update the workspace.
        if (userChoice == JOptionPane.OK_OPTION) {
            String chosenWorkspacePath = panel.getComboBox().getValue();
            if (!ViewWorkspace.isWorkspaceValid(new File(chosenWorkspacePath))) {
                LOGGER.error(I18N.tr("The workspace folder version is invalid " +
                        "(!=OrbisGIS {0}), or the folder is not empty",
                        CoreWorkspaceImpl.MAJOR_VERSION));
                return false;
            }
            try {
                updateWorkspace(coreWorkspace, panel.getComboBox(), oldWorkspace,
                        panel);
            } catch (IOException ex) {
                LOGGER.error(I18N.tr("Problem updating the workspace. ")
                        + ex.getLocalizedMessage(), ex);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * The user select another workspace folder.Update the JDBC uri
     */
    public void onWorkspaceFolderChange() {
        CoreWorkspaceImpl tempWorkspace = new CoreWorkspaceImpl();
        tempWorkspace.setWorkspaceFolder(getComboBox().getValue());
        if (databaseSettingsPanel != null) {
            databaseSettingsPanel.setURL(tempWorkspace.getJDBCConnectionReference());
            databaseSettingsPanel.setUser(tempWorkspace.getDataBaseUser());
            databaseSettingsPanel.setPassword(tempWorkspace.getDataBasePassword());
        }
    }

    /**
     * Update and/or initialize the user-selected workspace as necessary.
     * @param coreWorkspace       Core workspace
     * @param comboBox            ComboBox with possible workspace directories
     * @param oldWorkspacePath    Path of the previous workspace
     * @param wkDialog            Workspace panel
     * @throws IOException during certain workspace operations.
     */
    private static void updateWorkspace(CoreWorkspaceImpl coreWorkspace,
                                        DirectoryComboBoxChoice comboBox,
                                        String oldWorkspacePath,WorkspaceSelectionDialog wkDialog) throws IOException {
        // Set as default workspace if necessary
        if (wkDialog.getDefaultCheckBox().isSelected()) {
            coreWorkspace.setDefaultWorkspace(new File(wkDialog.getComboBox().getValue()));
        } else {
            coreWorkspace.setDefaultWorkspace(null);
        }
        String jdbcUri = wkDialog.getDatabaseSettingsPanel().getJdbcURI().getText();
        // Create a temporary workspace to compute future path
        CoreWorkspaceImpl tempWorkspace = new CoreWorkspaceImpl();
        tempWorkspace.setWorkspaceFolder(wkDialog.getComboBox().getValue());
        if(jdbcUri.isEmpty()) {
            jdbcUri = tempWorkspace.getJDBCConnectionReference();
        }
        // Save the workspace list, including the previous one
        List<File> workspaces = comboBox.getValues();
        if (oldWorkspacePath != null && !oldWorkspacePath.isEmpty()) {
            workspaces.add(new File(oldWorkspacePath));
        }
        coreWorkspace.writeKnownWorkspaces(workspaces);
        // Initialize the workspace if empty or new
        File wkFile = new File(wkDialog.getComboBox().getValue());
        File[] files = wkFile.listFiles();
        if (!wkFile.exists() || (files != null && files.length == 0)) {
            ViewWorkspace.initWorkspaceFolder(wkFile);
        }
        // Save the database.uri file
        try(FileWriter fileWriter = new FileWriter(tempWorkspace.getDataBaseUriFilePath())) {
            fileWriter.write(jdbcUri+"\n");
        }
        // Do this at the end because there is trigger on property change
        coreWorkspace.setDataBaseUser(wkDialog.getDatabaseSettingsPanel().getUser().getText());
        coreWorkspace.setDataBasePassword(new String(wkDialog.getDatabaseSettingsPanel().getPassword().getPassword()));
        coreWorkspace.setWorkspaceFolder(wkDialog.getComboBox().getValue());
    }
}
