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

package org.orbisgis.wpsservice.controller.utils;

import org.orbisgis.wpsgroovyapi.attributes.*;
import org.orbisgis.wpsservice.model.*;
import org.orbisgis.wpsservice.model.Process;
import org.slf4j.LoggerFactory;

import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Class able to convert annotation into object and object into annotation.
 *
 * @author Sylvain PALOMINOS
 **/

public class ObjectAnnotationConverter {

    public static void annotationToObject(DescriptionTypeAttribute descriptionTypeAttribute,
                                          DescriptionType descriptionType){
        try {
            if(!descriptionTypeAttribute.title().equals("")){
                    descriptionType.setTitle(descriptionTypeAttribute.title());
            }
            if(!descriptionTypeAttribute.resume().equals(DescriptionTypeAttribute.defaultResume)){
                descriptionType.setResume(descriptionTypeAttribute.resume());
            }
            if(!descriptionTypeAttribute.identifier().equals(DescriptionTypeAttribute.defaultIdentifier)){
                descriptionType.setIdentifier(URI.create(descriptionTypeAttribute.identifier()));
            }
            if(!descriptionTypeAttribute.keywords().equals(DescriptionTypeAttribute.defaultKeywords)){
                descriptionType.setKeywords(Arrays.asList(descriptionTypeAttribute.keywords().split(",")));
            }
            //TODO : implements for metadata.
            if(!descriptionTypeAttribute.metadata().equals(DescriptionTypeAttribute.defaultMetadata)){
                List<Metadata> metadatas = new ArrayList<>();
                for(MetadataAttribute metadata : descriptionTypeAttribute.metadata()) {
                    metadatas.add(ObjectAnnotationConverter.annotationToObject(metadata));
                }
                descriptionType.setMetadata(metadatas);
            }
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
        }
    }

