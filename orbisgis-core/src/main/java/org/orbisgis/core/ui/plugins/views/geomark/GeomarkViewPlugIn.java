package org.orbisgis.core.ui.plugins.views.geomark;

import java.util.Observable;

import javax.swing.JMenuItem;

import org.orbisgis.core.images.IconNames;
import org.orbisgis.core.ui.editor.IEditor;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.pluginSystem.ViewPlugIn;
import org.orbisgis.core.ui.pluginSystem.workbench.Names;

public class GeomarkViewPlugIn extends ViewPlugIn {

	private GeomarkPanel panel;
	private String editors[];
	private JMenuItem menuItem;

	public void initialize(PlugInContext context) throws Exception {
		panel = new GeomarkPanel();
		editors = new String[1];
		editors[0] = Names.EDITOR_MAP_ID;
		menuItem = context.getFeatureInstaller().addMainMenuItem(this,
				new String[] { Names.VIEW }, Names.GEOMARK, true,
				getIcon(IconNames.GEOMARK_ICON), editors, panel, context);
		context.getFeatureInstaller().addRegisterCustomQuery(Geomark.class);
	}

	public boolean execute(PlugInContext context) throws Exception {
		getPlugInContext().loadView(getId());
		return true;
	}

	public boolean setEditor(IEditor editor) {
		panel.setEditor(editor);
		return true;
	}

	public boolean isEnabled() {		
		return true;
	}
	
	public boolean isSelected() {
		boolean isSelected = false;
		isSelected = getPlugInContext().viewIsOpen(getId());
		menuItem.setSelected(isSelected);
		return isSelected;
	}
	
	public String getName() {		
		return "Geomark view";
	}

}