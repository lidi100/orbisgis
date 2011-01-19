/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 * 
 *  
 *  Lead Erwan BOCHER, scientific researcher, 
 *
 *  Developer lead : Pierre-Yves FADET, computer engineer. 
 *  
 *  User support lead : Gwendall Petit, geomatic engineer. 
 * 
 * Previous computer developer : Thomas LEDUC, scientific researcher, Fernando GONZALEZ
 * CORTES, computer engineer.
 * 
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 * 
 * Copyright (C) 2010 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
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
 * For more information, please consult: <http://orbisgis.cerma.archi.fr/>
 * <http://sourcesup.cru.fr/projects/orbisgis/>
 * 
 * or contact directly: 
 * erwan.bocher _at_ ec-nantes.fr 
 * Pierre-Yves.Fadet _at_ ec-nantes.fr
 * gwendall.petit _at_ ec-nantes.fr
 **/

package org.orbisgis.core.ui.plugins.views.beanShellConsole.actions;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.orbisgis.core.ui.preferences.lookandfeel.images.IconLoader;
import org.orbisgis.utils.I18N;

public class BshConsoleAction {
	public final static int EXECUTE = 110;
	public final static int CLEAR = 111;
	public final static int OPEN = 115;
	public final static int SAVE = 116;

	private static class InternalConsoleAction {
		ImageIcon icon;
		String toolTipText;

		InternalConsoleAction(final String icon, final String toolTipText) {
			this.icon = IconLoader.getIcon(icon);
			this.toolTipText = toolTipText;
		}
	}

	private static Map<Integer, InternalConsoleAction> mapOfActions;

	static {
		mapOfActions = new HashMap<Integer, InternalConsoleAction>(7);

		mapOfActions.put(EXECUTE, new InternalConsoleAction("execute.png", //$NON-NLS-1$
				I18N.getString("orbisgis.org.orbisgis.ui.bshConsoleAction.execute"))); //$NON-NLS-1$
		mapOfActions.put(CLEAR, new InternalConsoleAction("erase.png", //$NON-NLS-1$
				I18N.getString("orbisgis.org.orbisgis.ui.bshConsoleAction.clear"))); //$NON-NLS-1$
		mapOfActions.put(OPEN, new InternalConsoleAction("open.png", //$NON-NLS-1$
				I18N.getString("orbisgis.org.orbisgis.ui.bshConsoleAction.open"))); //$NON-NLS-1$
		mapOfActions.put(SAVE, new InternalConsoleAction("save.png", //$NON-NLS-1$
				I18N.getString("orbisgis.org.orbisgis.ui.bshConsoleAction.save"))); //$NON-NLS-1$
	}

	public static ImageIcon getImageIcon(final int type) {
		return mapOfActions.get(type).icon;
	}

	public static String getToolTipText(final int type) {
		return mapOfActions.get(type).toolTipText;
	}
}