    public static Format annotationToObject(FormatAttribute formatAttribute){
        try {
            Format format = new Format(formatAttribute.mimeType(), URI.create(formatAttribute.schema()));
            format.setDefaultFormat(formatAttribute.isDefaultFormat());
            format.setMaximumMegaBytes(formatAttribute.maximumMegaBytes());
            return format;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static Metadata annotationToObject(MetadataAttribute descriptionTypeAttribute){
        try {
            URI href = URI.create(descriptionTypeAttribute.href());
            URI role = URI.create(descriptionTypeAttribute.role());
            String title = descriptionTypeAttribute.title();

            return new Metadata(title, role, href);
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static Values annotationToObject(ValuesAttribute valueAttribute){
        try {
            Values value;
            if(valueAttribute.type().equals(ValuesType.VALUE)){
                value = new Value<>(valueAttribute.value());
            }
            else{
                if(valueAttribute.spacing().equals("")) {
                    value = new Range(
                                    Double.parseDouble(valueAttribute.minimum()),
                                    Double.parseDouble(valueAttribute.maximum())
                    );
                }
                else{
                    value = new Range(
                                    Double.parseDouble(valueAttribute.minimum()),
                                    Double.parseDouble(valueAttribute.maximum()),
                                    Double.parseDouble(valueAttribute.spacing())
                    );
                }
            }
            return value;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static LiteralDataDomain annotationToObject(LiteralDataDomainAttribute literalDataDomainAttribute){
        try {
            PossibleLiteralValuesChoice possibleLiteralValuesChoice = ObjectAnnotationConverter.annotationToObject(
                    literalDataDomainAttribute.possibleLiteralValues());

            DataType dataType = DataType.valueOf(literalDataDomainAttribute.dataType());

            Values defaultValue = ObjectAnnotationConverter.annotationToObject(literalDataDomainAttribute.defaultValue());

            LiteralDataDomain literalDataDomain = new LiteralDataDomain(
                    possibleLiteralValuesChoice,
                    dataType,
                    defaultValue
            );

            literalDataDomain.setDefaultDomain(literalDataDomainAttribute.isDefault());

            return literalDataDomain;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static LiteralData annotationToObject(LiteralDataAttribute literalDataAttribute) {
        try {
            List<Format> formatList = new ArrayList<>();
            if(literalDataAttribute.formats().length == 0){
                formatList.add(new Format("undefined", URI.create("undefined")));
                formatList.get(0).setDefaultFormat(true);
            }
            else {
                for (FormatAttribute formatAttribute : literalDataAttribute.formats()) {
                    formatList.add(ObjectAnnotationConverter.annotationToObject(formatAttribute));
                }
            }

            List<LiteralDataDomain> lddList = new ArrayList<>();
            if(literalDataAttribute.validDomains().length == 0){
                lddList.add(new LiteralDataDomain(new PossibleLiteralValuesChoice(), DataType.STRING, new Value<String>("")));
            }
            else {
                for (LiteralDataDomainAttribute literalDataDomainAttribute : literalDataAttribute.validDomains()) {
                    lddList.add(ObjectAnnotationConverter.annotationToObject(literalDataDomainAttribute));
                }
            }

            LiteralValue literalValue = null;
            try {
                literalValue = ObjectAnnotationConverter.annotationToObject(literalDataAttribute.valueAttribute());
            }
            catch(IncompleteAnnotationException e){
                literalValue = new LiteralValue();
            }

            return new LiteralData(formatList, lddList, literalValue);
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static RawData annotationToObject(RawDataAttribute rawDataAttribute, Format format) {
        try {
            //Instantiate the RawData
            format.setDefaultFormat(true);
            RawData rawData = new RawData(format);
            rawData.setFile(rawDataAttribute.isFile());
            rawData.setDirectory(rawDataAttribute.isDirectory());
            rawData.setMultiSelection(rawDataAttribute.multiSelection());
            return rawData;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static LiteralValue annotationToObject(LiteralValueAttribute literalValueAttribute){
        LiteralValue literalValue = new LiteralValue();
        if(!literalValueAttribute.uom().equals(LiteralValueAttribute.defaultUOM)) {
            literalValue.setUom(URI.create(literalValueAttribute.uom()));
        }
        if(!literalValueAttribute.dataType().equals(LiteralValueAttribute.defaultDataType)) {
            literalValue.setDataType(DataType.valueOf(literalValueAttribute.dataType()));
        }
        return literalValue;
    }

    public static void annotationToObject(InputAttribute inputAttribute, Input input){
        input.setMinOccurs(0);
        input.setMaxOccurs(inputAttribute.maxOccurs());
        input.setMinOccurs(inputAttribute.minOccurs());
    }

    public static PossibleLiteralValuesChoice annotationToObject(
            PossibleLiteralValuesChoiceAttribute possibleLiteralValuesChoiceAttribute){
        try {
            PossibleLiteralValuesChoice possibleLiteralValuesChoice = null;
            if(possibleLiteralValuesChoiceAttribute.allowedValues().length != 0){
                List<Values> valuesList = new ArrayList<>();
                for(ValuesAttribute va : possibleLiteralValuesChoiceAttribute.allowedValues()){
                    valuesList.add(ObjectAnnotationConverter.annotationToObject(va));
                }
                possibleLiteralValuesChoice = new PossibleLiteralValuesChoice(valuesList);
            }
            else{
                possibleLiteralValuesChoice = new PossibleLiteralValuesChoice();
            }
            return possibleLiteralValuesChoice;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static void annotationToObject(ProcessAttribute processAttribute, Process process){
        process.setLanguage(Locale.forLanguageTag(processAttribute.language()));
    }

    public static DataStore annotationToObject(DataStoreAttribute dataStoreAttribute, List<Format> formatList) {
        try {
            DataStore dataStore = new DataStore(formatList);
            dataStore.setAutoImport(dataStoreAttribute.isCreateTable());
            dataStore.setIsSpatial(dataStoreAttribute.isSpatial());
            return dataStore;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static DataField annotationToObject(DataFieldAttribute dataFieldAttribute, Format format, URI dataStoreUri) {
        try {
            format.setDefaultFormat(true);
            List<DataType> dataTypeList = new ArrayList<>();
            //For each field type value from the groovy annotation, test if it is contain in the FieldType enumeration.
            for(String type : Arrays.asList(dataFieldAttribute.fieldTypes())){
                dataTypeList.add(DataType.getDataTypeFromFieldType(type));
            }
            List<DataType> excludedTypeList = new ArrayList<>();
            //For each excluded type value from the groovy annotation, test if it is contain in the FieldType enumeration.
            for(String type : Arrays.asList(dataFieldAttribute.excludedTypes())){
                excludedTypeList.add(DataType.getDataTypeFromFieldType(type));
            }
            DataField dataField = new DataField(format, dataTypeList, dataStoreUri);
            dataField.setExcludedTypeList(excludedTypeList);
            dataField.setMultipleField(dataFieldAttribute.isMultipleField());
            return dataField;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static FieldValue annotationToObject(FieldValueAttribute fieldvalueAttribute, Format format, URI dataFieldUri) {
        try {
            format.setDefaultFormat(true);
            return new FieldValue(format, dataFieldUri, fieldvalueAttribute.multiSelection());
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static Enumeration annotationToObject(EnumerationAttribute enumAttribute, Format format) {
        try{
            format.setDefaultFormat(true);
            Enumeration enumeration = new Enumeration(format, enumAttribute.values(), enumAttribute.defaultValues());
            enumeration.setEditable(enumAttribute.isEditable());
            enumeration.setMultiSelection(enumAttribute.multiSelection());
            enumeration.setValuesNames(enumAttribute.names());
            return enumeration;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    public static GeometryData annotationToObject(GeometryAttribute geometryAttribute, Format format) {
        try{
            format.setDefaultFormat(true);
            List<DataType> geometryTypeList = new ArrayList<>();
            //For each field type value from the groovy annotation, test if it is contain in the FieldType enumeration.
            for(String type : Arrays.asList(geometryAttribute.geometryTypes())){
                geometryTypeList.add(DataType.getDataTypeFromFieldType(type));
            }
            List<DataType> excludedTypeList = new ArrayList<>();
            //For each excluded type value from the groovy annotation, test if it is contain in the FieldType enumeration.
            for(String type : Arrays.asList(geometryAttribute.excludedTypes())){
                excludedTypeList.add(DataType.getDataTypeFromFieldType(type));
            }
            GeometryData geometryData = new GeometryData(format, geometryTypeList);
            geometryData.setDimension(geometryAttribute.dimension());
            geometryData.setExcludedTypeList(excludedTypeList);
            return geometryData;
        } catch (MalformedScriptException e) {
            LoggerFactory.getLogger(ObjectAnnotationConverter.class).error(e.getMessage());
            return null;
        }
    }

    /**
     * Returns the process identifier and if their is not, return an URI build around its title.
     * @return String process identifier.
     */
    public static String getProcessIdentifier(Method method){
        DescriptionTypeAttribute annot = method.getAnnotation(DescriptionTypeAttribute.class);
        if(annot != null && !annot.identifier().equals(DescriptionTypeAttribute.defaultIdentifier)){
            return annot.identifier();
        }
        else{
            return annot.title();
        }
    }
}
