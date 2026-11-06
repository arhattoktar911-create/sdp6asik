package kz.aitu.se2522;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryNode implements Element {
    private final String name;
    private DirectoryNode parent;
    private final List<Element> children = new ArrayList<>();

    public DirectoryNode(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Directory name cannot be empty");
        }
        this.name = name;
    }

    public void addElement(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot add null element");
        }
        if (element == this) {
            throw new IllegalArgumentException("Cannot add directory to itself");
        }
        if (element.getParent() != null) {
            throw new IllegalStateException("Element already has a parent");
        }
        if (containsName(element.getName())) {
            throw new IllegalArgumentException("Duplicate sibling name: " + element.getName());
        }
        if (element instanceof DirectoryNode childDir && isAncestorOf(childDir)) {
            throw new IllegalArgumentException("Adding child would create a cycle");
        }

        children.add(element);
        element.setParent(this);
    }

    private boolean containsName(String name) {
        return children.stream().anyMatch(e -> e.getName().equals(name));
    }

    private boolean isAncestorOf(DirectoryNode target) {
        DirectoryNode current = this;
        while (current != null) {
            if (current == target) return true;
            current = current.getParent();
        }
        return false;
    }

    public List<Element> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DirectoryNode getParent() {
        return parent;
    }

    @Override
    public void setParent(DirectoryNode parent) {
        this.parent = parent;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}