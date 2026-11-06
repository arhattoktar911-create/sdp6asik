package kz.aitu.se2522;

public interface Visitor {
    void visit(FileNode file);
    void visit(DirectoryNode directory);
}