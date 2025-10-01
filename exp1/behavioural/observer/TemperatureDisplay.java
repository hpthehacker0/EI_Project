

public class TemperatureDisplay implements Observer {
	@Override
	public void update(float temperature, float humidity, float pressure) {
		System.out.println("TemperatureDisplay: Temperature is now " + temperature + "°C");
	}
}