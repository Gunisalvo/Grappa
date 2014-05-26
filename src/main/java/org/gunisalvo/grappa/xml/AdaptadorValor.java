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
    private DocumentBuilder documentBuilder;
    private JAXBContext jaxbContext;
 
    public AdaptadorValor() {
        classLoader = Thread.currentThread().getContextClassLoader();
    }
 
    public AdaptadorValor(JAXBContext jaxbContext) {
        this();
        this.jaxbContext = jaxbContext;
    }
 
    private DocumentBuilder getDocumentBuilder() throws Exception {
        // Lazy load the DocumentBuilder as it is not used for unmarshalling.
        if (null == documentBuilder) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            documentBuilder = dbf.newDocumentBuilder();
        }
        return documentBuilder;
    }
 
    private JAXBContext getJAXBContext(Class<?> type) throws Exception {
        if (null == jaxbContext) {
            // A JAXBContext was not set, so create a new one based  on the type.
            return JAXBContext.newInstance(type);
        }
        return jaxbContext;
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Element marshal(Valor parameter) throws Exception {
        if (null == parameter) {
            return null;
        }
 
        // 1. Build the JAXBElement to wrap the instance of Parameter.
        QName rootElement = new QName(parameter.getNome());
        Object value = parameter.getCorpo();
        Class<?> type = value.getClass();
		JAXBElement<?> jaxbElement = new JAXBElement(rootElement, type, value);
 
        // 2.  Marshal the JAXBElement to a DOM element.
        Document document = getDocumentBuilder().newDocument();
        Marshaller marshaller = getJAXBContext(type).createMarshaller();
        marshaller.marshal(jaxbElement, document);
        Element element = document.getDocumentElement();
 
        return element;
    }
 
    @Override
    public Valor unmarshal(Element element) throws Exception {
        if (null == element) {
            return null;
        }
 
        // 1. Determine the values type from the type attribute.
        Class<?> type = classLoader.loadClass(element.getLocalName());
 
        // 2. Unmarshal the element based on the value's type.
        DOMSource source = new DOMSource(element);
        Unmarshaller unmarshaller = getJAXBContext(type).createUnmarshaller();
        JAXBElement<?> jaxbElement = unmarshaller.unmarshal(source, type);
 
        // 3. Build the instance of Parameter
        Valor parameter = new Valor();
        parameter.setNome(element.getLocalName());
        parameter.setCorpo(jaxbElement.getValue());
        return parameter;
    }
 
}
