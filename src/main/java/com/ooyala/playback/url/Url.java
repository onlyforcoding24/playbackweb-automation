//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.21 at 11:02:37 PM AEDT 
//

package com.ooyala.playback.url;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="embed_code">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="pcode">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="plugins">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="adPlugins">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="additionalPlugins">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="pbid">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="playerParameter" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "description", "embedCode", "pcode",
		"plugins", "adPlugins", "additionalPlugins", "pbid", "playerParameter" })
public class Url {

	@XmlElement(required = true)
	protected Description description;
	@XmlElement(name = "embed_code", required = true)
	protected EmbedCode embedCode;
	@XmlElement(required = true)
	protected Pcode pcode;
	@XmlElement(required = true)
	protected Plugins plugins;
	@XmlElement(required = true)
	protected AdPlugins adPlugins;
	@XmlElement(required = true)
	protected AdditionalPlugins additionalPlugins;
	@XmlElement(required = true)
	protected Pbid pbid;
	@XmlElement(required = true)
	protected String playerParameter;

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link Description }
	 * 
	 */
	public Description getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link Description }
	 * 
	 */
	public void setDescription(Description value) {
		this.description = value;
	}

	/**
	 * Gets the value of the embedCode property.
	 * 
	 * @return possible object is {@link EmbedCode }
	 * 
	 */
	public EmbedCode getEmbedCode() {
		return embedCode;
	}

	/**
	 * Sets the value of the embedCode property.
	 * 
	 * @param value
	 *            allowed object is {@link EmbedCode }
	 * 
	 */
	public void setEmbedCode(EmbedCode value) {
		this.embedCode = value;
	}

	/**
	 * Gets the value of the pcode property.
	 * 
	 * @return possible object is {@link Pcode }
	 * 
	 */
	public Pcode getPcode() {
		return pcode;
	}

	/**
	 * Sets the value of the pcode property.
	 * 
	 * @param value
	 *            allowed object is {@link Pcode }
	 * 
	 */
	public void setPcode(Pcode value) {
		this.pcode = value;
	}

	/**
	 * Gets the value of the plugins property.
	 * 
	 * @return possible object is {@link Plugins }
	 * 
	 */
	public Plugins getPlugins() {
		return plugins;
	}

	/**
	 * Sets the value of the plugins property.
	 * 
	 * @param value
	 *            allowed object is {@link Plugins }
	 * 
	 */
	public void setPlugins(Plugins value) {
		this.plugins = value;
	}

	/**
	 * Gets the value of the adPlugins property.
	 * 
	 * @return possible object is {@link AdPlugins }
	 * 
	 */
	public AdPlugins getAdPlugins() {
		return adPlugins;
	}

	/**
	 * Sets the value of the adPlugins property.
	 * 
	 * @param value
	 *            allowed object is {@link AdPlugins }
	 * 
	 */
	public void setAdPlugins(AdPlugins value) {
		this.adPlugins = value;
	}

	/**
	 * Gets the value of the additionalPlugins property.
	 * 
	 * @return possible object is {@link AdditionalPlugins }
	 * 
	 */
	public AdditionalPlugins getAdditionalPlugins() {
		return additionalPlugins;
	}

	/**
	 * Sets the value of the additionalPlugins property.
	 * 
	 * @param value
	 *            allowed object is {@link AdditionalPlugins }
	 * 
	 */
	public void setAdditionalPlugins(AdditionalPlugins value) {
		this.additionalPlugins = value;
	}

	/**
	 * Gets the value of the pbid property.
	 * 
	 * @return possible object is {@link Pbid }
	 * 
	 */
	public Pbid getPbid() {
		return pbid;
	}

	/**
	 * Sets the value of the pbid property.
	 * 
	 * @param value
	 *            allowed object is {@link Pbid }
	 * 
	 */
	public void setPbid(Pbid value) {
		this.pbid = value;
	}

	/**
	 * Gets the value of the playerParameter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPlayerParameter() {
		return playerParameter;
	}

	/**
	 * Sets the value of the playerParameter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPlayerParameter(String value) {
		this.playerParameter = value;
	}

}
