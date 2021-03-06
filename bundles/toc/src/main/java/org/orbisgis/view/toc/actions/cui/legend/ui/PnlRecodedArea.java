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
package org.orbisgis.view.toc.actions.cui.legend.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.orbisgis.coremap.renderer.se.CompositeSymbolizer;
import org.orbisgis.coremap.renderer.se.Rule;
import org.orbisgis.legend.Legend;
import org.orbisgis.legend.structure.stroke.constant.NullPenStrokeLegend;
import org.orbisgis.legend.thematic.AreaParameters;
import org.orbisgis.legend.thematic.constant.UniqueSymbolArea;
import org.orbisgis.legend.thematic.recode.AbstractRecodedLegend;
import org.orbisgis.legend.thematic.recode.RecodedArea;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.UIPanel;
import org.orbisgis.view.toc.actions.cui.LegendContext;
import org.orbisgis.view.toc.actions.cui.components.CanvasSE;
import org.orbisgis.view.toc.actions.cui.legend.model.KeyEditorRecodedArea;
import org.orbisgis.view.toc.actions.cui.legend.model.ParametersEditorRecodedArea;
import org.orbisgis.view.toc.actions.cui.legend.model.TableModelRecodedArea;
import org.orbisgis.view.toc.actions.cui.legend.model.KeyEditorUniqueValue;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.EventHandler;

/**
 * "Value classification - Area" UI.
 *
 * <p></p>This panel must be used to manage all the parameters of an area
 * symbolizer which is configured thanks to a "simple" recoded PenStroke and a
 * "simple" recoded SolidFill. All the parameters of these symbolizer nodes
 * must be configured either with a Recode or a Literal, all the Recode must be
 * done with the same analysis field.</p>
 *
 * @author Alexis Guéganno
 * @author Adam Gouge
 */
public final class PnlRecodedArea extends PnlAbstractUniqueValue<AreaParameters>{
    public static final Logger LOGGER = LoggerFactory.getLogger(PnlRecodedLine.class);
    private static final I18n I18N = I18nFactory.getI18n(PnlRecodedLine.class);
    private String id;

    /**
     * Builds a panel with a new legend.
     *
     * @param lc     LegendContext
     */
    public PnlRecodedArea(LegendContext lc) {
        this(lc, new RecodedArea());
    }

    /**
     * Builds a panel based on the given legend.
     *
     * @param lc     LegendContext
     * @param legend Legend
     */
    public PnlRecodedArea(LegendContext lc, RecodedArea legend) {
        super(lc, legend);
        initPreview();
        buildUI();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    /**
     * This methods is called by EventHandler when the user clicks on the fall back's preview. It opens an UI that lets
     * the user edit the parameters of the fall back configuration and that apply it if the user clicks OK.
     * @param me The MouseEvent that caused the call to this method.
     */
    public void onEditFallback(MouseEvent me){
        getLegend().setFallbackParameters(editCanvas(fallbackPreview));
    }

    /**
     * Builds a SIF dialog used to edit the given LineParameters.
     * @param cse The canvas we want to edit
     * @return The LineParameters that must be used at the end of the edition.
     */
    private AreaParameters editCanvas(CanvasSE cse){
        RecodedArea leg = (RecodedArea) getLegend();
        AreaParameters lps = leg.getFallbackParameters();
        UniqueSymbolArea usa = new UniqueSymbolArea(lps);
        if(leg.isStrokeEnabled()){
            usa.setStrokeUom(leg.getStrokeUom());
        } else {
            usa.setStrokeLegend(new NullPenStrokeLegend());
        }
        PnlUniqueAreaSE pls = new PnlUniqueAreaSE(usa, leg.isStrokeEnabled());
        if(UIFactory.showDialog(new UIPanel[]{pls}, true, true)){
            usa = pls.getLegend();
            AreaParameters nlp = usa.getAreaParameters();
            cse.setSymbol(usa.getSymbolizer());
            return nlp;
        } else {
            return lps;
        }
    }

    @Override
    public void initPreview() {
        fallbackPreview = new CanvasSE(getFallbackSymbolizer());
        MouseListener l = EventHandler.create(MouseListener.class, this, "onEditFallback", "", "mouseClicked");
        fallbackPreview.addMouseListener(l);
    }

    @Override
    public AreaParameters getColouredParameters(AreaParameters f, Color c) {
        return new AreaParameters(f.getLineColor(), f.getLineOpacity(),f.getLineWidth(),f.getLineDash(),c,f.getFillOpacity());
    }

    @Override
    public AbstractTableModel getTableModel(){
        return new TableModelRecodedArea((AbstractRecodedLegend<AreaParameters>) getLegend());
    }

    @Override
    public TableCellEditor getPreviewCellEditor(){
        return new ParametersEditorRecodedArea();
    }

    @Override
    public KeyEditorUniqueValue<AreaParameters> getKeyCellEditor(){
        return new KeyEditorRecodedArea();
    }

    @Override
    public RecodedArea getEmptyAnalysis() {
        RecodedArea ra = new RecodedArea();
        RecodedArea old = (RecodedArea)getLegend();
        if(old != null){
            ra.setStrokeEnabled(old.isStrokeEnabled());
        }
        return ra;
    }

    @Override
    public void setLegend(Legend legend) {
        if (legend instanceof RecodedArea) {
            if(getLegend() != null){
                Rule rule = getLegend().getSymbolizer().getRule();
                if(rule != null){
                    CompositeSymbolizer compositeSymbolizer = rule.getCompositeSymbolizer();
                    int i = compositeSymbolizer.getSymbolizerList().indexOf(this.getLegend().getSymbolizer());
                    compositeSymbolizer.setSymbolizer(i, legend.getSymbolizer());
                }
            }
            setLegendImpl((RecodedArea)legend);
            this.buildUI();
        } else {
            throw new IllegalArgumentException(I18N.tr("You must use recognized RecodedArea instances in "
                        + "this panel."));
        }
    }
}
