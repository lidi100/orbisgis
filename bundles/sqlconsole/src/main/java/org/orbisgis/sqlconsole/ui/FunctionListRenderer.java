/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
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
package org.orbisgis.sqlconsole.ui;

import org.orbisgis.sif.components.renderers.ListLaFRenderer;
import org.orbisgis.sqlconsole.icons.SQLConsoleIcon;

import javax.swing.*;
import java.awt.*;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * Class to improve the function list rendering. Add icons corresponding to
 * FunctionElement type.
 *
 * @author Erwan Bocher
 */
public class FunctionListRenderer extends ListLaFRenderer<FunctionElement> {
        private static final long serialVersionUID = 1L;

        public static final int TOOLTIP_WIDTH_PX = 100;
        protected final static I18n I18N = I18nFactory.getI18n(FunctionListRenderer.class);

        public FunctionListRenderer(JList list) {
                super(list);
        }

        /**
         * Display an icon for the function
         * @return 
         */
        private static Icon getFunctionIcon() {
                return SQLConsoleIcon.getIcon("builtinfunctionmap");
        }
        
        @Override
        public Component getListCellRendererComponent(JList<? extends FunctionElement> jlist, FunctionElement sqlFunction, int index, boolean isSelected, boolean cellHasFocus) {
                Component nativeCell = lookAndFeelRenderer.getListCellRendererComponent(jlist, sqlFunction, index, isSelected, cellHasFocus);
                if(nativeCell instanceof JLabel) {
                        JLabel renderingComp = (JLabel) nativeCell;
                        renderingComp.setIcon(getFunctionIcon());
                        renderingComp.setText(sqlFunction.getFunctionName());
                        renderingComp.setToolTipText("<html><body><p style='width: " + TOOLTIP_WIDTH_PX + "px;'>"
                                + I18N.tr("Drag and drop the function to the SQL editor.")
                                + "</p></body></html>");
                }
                return nativeCell;
        }
}
