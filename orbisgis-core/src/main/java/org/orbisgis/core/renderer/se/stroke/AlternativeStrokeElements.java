/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 *
 *  Team leader Erwan BOCHER, scientific researcher,
 *
 *  User support leader : Gwendall Petit, geomatic engineer.
 *
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Pierre-Yves FADET, Alexis GUEGANNO, Maxence LAURENT
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
 *
 * or contact directly:
 * erwan.bocher _at_ ec-nantes.fr
 * gwendall.petit _at_ ec-nantes.fr
 */
package org.orbisgis.core.renderer.se.stroke;

import java.util.ArrayList;
import java.util.List;
import net.opengis.se._2_0.core.AlternativeStrokeElementsType;
import net.opengis.se._2_0.core.StrokeElementType;
import org.orbisgis.core.renderer.se.SeExceptions.InvalidStyle;

/**
 * {@code AlternativeStrokeElements} provides the option for the rendering system to 
 * choose from different {@code StrokeElement} definitions.</p>
 * <p>Instances of this class contains a list of {@code StrokElement} that is used
 * to provide the said options.
 * @author maxence
 */
public class AlternativeStrokeElements extends CompoundStrokeElement {

	private List<StrokeElement> elements;


    public AlternativeStrokeElements(){
		elements = new ArrayList<StrokeElement>();
    }


	public AlternativeStrokeElements(AlternativeStrokeElementsType aset) throws InvalidStyle {
        this();
		for (StrokeElementType se : aset.getStrokeElement()) {
            StrokeElement strokeElement = new StrokeElement(se);
            strokeElement.setParent(this);
			elements.add(strokeElement);
		}
        if (elements.isEmpty()){
            throw new InvalidStyle("Alternative Stroke Element must, at least, contains one stroke element");
        }
	}

	@Override
	public Object getJaxbType() {
		AlternativeStrokeElementsType aset = new AlternativeStrokeElementsType();

		List<StrokeElementType> strokeElement = aset.getStrokeElement();

		for (StrokeElement elem : this.elements) {
			strokeElement.add((StrokeElementType) elem.getJaxbType());
		}

		return aset;
	}

	@Override
	public String dependsOnFeature() {
        String result = " ";
		for (StrokeElement elem : elements) {
            result += elem.dependsOnFeature();
		}
		return result.trim();
	}

    public List<StrokeElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<StrokeElement> elements) {
        this.elements = elements;
    }
}