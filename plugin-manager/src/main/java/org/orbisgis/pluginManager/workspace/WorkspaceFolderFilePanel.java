package org.orbisgis.pluginManager.workspace;

import java.io.File;

import javax.swing.JFileChooser;

import org.orbisgis.pluginManager.ui.OpenFilePanel;

public class WorkspaceFolderFilePanel extends OpenFilePanel {

	public static final String SIF_ID = "org.orbisgis.pluginManager.WorkspaceFileChooser";

	public WorkspaceFolderFilePanel(String title, String dir) {
		super(SIF_ID, title);
		JFileChooser ret = super.getFileChooser();
		ret.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		ret.setMultiSelectionEnabled(false);
		ret.setSelectedFile(new File(dir));
	}

	public String validateInput() {
		File file = getSelectedFile();
		if (file == null) {
			return "A file must be selected";
		} else if (!file.exists())  {
			return "The directory must exist";
		} else if (!file.isDirectory()) {
			return "The selection must be a directory";
		}

		return null;
	}
}
