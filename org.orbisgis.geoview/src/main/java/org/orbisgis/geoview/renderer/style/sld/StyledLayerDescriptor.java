/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at french IRSTV institute and is able
 * to manipulate and create vectorial and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geomatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OrbisGIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.orbisgis.geoview.renderer.style.sld;

import org.orbisgis.pluginManager.VTD;

/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002-2006, GeoTools Project Managment Committee (PMC)
 *    (C) 2002, Centre for Computational Geography
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */



/**
 * Holds styling information (from a StyleLayerDescriptor document).
 *
 * <p>
 * This interface is bound to version 1.0 of the SLD specification.
 * </p>
 *
 * <p>
 * For many of us in geotools this is the reason we came along for the ride - a
 * pretty picture. For documentation on the use of this class please consult
 * the SLD 1.0 specification.
 * </p>
 *
 * <p>
 * We may experiment with our own (or SLD 1.1) ideas but will mark such
 * experiments for you. This is only an issue of you are considering writing
 * out these objects for interoptability with other systems.
 * </p>
 *
 * <p>
 * General stratagy for supporting multiple SLD versions (and experiments):
 *
 * <ul>
 * <li>
 * These interfaces will reflect the current published specification
 * </li>
 * <li>
 * Our implementations will be <b>BIGGER</b> and more capabile then any one
 * specification
 * </li>
 * <li>
 * We cannot defined explicit interfaces tracking each version until we move to
 * Java 5 (perferably GeoAPI would hold these anyways)
 * </li>
 * <li>
 * We can provided javadocs indicating extentions, and isolate these using the
 * normal java convention: TextSymbolizer and TextSymbolizer2 (In short you
 * will have to go out of your way to work with a hack or experiment, you
 * won't depend on one by accident)
 * </li>
 * <li>
 * We can use Factories (aka SLD1Factory and SLD1_1Factory and SEFactory) to
 * support the creation of conformant datastructures. Code (such as user
 * interfaces) can be parameratized with these factories when they need to
 * confirm to an exact version supported by an individual service. We hope
 * that specifications are always adative, and will be forced to throw
 * unsupported exceptions when functionality is removed from a specification.
 * </li>
 * </ul>
 * </p>
 *
 * @author Ian Turton, CCG
 * @author James Macgill, CCG
 * @author Jody Garnett, Refractions Research
 * @author bocher, IRSTV
 * @source $URL: http://svn.geotools.org/geotools/tags/2.3.3/module/api/src/org/geotools/styling/StyledLayerDescriptor.java $
 * @version SLD 1.1
 *
 * @since GeoTools 2.0
 */


public class StyledLayerDescriptor {
   
	
	/*
	 * To be complete and use
	 */
	
	/**
	 * OCG SLD 1.0
	 * 
	 * 
	 * A StyledLayerDescriptor is a sequence of styled layers, represented at the first level by NamedLayer and UserLayer elements.
		
		
		<xs:element name="StyledLayerDescriptor">
  <xs:complexType>
    <xs:choice minOccurs="0" maxOccurs="unbounded">
      <xs:element ref="sld:NamedLayer"/>
      <xs:element ref="sld:UserLayer"/>
    </xs:choice>
    <xs:attribute name="version" type="xs:string"
                   use="required"/>
  </xs:complexType>
</xs:element>


	 */
	
	
	private VTD vtd;
	private String rootXpathQuery;

	public StyledLayerDescriptor (VTD vtd, String rootXpathQuery){
		this.vtd = vtd;
		this.rootXpathQuery = rootXpathQuery;
		
	}

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName(){
		return null;
    	
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(String name){
    	
    }

    /**
     * Getter for property title.
     *
     * @return Value of property title.
     */
    public String getTitle() {
    	return null;
    }

    /**
     * Setter for property title.
     *
     * @param title New value of property title.
     */
    public void setTitle(String title){
    	
    }

    /**
     * Getter for property abstractStr.
     *
     * @return Value of property abstractStr.
     */
    public java.lang.String getAbstract() {
    	return null;
    }

    /**
     * Setter for property abstractStr.
     *
     * @param abstractStr New value of property abstractStr.
     */
    public void setAbstract(String abstractStr) {
    	
    }
    
}
