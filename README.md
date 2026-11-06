# Assignment 06: Visitor Pattern - Advanced File System

**Student:** Toktar Arkat  
**Course:** ShP-2216 (Software Design Patterns)  
**Group:** SE-2522  
**University:** Astana IT University  

---

## 1. Project Overview & Architecture
This project implements an in-memory hierarchical file system using the **Visitor behavioral pattern** in Java 17 with Maven and JUnit 5 testing.

### Key Components:
- **`Element`**: Common interface declaring `accept(Visitor visitor)`.
- **`FileNode`**: Concrete element representing a file with name and byte size.
- **`DirectoryNode`**: Concrete element representing a directory holding child nodes (files or nested directories).
- **`TotalSizeVisitor`**: Computes total size of files across the subtree.
- **`SearchVisitor`**: Searches files matching configurable filename criteria and minimum size thresholds.
- **`StatisticsVisitor`**: Gathers total file count, directory count, and maximum depth of the directory structure.

---

## 2. Traversal & Extensibility Trade-offs

- **Traversal Owner:** Visitor-driven traversal. In `visit(DirectoryNode)`, the visitor iterates over child nodes. This allows visitors to manage complex internal context (e.g., path stacks and depth tracking) during execution.
- **Extension Boundary:** Adding new operations (like `StatisticsVisitor`) requires zero changes to existing `FileNode` or `DirectoryNode` classes. However, adding a new element type (e.g., `SymlinkNode`) requires modifying the `Visitor` interface and all implementing visitors.

---

## 3. Five Clean Code Practices Applied

1. **Meaningful Naming (`SearchVisitor.minSizeThreshold`):** State and parameters use intent-revealing names instead of magic values.
2. **Single Responsibility Principle (`TotalSizeVisitor`):** Each visitor class handles exactly one independent operation over the element structure.
3. **Small Methods (`DirectoryNode.isAncestorOf`):** Concise, focused helper method dedicated strictly to cycle detection.
4. **Encapsulation (`DirectoryNode.getChildren`):** Protects internal state by returning `Collections.unmodifiableList()`.
5. **Elimination of Type-Checking Chains (`FileNode.accept` / `DirectoryNode.accept`):** Replaces `instanceof` and `if-else` branching with polymorphic **Double Dispatch**.

---

## 4. How to Run

### Run Tests:
```bash
mvn test
Run Demonstration:
5. Automated Verification Results
Test Suite: kz.aitu.se2522.FileSystemVisitorTest

Result: 8/8 PASSED (T1–T8 fully verified)
