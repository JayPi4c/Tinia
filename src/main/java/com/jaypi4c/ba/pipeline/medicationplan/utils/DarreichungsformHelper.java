package com.jaypi4c.ba.pipeline.medicationplan.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.xerces.parsers.DOMParser;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class DarreichungsformHelper {

    private final Map<String, Triple<String, String, String>> values = new HashMap<>();
    private final String PATH = "aliases/S_BMP_DARREICHUNGSFORM_V1.03.xml";

    public DarreichungsformHelper() {
        try {
            ClassLoader classLoader = DarreichungsformHelper.class.getClassLoader();
            InputStream xmlStream = classLoader.getResourceAsStream(PATH);


            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(xmlStream)); // Provide the path to your XML file here

            Document doc = parser.getDocument();
            NodeList keytabsList = doc.getElementsByTagName("keytab");

            for (int i = 0; i < keytabsList.getLength(); i++) {
                Node keytabNode = keytabsList.item(i);
                if (keytabNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element keytabElement = (Element) keytabNode;
                    if (!keytabElement.getAttribute("SN").equals("S_BMP_DARREICHUNGSFORM"))
                        continue;
                    NodeList keyList = keytabElement.getElementsByTagName("key");
                    for (int j = 0; j < keyList.getLength(); j++) {
                        Element keyElement = (Element) keyList.item(j);
                        String v = keyElement.getAttribute("V");
                        String dn = keyElement.getAttribute("DN");
                        String sv = keyElement.getAttribute("SV");
                        String bezeichnungIFA = keyElement.getAttribute("bezeichnungIFA");
                        values.put(v, Triple.of(dn, sv, bezeichnungIFA));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while parsing xml", e);
        }
    }

    public Optional<String> getBezeichnungIFAByDN(String DN) {
        return values.values().stream().filter(t -> (t.getLeft().equals(DN))).map(Triple::getRight).findFirst();
    }

    public Optional<String> getBezeichnungIFAByV(String V) {
        Triple<String, String, String> t = values.get(V);
        if (t == null)
            return Optional.empty();
        return Optional.of(t.getRight());
    }

}
