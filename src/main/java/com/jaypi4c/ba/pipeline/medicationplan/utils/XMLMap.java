package com.jaypi4c.ba.pipeline.medicationplan.utils;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLMap {

    private final Map<String, List<String>> map = new HashMap<>();

    public XMLMap(String path, String keytab_sn) {
        try {
            DOMParser parser = new DOMParser();
            parser.parse(path); // Provide the path to your XML file here

            Document doc = parser.getDocument();
            NodeList keytabsList = doc.getElementsByTagName("keytab");

            for (int i = 0; i < keytabsList.getLength(); i++) {
                Node keytabNode = keytabsList.item(i);
                if (keytabNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element keytabElement = (Element) keytabNode;
                    if (!keytabElement.getAttribute("SN").equals(keytab_sn))
                        continue;
                    NodeList keyList = keytabElement.getElementsByTagName("key");
                    for (int j = 0; j < keyList.getLength(); j++) {
                        Element keyElement = (Element) keyList.item(j);
                        String v = keyElement.getAttribute("V");
                        String dn = keyElement.getAttribute("DN");
                        String sv = keyElement.getAttribute("SV");
                        String sortierung = keyElement.getAttribute("sortierung");
                        String bedeutung = keyElement.getAttribute("bedeutung");
                        if (map.containsKey(v)) {
                            map.get(v).add(dn);
                        } else {
                            map.put(v, new ArrayList<>(List.of(dn)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    record Key(String V, String DN, String S, String SV, String sortierung, String bedeutung, boolean unterstuetzt) {

    }


}
