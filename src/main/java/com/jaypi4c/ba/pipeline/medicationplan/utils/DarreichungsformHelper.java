package com.jaypi4c.ba.pipeline.medicationplan.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Component
public class DarreichungsformHelper {

    private final Map<String, Triple<String, String, String>> values = new HashMap<>();
    private final String PATH = "aliases/S_BMP_DARREICHUNGSFORM_V1.03.xml";

    @Getter
    private String[] dictionary;

    @Getter
    private Map<String, String> aliases;

    public DarreichungsformHelper() {
        try {
            aliases = new HashMap<>();
            List<String> dict = new ArrayList<>();
            ClassLoader classLoader = DarreichungsformHelper.class.getClassLoader();
            InputStream xmlStream = classLoader.getResourceAsStream(PATH);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);

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
                        dict.add(bezeichnungIFA);
                        values.put(v, Triple.of(dn, sv, bezeichnungIFA));
                        aliases.put(v, bezeichnungIFA);
                        aliases.put(dn, bezeichnungIFA);
                    }
                }
            }
            dictionary = dict.toArray(new String[0]);
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
