package creational.singleton;
// Usage
public class Main {
    public static void main(String[] args) {
        ConfigurationManager config1 = ConfigurationManager.getInstance();
        ConfigurationManager config2 = ConfigurationManager.getInstance();

        config1.setSetting("theme", "dark");
        System.out.println(config2.getSetting("theme"));  // Output: dark

        System.out.println(config1 == config2);  // Output: true
    }
}