/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 *
 * Team leader : Erwan BOCHER, scientific researcher,
 *
 * User support leader : Gwendall Petit, geomatic engineer.
 *
 * Previous computer developer : Pierre-Yves FADET, computer engineer, Thomas LEDUC, 
 * scientific researcher, Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Alexis GUEGANNO, Maxence LAURENT, Antoine GOURLAY
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
 * info@orbisgis.org
 */
package org.gdms.sql.function.alphanumeric;

import org.apache.log4j.Logger;
import org.gdms.data.SQLDataSourceFactory;
import org.gdms.data.types.Type;
import org.gdms.data.types.TypeFactory;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueFactory;
import org.gdms.sql.function.AbstractAggregateFunction;
import org.gdms.sql.function.ScalarArgument;
import org.gdms.sql.function.FunctionException;
import org.gdms.sql.function.FunctionSignature;
import org.gdms.sql.function.SameTypeFunctionSignature;
import org.gdms.data.types.IncompatibleTypesException;

/**
 * This aggregated function computes the average of the numeric value passed as argument.
 * 
 */
public class Average extends AbstractAggregateFunction {

        private static final Value NULLVALUE = ValueFactory.createNullValue();
        private double sumOfValues = 0;
        private int numberOfValues = 0;
        private static final Logger LOG = Logger.getLogger(Average.class);

        @Override
        public void evaluate(SQLDataSourceFactory dsf, Value... args) throws FunctionException {
                LOG.trace("Evaluating");
                if (!args[0].isNull()) {
                        try {
                                sumOfValues += args[0].getAsDouble();
                        } catch (IncompatibleTypesException e) {
                                throw new FunctionException(
                                        "Cannot operate on non-numeric fields", e);
                        }
                        numberOfValues++;
                }
        }

        @Override
        public String getName() {
                return "Avg";
        }

        @Override
        public String getDescription() {
                return "Calculate the average value";
        }

        @Override
        public String getSqlOrder() {
                return "select Avg(myNumericField) from myTable;";
        }

        @Override
        public Type getType(Type[] argsTypes) {
                return TypeFactory.createType(Type.DOUBLE);
        }

        @Override
        public Value getAggregateResult() {
                LOG.trace("Returning aggregated results");
                if (0 == numberOfValues) {
                        return NULLVALUE;
                }
                return ValueFactory.createValue(sumOfValues / numberOfValues);
        }

        @Override
        public FunctionSignature[] getFunctionSignatures() {
                return new FunctionSignature[]{new SameTypeFunctionSignature(ScalarArgument.DOUBLE)};
        }
}