/**
 * OrbisToolBox is an OrbisGIS plugin dedicated to create and manage processing.
 * <p/>
 * OrbisToolBox is distributed under GPL 3 license. It is produced by CNRS <http://www.cnrs.fr/> as part of the
 * MApUCE project, funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
 * <p/>
 * OrbisToolBox is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p/>
 * OrbisToolBox is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with OrbisToolBox. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p/>
 * For more information, please consult: <http://www.orbisgis.org/> or contact directly: info_at_orbisgis.org
 */

package org.orbisgis.orbistoolbox.controller.parser;

import org.orbisgis.orbistoolbox.model.*;
import org.orbisgis.orbistoolboxapi.annotations.model.DescriptionTypeAttribute;
import org.orbisgis.orbistoolboxapi.annotations.model.InputAttribute;
import org.orbisgis.orbistoolboxapi.annotations.model.OutputAttribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Default parse. This Parser is able to parse any input or output as a raw data, literal data or bounding box.
 * But the DataDescription returned is basic.
 *
 * @author Sylvain PALOMINOS
 **/

public class DefaultParser implements Parser {
    @Override
    public Input parseInput(Field f, String processName) {
        DataDescription data;
        List<Format> formatList = new ArrayList<>();
        List<LiteralDataDomain> lddList = new ArrayList<>();

        //Create a format list with an empty format
        Format format = new Format("no/mimetype", URI.create("no/mimetype"));
        format.setDefaultFormat(true);
        formatList.add(format);

        //Check if the type of the field is Boolean, Character, Byte ... to instantiate a LiteralData
        if(f.getType().equals(Boolean.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Value<>(true));
            valueList.add(new Value<>(false));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.BOOLEAN, new Value<>(false)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Character.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Character.MIN_VALUE, Character.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.UNSIGNED_BYTE, new Value<>(' ')));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Byte.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Byte.MIN_VALUE, Byte.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.BYTE, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Short.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Short.MIN_VALUE, Short.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.SHORT, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Integer.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.INTEGER, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Long.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Long.MIN_VALUE, Long.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.LONG, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Float.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Float.MIN_VALUE, Float.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.FLOAT, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Double.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Double.MIN_VALUE, Double.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.DOUBLE, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(String.class)){
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice();
            lddList.add(new LiteralDataDomain(plvc, DataType.STRING, new Value<>("")));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        //If the field can not be parsed as a LiteralData, parse it as a RawData
        else{
            RawData rawData = new RawData(formatList);
            rawData.setData(f, f.getType());
            data = rawData;
        }

        //Instantiate the returned input
        Input input = new Input(f.getName(),
                URI.create("orbisgis:wps:"+processName+":input:"+f.getName()),
                data);
        input.setMinOccurs(0);
        input.setMaxOccurs(1);
        input.setMinOccurs(1);

        return input;
    }

    @Override
    public Output parseOutput(Field f, String processName) {
        DataDescription data;
        List<Format> formatList = new ArrayList<>();
        List<LiteralDataDomain> lddList = new ArrayList<>();

        //Create a format list with an empty format
        Format format = new Format("no/mimetype", URI.create("no/mimetype"));
        format.setDefaultFormat(true);
        formatList.add(format);

        //Check if the type of the field is Boolean, Character, Byte ... to instantiate a LiteralData
        if(f.getType().equals(Boolean.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Value<>(true));
            valueList.add(new Value<>(false));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.BOOLEAN, new Value<>(false)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Character.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Character.MIN_VALUE, Character.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.UNSIGNED_BYTE, new Value<>(' ')));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Byte.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Byte.MIN_VALUE, Byte.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.BYTE, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Short.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Short.MIN_VALUE, Short.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.SHORT, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Integer.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.INTEGER, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Long.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Long.MIN_VALUE, Long.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.LONG, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Float.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Float.MIN_VALUE, Float.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.FLOAT, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(Double.class)){
            List<Values> valueList = new ArrayList<>();
            valueList.add(new Range(Double.MIN_VALUE, Double.MAX_VALUE, 1));
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice(valueList);
            lddList.add(new LiteralDataDomain(plvc, DataType.DOUBLE, new Value<>(0)));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        else if(f.getType().equals(String.class)){
            PossibleLiteralValuesChoice plvc = new PossibleLiteralValuesChoice();
            lddList.add(new LiteralDataDomain(plvc, DataType.STRING, new Value<>("")));

            LiteralValue literalValue = new LiteralValue();

            data = new LiteralData(formatList, lddList, literalValue);
        }
        //If the field can not be parsed as a LiteralData, parse it as a RawData
        else{
            RawData rawData = new RawData(formatList);
            rawData.setData(f, f.getType());
            data = rawData;
        }
        //Instantiate the returned output
        Output output = new Output(f.getName(),
                URI.create("orbisgis:wps:"+processName+":output:"+f.getName()),
                data);

        return output;
    }

    @Override
    public Class<? extends Annotation> getAnnotationInput() {
        return org.orbisgis.orbistoolboxapi.annotations.input.Input.class;
    }

    @Override
    public Class<? extends Annotation> getAnnotationOutput() {
        return org.orbisgis.orbistoolboxapi.annotations.output.Output.class;
    }
}
