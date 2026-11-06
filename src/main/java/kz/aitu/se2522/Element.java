package kz.aitu.se2522;

public interface Element {
    String getName();
    DirectoryNode getParent();
    void setParent(DirectoryNode parent);
    void accept(Visitor visitor);
}