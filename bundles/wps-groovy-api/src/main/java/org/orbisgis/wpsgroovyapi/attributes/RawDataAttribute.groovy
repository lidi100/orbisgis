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

package org.orbisgis.wpsgroovyapi.attributes

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Attributes for the RawData complex data.
 * The RawData is a complex data that represents a file or directory.
 *
 * The following fields can be defined (optional) :
 *  - isDirectory : boolean
 *      Indicates that the RawData can be a directory.
 *  - isFile : boolean
 *      Indicates that the RawData can be a file.
 *  - multiSelection : boolean
 *      Indicates that the user can select more than one file/directory.
 *
 * @author Sylvain PALOMINOS
 */
@Retention(RetentionPolicy.RUNTIME)
@interface RawDataAttribute {
    /** Indicates that the RawData can be a directory. */
    boolean isDirectory() default true
    /** Indicates that the RawData can be a file. */
    boolean isFile() default true
    /** Indicates that the user can select more than one file/directory. */
    boolean multiSelection() default false



    /********************/
    /** default values **/
    /********************/
    public static final boolean defaultIsDirectory = true
    public static final boolean defaultIsFile = true
    public static final boolean defaultMultiSelection = false
}