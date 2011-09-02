/* 
 * TANATO  is a library dedicated to the modelling of water pathways based on 
 * triangulate irregular network. TANATO takes into account anthropogenic and 
 * natural artifacts to evaluate their impacts on the watershed response. 
 * It ables to compute watershed, main slope directions and water flow pathways.
 * 
 * This library has been originally created  by Erwan Bocher during his thesis 
 * “Impacts des activités humaines sur le parcours des écoulements de surface dans 
 * un bassin versant bocager : essai de modélisation spatiale. Application au 
 * Bassin versant du Jaudy-Guindy-Bizien (France)”. It has been funded by the 
 * Bassin versant du Jaudy-Guindy-Bizien and Syndicat d’Eau du Trégor.
 * 
 * The new version is developed at French IRSTV institut as part of the 
 * AvuPur project, funded by the French Agence Nationale de la Recherche 
 * (ANR) under contract ANR-07-VULN-01.
 * 
 * TANATO is distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 * Copyright (C) 2010 Erwan BOCHER, Alexis GUEGANNO, Jean-Yves MARTIN
 * Copyright (C) 2011 Erwan BOCHER, , Alexis GUEGANNO, Jean-Yves MARTIN
 * 
 * TANATO is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * TANATO is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * TANATO. If not, see <http://www.gnu.org/licenses/>.
 * 
 * For more information, please consult: <http://trac.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.gdms.sql.function.spatial.geometry.edit;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import org.gdms.data.SQLDataSourceFactory;
import org.gdms.data.types.Type;
import org.gdms.data.types.TypeFactory;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueFactory;
import org.gdms.geometryUtils.GeometryEdit;
import org.gdms.sql.function.AbstractScalarFunction;
import org.gdms.sql.function.BasicFunctionSignature;
import org.gdms.sql.function.FunctionException;
import org.gdms.sql.function.FunctionSignature;
import org.gdms.sql.function.ScalarArgument;

/**
 *
 * @author ebocher
 */
public class ST_LinearInterpolation extends AbstractScalarFunction {

        private GeometryFactory gf = new GeometryFactory();

        @Override
        public final Value evaluate(SQLDataSourceFactory dsf, Value... values) throws FunctionException {
                Geometry geom = values[0].getAsGeometry();
                if (geom instanceof MultiLineString) {
                        geom = GeometryEdit.linearZInterpolation((MultiLineString) geom);
                } else if (geom instanceof LineString) {
                        geom = GeometryEdit.linearZInterpolation((LineString) geom);
                }
                return ValueFactory.createValue(geom);

        }

        @Override
        public final String getName() {
                return "ST_LINEARINTERPOLATION";
        }

        @Override
        public final Type getType(Type[] types) {
                return TypeFactory.createType(Type.GEOMETRY);
        }

        @Override
        public final String getDescription() {
                return "Update the z coordinates of a geometry based on a linear interpolation between first and last coordinates.";
        }

        @Override
        public final String getSqlOrder() {
                return "SELECT ST_LINEARINTERPOLATION(the_geom) FROM table";
        }

        @Override
        public FunctionSignature[] getFunctionSignatures() {
                return new FunctionSignature[]{
                                new BasicFunctionSignature(Type.GEOMETRY, ScalarArgument.LINESTRING),
                                new BasicFunctionSignature(Type.GEOMETRY, ScalarArgument.MULTILINESTRING)};
        }
}