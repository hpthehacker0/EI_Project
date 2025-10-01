package creational.factory;
// Concrete Product
public class WordDocument implements Document {
    @Override
    public String create() {
        return "Word Document created";
    }
}
