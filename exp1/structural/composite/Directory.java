package structural.composite;
import java .util.*;
// Composite
public class Directory implements FileSystemItem {
    private String name;
    private List<FileSystemItem> children;

    public Directory(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void add(FileSystemItem item) {
        children.add(item);
    }

    @Override
    public void display(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        System.out.println(name);
        for (FileSystemItem child : children) {
            child.display(indent + 1);
        }
    }
}