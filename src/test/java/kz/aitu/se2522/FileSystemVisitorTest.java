package kz.aitu.se2522;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemVisitorTest {

    private DirectoryNode root;
    private DirectoryNode src;
    private DirectoryNode tests;
    private FileNode file1;
    private FileNode file2;
    private FileNode file3;

    @BeforeEach
    void setUp() {
        root = new DirectoryNode("root");
        src = new DirectoryNode("src");
        tests = new DirectoryNode("test");

        file1 = new FileNode("Main.java", 1500);
        file2 = new FileNode("Utils.java", 800);
        file3 = new FileNode("TestMain.java", 2000);

        root.addElement(src);
        src.addElement(file1);
        src.addElement(file2);
        src.addElement(tests);
        tests.addElement(file3);
    }

    @Test
    @DisplayName("T1: Double Dispatch - elements route to correct visit method")
    void testT1_DoubleDispatchRouting() {
        class MockVisitor implements Visitor {
            boolean visitedFile = false;
            boolean visitedDir = false;

            @Override
            public void visit(FileNode file) { visitedFile = true; }
            @Override
            public void visit(DirectoryNode directory) { visitedDir = true; }
        }

        MockVisitor mv1 = new MockVisitor();
        file1.accept(mv1);
        assertTrue(mv1.visitedFile && !mv1.visitedDir);

        MockVisitor mv2 = new MockVisitor();
        root.accept(mv2);
        assertTrue(mv2.visitedDir);
    }

    @Test
    @DisplayName("T2: TotalSizeVisitor accumulates size across nested levels correctly")
    void testT2_SizeAccumulation() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        root.accept(visitor);
        assertEquals(4300, visitor.getTotalSize());
    }

    @Test
    @DisplayName("T3: SearchVisitor filters by combined criteria (name AND min size)")
    void testT3_SearchCriteriaCombination() {
        SearchVisitor visitor = new SearchVisitor("Main", 1000);
        root.accept(visitor);
        List<String> results = visitor.getMatchingPaths();

        assertEquals(2, results.size());
        assertTrue(results.contains("root/src/Main.java"));
        assertTrue(results.contains("root/src/test/TestMain.java"));
    }

    @Test
    @DisplayName("T4: SearchVisitor produces deterministic, root-relative, lexicographically sorted paths")
    void testT4_SearchDeterministicPaths() {
        SearchVisitor visitor = new SearchVisitor(".java", 0);
        root.accept(visitor);
        List<String> results = visitor.getMatchingPaths();

        List<String> expected = List.of(
                "root/src/Main.java",
                "root/src/Utils.java",
                "root/src/test/TestMain.java"
        );
        assertEquals(expected, results);
    }

    @Test
    @DisplayName("T5: StatisticsVisitor correctly counts files, directories, and max depth")
    void testT5_StatisticsCalculation() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        root.accept(visitor);

        assertEquals(3, visitor.getFileCount());
        assertEquals(3, visitor.getDirectoryCount());
        assertEquals(2, visitor.getMaxDepth());
    }

    @Test
    @DisplayName("T6: Empty structures and no-match search cases handled safely")
    void testT6_EmptyAndNoMatch() {
        DirectoryNode emptyDir = new DirectoryNode("empty");

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        emptyDir.accept(sizeVisitor);
        assertEquals(0, sizeVisitor.getTotalSize());

        SearchVisitor searchVisitor = new SearchVisitor("NonExistent", 500);
        root.accept(searchVisitor);
        assertTrue(searchVisitor.getMatchingPaths().isEmpty());
    }

    @Test
    @DisplayName("T7: Repeat executions do not leak state when reset() is called")
    void testT7_StateIsolation() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        root.accept(visitor);
        assertEquals(4300, visitor.getTotalSize());

        visitor.reset();
        root.accept(visitor);
        assertEquals(4300, visitor.getTotalSize());
    }

    @Test
    @DisplayName("T8: Tree integrity checks reject invalid mutations")
    void testT8_TreeIntegrity() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad.txt", -10));
        assertThrows(IllegalArgumentException.class, () -> src.addElement(new FileNode("Main.java", 10)));
        assertThrows(IllegalStateException.class, () -> root.addElement(file1));

        DirectoryNode subDir = new DirectoryNode("sub");
        root.addElement(subDir);
        assertThrows(IllegalArgumentException.class, () -> subDir.addElement(root));
    }
}