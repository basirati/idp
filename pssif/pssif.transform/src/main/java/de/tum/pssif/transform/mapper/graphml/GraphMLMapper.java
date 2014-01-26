package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.google.common.collect.Maps;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFUtil;
import de.tum.pssif.core.util.PSSIFValue;
import de.tum.pssif.transform.mapper.Mapper;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlAttrImpl;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlEdgeImpl;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlNodeImpl;


public class GraphMLMapper implements Mapper {
  @Override
  public Model read(Metamodel metamodel, InputStream in) {
    Model result = new ModelImpl();

    GraphMLGraph graph = GraphMLGraph.read(in);

    for (GraphMLNode inNode : graph.getNodes()) {
      NodeType type = metamodel.findNodeType(inNode.getType());
      if (type != null) {
        Attribute idAttribute = type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
        Node resultNode = type.create(result);
        idAttribute.set(resultNode, PSSIFOption.one(idAttribute.getType().fromObject(inNode.getId())));

        readAttributes(type, resultNode, inNode);
      }
      else {
        System.out.println("NodeType " + inNode.getType() + " not found!");
      }
    }

    for (GraphMLEdge inEdge : graph.getEdges()) {
      EdgeType type = metamodel.findEdgeType(inEdge.getType());
      if (type != null) {

        GraphMLNode inSourceNode = graph.getNode(inEdge.getSourceId());
        GraphMLNode inTargetNode = graph.getNode(inEdge.getTargetId());

        NodeType sourceType = metamodel.findNodeType(inSourceNode.getType());
        PSSIFOption<Node> sourceNode = sourceType.apply(result, inSourceNode.getId());

        NodeType targetType = metamodel.findNodeType(inTargetNode.getType());
        PSSIFOption<Node> targetNode = targetType.apply(result, inTargetNode.getId());

        if (sourceType != null && targetType != null) {
          ConnectionMapping mapping = type.getMapping(sourceType, targetType);
          if (sourceNode.isOne() && targetNode.isOne()) {
            Edge edge = mapping.create(result, sourceNode.getOne(), targetNode.getOne());
            type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED).set(edge, PSSIFOption.one(PSSIFValue.create(inEdge.isDirected())));
            readAttributes(type, edge, inEdge);
          }
          else {
            System.out.println("either source or target node not found!");
          }
        }
        else {
          System.out.println("either source or target type of edge " + inEdge.getId() + " not found!");
        }
      }
      else {
        System.out.println("EdgeType " + inEdge.getType() + " not found!");
      }
    }

    return result;
  }

  private static void readAttributes(ElementType<?, ?> type, Element element, GraphMLElement inElement) {
    Map<String, String> values = inElement.getValues();
    for (String key : values.keySet()) {
      Attribute attribute = type.findAttribute(key);
      if (attribute != null) {
        attribute.set(element, PSSIFOption.one(attribute.getType().fromObject(values.get(key))));
      }
      else {
        System.out.println("AttributeType " + key + " not found!");
      }
    }
  }

  @Override
  public void write(Metamodel metamodel, Model model, OutputStream outputStream) {
    GraphMLGraph graph = GraphMLGraph.create();
    addAttributesToGraph(graph, metamodel);
    for (NodeType nodeType : metamodel.getNodeTypes()) {
      addNodesToGraph(graph, nodeType, model);
    }
    for (EdgeType edgeType : metamodel.getEdgeTypes()) {
      addEdgesToGraph(graph, edgeType, model);
    }
    GraphMLGraph.write(graph, outputStream);
  }

  private void addAttributesToGraph(GraphMLGraph graph, Metamodel metamodel) {
    Map<String, GraphMlAttribute> attributes = Maps.newHashMap();
    for (NodeType nodeType : metamodel.getNodeTypes()) {
      for (Attribute attribute : nodeType.getAttributes()) {
        if (!attributes.containsKey(PSSIFUtil.normalize(attribute.getName()))) {
          attributes.put(PSSIFUtil.normalize(attribute.getName()), new GraphMlAttrImpl(attribute.getName(), attribute.getType().getName()));
        }
      }
    }
    graph.addNodeAttributes(attributes.values());
    attributes = Maps.newHashMap();
    for (EdgeType edgeType : metamodel.getEdgeTypes()) {
      for (Attribute attribute : edgeType.getAttributes()) {
        if (!attributes.containsKey(PSSIFUtil.normalize(attribute.getName()))) {
          attributes.put(PSSIFUtil.normalize(attribute.getName()), new GraphMlAttrImpl(attribute.getName(), attribute.getType().getName()));
        }
      }
    }
    graph.addEdgeAttributes(attributes.values());
  }

  private void addNodesToGraph(GraphMLGraph graph, NodeType nodeType, Model model) {
    Attribute idAttribute = nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    for (Node pssifNode : nodeType.apply(model).getMany()) {
      GraphMlNodeImpl node = new GraphMlNodeImpl(id(idAttribute, pssifNode));
      node.setValue(GraphMLTokens.ELEMENT_TYPE, nodeType.getName());
      for (Attribute attr : nodeType.getAttributes()) {
        PSSIFOption<PSSIFValue> val = attr.get(pssifNode);
        if (!idAttribute.equals(attr) && val.isOne()) {
          node.setValue(attr.getName(), val.getOne().getValue().toString());
        }
      }
      graph.addNode(node);
    }
  }

  private void addEdgesToGraph(GraphMLGraph graph, EdgeType edgeType, Model model) {
    //FIXME des geht?!
    Attribute idAttribute = edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    for (ConnectionMapping mapping : edgeType.getMappings()) {
      for (Node pssifNode : mapping.getFrom().getNodeType().apply(model).getMany()) {
        PSSIFOption<Edge> edges = mapping.getFrom().apply(pssifNode);
        for (Edge pssifEdge : edges.getMany()) {
          Node targetNode = mapping.getTo().apply(pssifEdge).getOne();
          GraphMlEdgeImpl edge = new GraphMlEdgeImpl(graph.getNode(id(idAttribute, pssifEdge)).getId(), graph.getNode(id(idAttribute, pssifNode))
              .getId(), graph.getNode(id(idAttribute, targetNode)).getId(), false);
          graph.addEdge(edge);
        }
      }
    }
  }

  private static String id(Attribute idAttribute, Element element) {
    return idAttribute.get(element).getOne().asString();
  }
}
