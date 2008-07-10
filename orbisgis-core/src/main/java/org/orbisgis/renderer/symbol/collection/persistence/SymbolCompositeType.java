//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-600 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.10 at 05:39:39 PM CEST 
//


package org.orbisgis.renderer.symbol.collection.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for symbol-composite-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="symbol-composite-type">
 *   &lt;complexContent>
 *     &lt;extension base="{org.orbisgis.symbol}symbol-type">
 *       &lt;sequence>
 *         &lt;element name="symbol" type="{org.orbisgis.symbol}symbol-type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "symbol-composite-type", propOrder = {
    "symbol"
})
public class SymbolCompositeType
    extends SymbolType
{

    @XmlElement(namespace = "")
    protected List<SymbolType> symbol;

    /**
     * Gets the value of the symbol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the symbol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSymbol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SymbolType }
     * 
     * 
     */
    public List<SymbolType> getSymbol() {
        if (symbol == null) {
            symbol = new ArrayList<SymbolType>();
        }
        return this.symbol;
    }

}
