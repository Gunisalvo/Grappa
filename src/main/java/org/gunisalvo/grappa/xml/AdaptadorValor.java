package org.gunisalvo.grappa.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class AdaptadorValor extends XmlAdapter<Element, Valor> {
	 
    private ClassLoader classLoader;
    private DocumentBuilder builder;
    private JAXBContext contexto;
 
    public AdaptadorValor() {
        classLoader = Thread.currentThread().getContextClassLoader();
    }
 
    public AdaptadorValor(JAXBContext jaxbContext) {
        this();
        this.contexto = jaxbContext;
    }
 
    private DocumentBuilder getDocumentBuilder() throws Exception {
        
        if (null == builder) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            builder = dbf.newDocumentBuilder();
        }
        return builder;
    }
 
    private JAXBContext getJAXBContext(Class<?> type) throws Exception {
        if (null == contexto) {
            return JAXBContext.newInstance(type);
        }
        return contexto;
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Element marshal(Valor emJava) throws Exception {
        if (null == emJava) {
            return null;
        }
 
        QName raizXml = new QName(emJava.getNome());
        Object corpo = emJava.getCorpo();
        Class<?> tipo = corpo.getClass();
		JAXBElement<?> jaxbElement = new JAXBElement(raizXml, tipo, corpo);
 
        Document documento = getDocumentBuilder().newDocument();
        Marshaller marshaller = getJAXBContext(tipo).createMarshaller();
        marshaller.marshal(jaxbElement, documento);
        Element emXml = documento.getDocumentElement();
 
        return emXml;
    }
 
    @Override
    public Valor unmarshal(Element emXml) throws Exception {
        if (null == emXml) {
            return null;
        }
        
        Class<?> tipo = classLoader.loadClass(emXml.getLocalName());
        
        DOMSource fonte = new DOMSource(emXml);
        Unmarshaller unmarshaller = getJAXBContext(tipo).createUnmarshaller();
        JAXBElement<?> jaxb = unmarshaller.unmarshal(fonte, tipo);
 
        Valor emJava = new Valor();
        emJava.setNome(emXml.getLocalName());
        emJava.setCorpo(jaxb.getValue());
        return emJava;
    }
 
}
