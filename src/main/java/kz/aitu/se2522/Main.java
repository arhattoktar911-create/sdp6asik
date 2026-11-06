package kz.aitu.se2522;

public class Main {
    public static void main(String[] args) {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode src = new DirectoryNode("src");
        DirectoryNode docs = new DirectoryNode("docs");

        FileNode mainJava = new FileNode("Main.java", 1500);
        FileNode appJava = new FileNode("App.java", 2500);
        FileNode readme = new FileNode("README.md", 800);

        root.addElement(src);
        root.addElement(docs);
        src.addElement(mainJava);
        src.addElement(appJava);
        docs.addElement(readme);

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);
        System.out.println("Total Size: " + sizeVisitor.getTotalSize() + " bytes");

        SearchVisitor searchVisitor = new SearchVisitor(".java", 1000);
        root.accept(searchVisitor);
        System.out.println("Search Results (.java >= 1000B): " + searchVisitor.getMatchingPaths());

        StatisticsVisitor statsVisitor = new StatisticsVisitor();
        root.accept(statsVisitor);
        System.out.println("Stats -> Files: " + statsVisitor.getFileCount()
                + ", Directories: " + statsVisitor.getDirectoryCount()
                + ", Max Depth: " + statsVisitor.getMaxDepth());
    }
}