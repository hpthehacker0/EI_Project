package structural.adapter;
// Adapter
public class LegacySystemAdapter implements NewSystemInterface {
    private LegacySystem legacySystem;

    public LegacySystemAdapter(LegacySystem legacySystem) {
        this.legacySystem = legacySystem;
    }

    @Override
    public String request() {
        return legacySystem.specificRequest();
    }
}