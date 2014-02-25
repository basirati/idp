package de.tum.pssif.vsdx.impl;

import org.junit.Test;

import de.tum.pssif.vsdx.VsdxConnector;
import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxShape;


public class VsdxDocumentLoaderImplTest {

  @Test
  public void testReadTemplate() {
    System.out.println("--- Template ---");
    VsdxDocument document = VsdxDocumentLoaderFactory.INSTANCE.create().loadDocument(getClass().getResourceAsStream("/epk-template.vsdx"));
    System.out.println(document.getMasters());
    System.out.println(document.getPage().getShapes());
  }

  @Test
  public void testReadData() {
    System.out.println("--- Data ---");
    VsdxDocument document = VsdxDocumentLoaderFactory.INSTANCE.create().loadDocument(getClass().getResourceAsStream("/epk-data.vsdx"));
    System.out.println(document.getMasters());
    for (VsdxShape shape : document.getPage().getShapes()) {
      System.out.println(printShape("", shape));
    }
    for (VsdxConnector connect : document.getPage().getConnectors()) {
      System.out.println(printConnect("", connect));
    }
  }

  private String printShape(String prefix, VsdxShape shape) {
    StringBuilder b = new StringBuilder();
    b.append(prefix);
    b.append("Shape: \n");
    b.append(prefix);
    b.append(" - Type: ");
    b.append(shape.getMaster().getName());
    b.append("\n");
    b.append(prefix);
    b.append(" - ID: ");
    b.append(shape.getId());
    b.append("\n");
    b.append(prefix);
    b.append(" - Text: ");
    b.append(shape.getText());
    b.append("\n");
    b.append(prefix);
    b.append(" - Inner Shapes: ");
    for (VsdxShape inner : shape.getShapes()) {
      b.append(printShape(prefix + "  ", inner));
    }
    return b.toString();
  }

  private String printConnect(String prefix, VsdxConnector shape) {
    StringBuilder b = new StringBuilder();
    b.append(prefix);
    b.append("Connect: \n");
    b.append(prefix);
    b.append(" - Type: ");
    b.append(shape.getMaster().getName());
    b.append("\n");
    b.append(prefix);
    b.append(" - ID: ");
    b.append(shape.getId());
    b.append("\n");
    b.append(prefix);
    b.append(" - Text: ");
    b.append(shape.getText());
    b.append("\n");
    b.append(prefix);
    return b.toString();
  }

}