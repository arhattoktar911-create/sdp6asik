package kz.aitu.se2522;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchVisitor implements Visitor {
    private final String nameCriterion;
    private final long minSizeThreshold;
    private final List<String> matchingPaths = new ArrayList<>();
    private final List<String> pathStack = new ArrayList<>();

    public SearchVisitor(String nameCriterion, long minSizeThreshold) {
        this.nameCriterion = nameCriterion == null ? "" : nameCriterion;
        this.minSizeThreshold = Math.max(0, minSizeThreshold);
    }

    public List<String> getMatchingPaths() {
        List<String> sorted = new ArrayList<>(matchingPaths);
        Collections.sort(sorted);
        return Collections.unmodifiableList(sorted);
    }

    public void reset() {
        matchingPaths.clear();
        pathStack.clear();
    }

    @Override
    public void visit(FileNode file) {
        boolean matchesName = file.getName().contains(nameCriterion);
        boolean matchesSize = file.getSize() >= minSizeThreshold;

        if (matchesName && matchesSize) {
            String fullPath = buildCurrentPath(file.getName());
            matchingPaths.add(fullPath);
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        pathStack.add(directory.getName());
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
        pathStack.remove(pathStack.size() - 1);
    }

    private String buildCurrentPath(String fileName) {
        if (pathStack.isEmpty()) {
            return fileName;
        }
        return String.join("/", pathStack) + "/" + fileName;
    }
}