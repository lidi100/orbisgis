//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.15 at 03:38:58 PM CET 
//


package net.opengis.gml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Definition of one unit term for a derived unit of measure. This unit term references another unit of measure (uom) and provides an integer exponent applied to that unit in defining the compound unit. The exponent can be positive or negative, but not zero.
 * 
 * <p>Java class for DerivationUnitTermType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DerivationUnitTermType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}UnitOfMeasureType">
 *       &lt;attribute name="exponent" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DerivationUnitTermType")
public class DerivationUnitTermType
    extends UnitOfMeasureType
{

    @XmlAttribute(name = "exponent")
    protected BigInteger exponent;

    /**
     * Gets the value of the exponent property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getExponent() {
        return exponent;
    }

    /**
     * Sets the value of the exponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setExponent(BigInteger value) {
        this.exponent = value;
    }

}
