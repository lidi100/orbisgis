/**
 * OrbisGIS is a GIS application dedicated to scientific spatial analysis.
 * This cross-platform GIS is developed at the Lab-STICC laboratory by the DECIDE
 * team located in University of South Brittany, Vannes.
 *
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 CNRS (UMR CNRS 6285)
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

package org.orbisgis.wpsgroovyapi.process

import groovy.transform.AnnotationCollector
import org.orbisgis.wpsgroovyapi.attributes.DescriptionTypeAttribute
import org.orbisgis.wpsgroovyapi.attributes.ProcessAttribute

/**
 * Groovy Process annotation.
 * This annotation is placed just before the 'processing' groovy method to declare the process information and
 * its main method
 *
 * The following fields must be defined (mandatory) :
 *  - title : String
 *       Title of the process. Normally available for display to a human.
 *
 * The following fields can be defined (optional) :
 *  - resume : String
 *      Brief narrative description of the process. Normally available for display to a human..
 *  - keywords : String
 *      Coma separated keywords that characterize the process.
 *  - identifier : String
 *      Unambiguous identifier of the process. It should be a valid URI.
 *  - metadata : MetaData[]
 *      Reference to additional metadata about this item.
 *  - language : String
 *      Language of the process title and abstract.
 *
 * Usage example can be found at https://github.com/orbisgis/orbisgis/wiki/
 *
 * @author Sylvain PALOMINOS
 */
@AnnotationCollector([ProcessAttribute, DescriptionTypeAttribute])
public @interface Process {}