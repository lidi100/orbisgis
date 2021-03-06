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
package org.orbisgis.postgis_jts;

import org.postgresql.util.PGobject;

/**
 * PostGRE driver forgets to override hashCode
 * Necessary until https://github.com/pgjdbc/pgjdbc/pull/181 is merged
 */
public class PGObjectWrapper {
    private PGobject pGobject;

    public PGObjectWrapper(PGobject pGobject) {
        this.pGobject = pGobject;
    }

    @Override
    public int hashCode() {
        return pGobject.getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PGObjectWrapper && pGobject.equals(((PGObjectWrapper) obj).getPGobject());
    }

    @Override
    public String toString() {
        return pGobject.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new PGObjectWrapper((PGobject)pGobject.clone());
    }

    /**
     * @return Wrapped PGObject
     */
    public PGobject getPGobject() {
        return pGobject;
    }
}
