package creational.factory;
// Concrete Creator
public class PDFDocumentCreator extends DocumentCreator {
    @Override
    public Document factoryMethod() {
        return new PDFDocument();
    }
}