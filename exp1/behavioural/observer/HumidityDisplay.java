// ConcreteObserver
public class HumidityDisplay implements Observer {
	@Override
	public void update(float temperature, float humidity, float pressure) {
		System.out.println("HumidityDisplay: Humidity is now " + humidity + "%");
	}
}