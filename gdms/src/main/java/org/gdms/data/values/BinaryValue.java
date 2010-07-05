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
package org.gdms.data.values;

import org.gdms.data.types.Type;
import org.gdms.data.types.TypeFactory;
import org.gdms.sql.strategies.IncompatibleTypesException;

/**
 * This cass is intended to store binary values, as a table of bytes.
 */
class BinaryValue extends AbstractValue {
        //the set of values.
	private byte[] value;

	/**
         * Constructor
         * @param bytes : the table taht contains the values
         */
	BinaryValue(byte[] bytes) {
		value = bytes;
	}

	/**
	 * Create a new empty BinaryValue
	 */
	BinaryValue() {
	}

	/**
         * Return the contet of the Value as a String
         * @return the value as a String
         */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < value.length; i++) {
			byte b = value[i];
			String s = Integer.toHexString(b);
			if (s.length() == 1) {
				sb.append("0");
			}
			sb.append(s);
		}

		return sb.toString();
	}

	/**
         * Evaluate if <code>this</code> is equal to <code>value</code>.
         * Two BynaryValues are equal if an only if they contain exactly the same byte values.
         * @param value The value to be compared with
         * @return a BooleanValue, which is "true" if this and value are equals.
         * @throws IncompatibleTypesException if value's type is neither NullValue nor BinaryValue
         */
	public Value equals(Value value) throws IncompatibleTypesException {
		if (value instanceof NullValue) {
			return ValueFactory.createValue(false);
		}

		if (value instanceof BinaryValue) {
			BinaryValue bv = (BinaryValue) value;
			boolean ret = true;
			if (this.value.length != bv.value.length)
				ret = false;
			else {
				for (int i = 0; i < this.value.length; i++) {
					if (this.value[i] != bv.value[i]) {
						ret = false;
						break;
					}
				}
			}
			return ValueFactory.createValue(ret);
		} else {
			throw new IncompatibleTypesException(
					"The specified value is not a binary:"
							+ TypeFactory.getTypeName(value.getType()));
		}
	}

	/**
	 * @see org.gdms.data.values.Value#notEquals()
	 */
	public Value notEquals(Value value) throws IncompatibleTypesException {
		if (value instanceof NullValue) {
			return ValueFactory.createValue(false);
		}

		if (value instanceof BinaryValue) {
			return ValueFactory.createValue(!((BooleanValue) equals(value))
					.getValue());
		} else {
			throw new IncompatibleTypesException(
					"The specified value is not a binary:"
							+ TypeFactory.getTypeName(value.getType()));
		}
	}

	/**
	 * @see org.gdms.data.values.Value#doHashCode()
	 */
	public int doHashCode() {
		return value.hashCode();
	}

	/**
	 * @return the byte table
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * @see org.gdms.data.values.Value#getStringValue(org.gdms.data.values.ValueWriter)
	 */
	public String getStringValue(ValueWriter writer) {
		return writer.getStatementString(value);
	}

	/**
	 * @see org.gdms.data.values.Value#getType()
	 */
	public int getType() {
		return Type.BINARY;
	}
        /**
         *
         * @return the byte table that contains the byte values
         */
	public byte[] getBytes() {
		return value;
	}
        /**
         * Create a new BinaryValue withe the byte tabe buffer
         * @param buffer
         * @return a new BinaryValue as a Value.
         */
	public static Value readBytes(byte[] buffer) {
		return new BinaryValue(buffer);
	}
        /**
         *
         * @return The byte table
         * @throws IncompatibleTypesException
         */
	@Override
	public byte[] getAsBinary() throws IncompatibleTypesException {
		return value;
	}
}