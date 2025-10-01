
public class WeatherForecastDisplay implements Observer {
	@Override
	public void update(float temperature, float humidity, float pressure) {
		System.out.println("WeatherForecastDisplay: Pressure is now " + pressure + " hPa");
	}
}