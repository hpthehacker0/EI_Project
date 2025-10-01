package creational.factory;
// Usage
public class Main {
    public static void main(String[] args) {
        DocumentCreator creator = new WordDocumentCreator();
        System.out.println(creator.createDocument());  // Output: Word Document created

        creator = new PDFDocumentCreator();
        System.out.println(creator.createDocument());  // Output: PDF Document created
    }
}
