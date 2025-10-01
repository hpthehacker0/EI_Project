package structural.adapter;
// Usage
public class Main {
    public static void main(String[] args) {
        LegacySystem legacySystem = new LegacySystem();
        LegacySystemAdapter adapter = new LegacySystemAdapter(legacySystem);
        System.out.println(adapter.request());  // Output: Legacy system response
    }
}
