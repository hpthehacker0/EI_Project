package creational.factory;
// Creator Class
public abstract class DocumentCreator {
    public abstract Document factoryMethod();

    public String createDocument() {
        Document document = factoryMethod();
        return document.create();
    }
}