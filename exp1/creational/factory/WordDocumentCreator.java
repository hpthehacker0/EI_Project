package creational.factory;
// Concrete Creator
public class WordDocumentCreator extends DocumentCreator {
    @Override
    public Document factoryMethod() {
        return new WordDocument();
    }
}