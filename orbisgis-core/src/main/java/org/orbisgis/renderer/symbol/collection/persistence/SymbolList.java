//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-600 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.10 at 05:39:39 PM CEST 
//


package org.orbisgis.renderer.symbol.collection.persistence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{org.orbisgis.symbol}symbol-composite"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "symbolComposite"
})
@XmlRootElement(name = "symbol-list")
public class SymbolList {

    @XmlElement(name = "symbol-composite", required = true)
    protected SymbolCompositeType symbolComposite;

    /**
     * Gets the value of the symbolComposite property.
     * 
     * @return
     *     possible object is
     *     {@link SymbolCompositeType }
     *     
     */
    public SymbolCompositeType getSymbolComposite() {
        return symbolComposite;
    }

    /**
     * Sets the value of the symbolComposite property.
     * 
     * @param value
     *     allowed object is
     *     {@link SymbolCompositeType }
     *     
     */
    public void setSymbolComposite(SymbolCompositeType value) {
        this.symbolComposite = value;
    }

}
