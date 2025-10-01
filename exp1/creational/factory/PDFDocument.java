package creational.factory;

// Concrete Product
public class PDFDocument implements Document {
    @Override
    public String create() {
        return "PDF Document created";
    }
}