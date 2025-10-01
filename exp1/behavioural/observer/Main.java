
public class Main {
	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData();
		TemperatureDisplay tempDisplay = new TemperatureDisplay();
		HumidityDisplay humidityDisplay = new HumidityDisplay();
		WeatherForecastDisplay forecastDisplay = new WeatherForecastDisplay();

		weatherData.registerObserver(tempDisplay);
		weatherData.registerObserver(humidityDisplay);
		weatherData.registerObserver(forecastDisplay);

		weatherData.setMeasurements(25, 65, 1013);
		weatherData.setMeasurements(22, 70, 1012);
	}
}