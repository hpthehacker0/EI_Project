package structural.composite;

// Leaf
public class File implements FileSystemItem {
    private String name;

    public File(String name) {
        this.name = name;
    }

    @Override
    public void display(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        System.out.println(name);
    }
}