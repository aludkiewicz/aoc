package com.aoc;

import java.util.*;

public class Day6 {

    public static void main(String[] args) {

        FileReader reader = new FileReader();
        List<String> day6Input = reader.read("input_day_6");

        Map<String, Node> nodes = new HashMap<>();
        buildTree(day6Input, nodes);

        int nofOrbits = 0;
        for(Node node : nodes.values()) {
            nofOrbits += traverseAndCount(node, 0);
        }

        Node santa = nodes.get("SAN");
        Node you = nodes.get("YOU");

        System.out.println("Number of orbits is: " + nofOrbits);
        System.out.println("Distance between you and santa is: " + distanceBetween(you, santa));

    }

    private static int distanceBetween(Node n1, Node n2) {
        Node commonAncestor = findCommonAncestor(n1, n2);

        if(commonAncestor == null) {
            throw new RuntimeException("No common ancestor between nodes!");
        }

        return  distanceToAncestor(n1, commonAncestor) + distanceToAncestor(n2, commonAncestor);
    }

    private static int distanceToAncestor(Node n, Node commonAncestor) {
        int dist = 0;

        Node parent1 = n.getParent();
        while (!commonAncestor.equals(parent1)) {
            dist++;
            parent1 = parent1.getParent();
        }
        return dist;
    }

    private static Node findCommonAncestor(Node n1, Node n2) {

        List<Node> node1Ancestors = new ArrayList<>();
        List<Node> node2Ancestors = new ArrayList<>();

        populateParents(n1, node1Ancestors);
        populateParents(n2, node2Ancestors);

        if(node1Ancestors.size() == 1) {
            return node1Ancestors.get(0);
        }

        if(node2Ancestors.size() == 1) {
            return node2Ancestors.get(0);
        }

        Set<Node> node2AncestorSet = new HashSet<>(node2Ancestors); // Just faster
        for (Node ancestor : node1Ancestors) {
            if(node2AncestorSet.contains(ancestor)) {
                return ancestor;
            }
        }
        return null;
    }

    private static void populateParents(Node n, List<Node> ancestors) {
        Node parent = n.getParent();

        if(parent == null) {
            ancestors.add(n); // In case of root node
        }

        while(parent != null) {
            ancestors.add(parent);
            parent = parent.getParent();
        }
    }

    private static int traverseAndCount(Node root, int startVal) {

        Set<Node> children = root.getChildren();

        for (Node child : children) {
            startVal = traverseAndCount(child, startVal + 1);
        }

        return startVal;
    }

    private static void buildTree(List<String> day6Input, Map<String, Node> nodes) {
        day6Input.forEach(s -> {

            Node parent = new Node(s.split("\\)")[0]);
            Node child = new Node(s.split("\\)")[1]);

            parent = resolve(nodes, parent);

            child = resolve(nodes, child);

            child.setParent(parent);
            parent.addChild(child);

        });
    }

    private static Node resolve(Map<String, Node> nodes, Node parent) {
        if (nodes.containsKey(parent.getName())) {
            parent = nodes.get(parent.getName());
        } else {
            nodes.put(parent.getName(), parent);
        }
        return parent;
    }

    static class Node {

        String name;
        Set<Node> children;
        Node parent;

        public Node(String name) {
            this.name = name;
            this.children = new HashSet<>();
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public String getName() {
            return name;
        }


        public void addChild(Node n) {
            children.add(n);
        }

        public Set<Node> getChildren() {
            return children;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
