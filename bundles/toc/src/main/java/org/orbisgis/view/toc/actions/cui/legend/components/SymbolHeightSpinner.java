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
package org.orbisgis.view.toc.actions.cui.legend.components;

import org.orbisgis.legend.thematic.constant.UniqueSymbolPoint;
import org.orbisgis.view.toc.actions.cui.components.CanvasSE;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.beans.EventHandler;

/**
 * Spinner for symbol height.
 *
 * @author Adam Gouge
 */
public class SymbolHeightSpinner extends PreviewSpinner {

    /**
     * Constructor
     *
     * @param legend  Legend
     * @param preview Preview
     */
    public SymbolHeightSpinner(final UniqueSymbolPoint legend,
                               CanvasSE preview) {
        super(new SpinnerNumberModel(
                (legend.getViewBoxHeight() == null)
                        ? legend.getViewBoxWidth().doubleValue()
                        : legend.getViewBoxHeight().doubleValue(),
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SMALL_STEP),
                preview);
        addChangeListener(EventHandler.create(
                ChangeListener.class, legend, "viewBoxHeight", "source.value"));
    }
}
