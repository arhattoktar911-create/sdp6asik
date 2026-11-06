package kz.aitu.se2522;

public class TotalSizeVisitor implements Visitor {
    private long totalSize = 0;

    public long getTotalSize() {
        return totalSize;
    }

    public void reset() {
        this.totalSize = 0;
    }

    @Override
    public void visit(FileNode file) {
        totalSize += file.getSize();
    }

    @Override
    public void visit(DirectoryNode directory) {
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
    }
}