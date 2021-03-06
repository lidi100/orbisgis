/**
 * OrbisToolBox is an OrbisGIS plugin dedicated to create and manage processing.
 *
 * OrbisToolBox is distributed under GPL 3 license. It is produced by CNRS <http://www.cnrs.fr/> as part of the
 * MApUCE project, funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
 *
 * OrbisToolBox is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * OrbisToolBox is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with OrbisToolBox. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/> or contact directly: info_at_orbisgis.org
 */

package org.orbisgis.wpsgroovyapi.attributes

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Attribute for the additional metadata.
 *
 * The following fields must be defined (mandatory) :
 *  - title : String
 *       Title of the documentation. Normally available for display to a human.
 *  - role : String
 *      Role identifier, indicating the role of the linked document.
 *  - href : String
 *      Reference to a documentation site for a process, input, or output.
 *
 * The following fields can be defined (optional) :
 *  - linkType : String
 *      Role identifier, indicating the role of the linked document.
 *
 * @author Sylvain PALOMINOS
 */
@Retention(RetentionPolicy.RUNTIME)
@interface MetadataAttribute {

    /** Title of the documentation. Normally available for display to a human. */
    String title()

    /** Type of the xlink, fixed to simple. */
    String linkType() default "simple"

    /** Role identifier, indicating the role of the linked document. */
    String role()

    /** Reference to a documentation site for a process, input, or output. */
    String href()



    /********************/
    /** default values **/
    /********************/
    public static final String defaultLinkType = "simple"
}