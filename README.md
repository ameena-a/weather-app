Overview :

Weather App API displays the weather information for the requested city and country. It calls the OpenWeather External API and Geocoding External API to get the weather information and stored the information in DB.
It is the sprint boot application and having weather app API endpoint which calls External API using Spring Web flux.
Weather App API is rate limited ( 5 requests in 1 hr) for API key using bucket4j.
 
Local Run :
Start the Weather APP application.
Invoke http://localhost:8080/weather-app/v1/status?city=Melbourne&country=AU&openWeatherMapApiKey=dcb259bad64a4f2599fb448997ec8a29  in Postman
Response :
{
    "status": "SUCCESS",
    "weatherDesc": "clear sky"
}

If the API is getting invoked more than 5 times in hr, the error response is returned.
{
    "status": "FAILURE - OpenWeatherMapApiKey is allowed only 5 calls per one hour for the given API key",
    "weatherDesc": ""
}

Test:
one black box test is been written.
We can use wiremock to have component tests to test Web Client.
