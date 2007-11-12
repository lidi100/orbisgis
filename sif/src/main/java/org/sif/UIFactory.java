package org.sif;

import java.awt.Frame;
import java.awt.Window;
import java.net.URL;
import java.util.HashMap;

public class UIFactory {

	private static HashMap<String, String> inputs = new HashMap<String, String>();

	public static SIFDialog getSimpleDialog(UIPanel panel) {
		return getSimpleDialog(panel, null);
	}

	public static SIFDialog getSimpleDialog(UIPanel panel, Window owner) {
		SIFDialog dlg = new SIFDialog(owner);
		SimplePanel simplePanel = new SimplePanel(dlg, panel);
		dlg.setComponent(simplePanel, inputs);
		return dlg;
	}

	public static DynamicUIPanel getDynamicUIPanel(String title, URL icon,
			String[] names) {
		return getDynamicUIPanel(null, title, icon, names, new int[0],
				new String[0], new String[0]);
	}

	public static DynamicUIPanel getDynamicUIPanel(String title, URL icon,
			String[] names, int[] types, String[] expressions,
			String[] errorMsgs) {
		return getDynamicUIPanel(null, title, icon, names, types, expressions,
				errorMsgs);
	}

	public static DynamicUIPanel getDynamicUIPanel(String id, String title,
			URL icon, String[] names) {
		return new DynamicUIPanel(id, title, icon, names, new int[0],
				new String[0], new String[0]);
	}

	public static DynamicUIPanel getDynamicUIPanel(String id, String title,
			URL icon, String[] names, int[] types, String[] expressions,
			String[] errorMsgs) {
		return new DynamicUIPanel(id, title, icon, names, types, expressions,
				errorMsgs);
	}

	public static SIFWizard getWizard(UIPanel[] panels) {
		return getWizard(panels, null);
	}

	private static SIFWizard getWizard(UIPanel[] panels, Frame owner) {
		SIFWizard dlg = new SIFWizard(owner);
		SimplePanel[] simplePanels = new SimplePanel[panels.length];
		for (int i = 0; i < simplePanels.length; i++) {
			simplePanels[i] = new SimplePanel(dlg, panels[i]);
		}
		dlg.setComponent(simplePanels, inputs);
		return dlg;
	}

	public static boolean showDialog(UIPanel[] panels) {
		AbstractOutsideFrame dlg;
		if (panels.length == 0) {
			throw new IllegalArgumentException(
					"At least a panel has to be specified");
		} else if (panels.length == 1) {
			dlg = getSimpleDialog(panels[0]);
		} else {
			dlg = getWizard(panels);
		}
		dlg.setModal(true);
		dlg.pack();
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);

		return dlg.isAccepted();
	}

	public static boolean showDialog(UIPanel panel) {
		return showDialog(new UIPanel[] { panel });
	}

	public static void setInputFor(String id, String inputName) {
		inputs.put(id, inputName);
	}
}
