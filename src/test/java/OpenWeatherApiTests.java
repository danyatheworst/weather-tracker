import com.danyatheworst.exceptions.OpenWeatherApiException;
import com.danyatheworst.openWeather.LocationApiDto;
import com.danyatheworst.openWeather.OpenWeatherApiService;
import com.danyatheworst.openWeather.weatherApiResponse.WeatherApiResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OpenWeatherApiTests {

    @Test
    void itShouldProvideLocationsByName() throws IOException, InterruptedException {
        //given
        String bodyResponse = """
                   [
                     {
                         "name": "Rome",
                         "local_names": {
                             "kk": "Рим",
                             "ko": "로마",
                             "ks": "روم",
                             "sh": "Rim",
                             "ne": "रोम",
                             "sr": "Рим",
                             "tr": "Roma",
                             "hu": "Róma",
                             "ln": "Roma",
                             "ay": "Roma",
                             "ku": "Roma",
                             "ta": "உரோமை நகரம்",
                             "de": "Rom",
                             "eo": "Romo",
                             "fi": "Rooma",
                             "li": "Roeme",
                             "ar": "روما",
                             "gd": "An Ròimh",
                             "cu": "Римъ",
                             "ro": "Roma",
                             "az": "Roma",
                             "sd": "روم",
                             "qu": "Roma",
                             "sk": "Rím",
                             "se": "Roma",
                             "ga": "An Róimh",
                             "mk": "Рим",
                             "my": "ရောမမြို့",
                             "tg": "Рим",
                             "os": "Ром",
                             "mn": "Ром",
                             "gl": "Roma",
                             "rn": "Roma",
                             "vi": "Rô-ma",
                             "gv": "Yn Raue",
                             "fy": "Rome",
                             "tl": "Roma",
                             "mr": "रोम",
                             "bo": "རོ་མ།",
                             "cy": "Rhufain",
                             "cs": "Řím",
                             "oc": "Roma",
                             "mt": "Ruma",
                             "th": "โรม",
                             "kl": "Roma",
                             "tt": "Рим",
                             "el": "Ρώμη",
                             "bs": "Rim",
                             "la": "Roma",
                             "ka": "რომი",
                             "lt": "Roma",
                             "mi": "Rōma",
                             "kv": "Рим",
                             "is": "Róm",
                             "en": "Rome",
                             "an": "Roma",
                             "ml": "റോം",
                             "pt": "Roma",
                             "tk": "Rim",
                             "hy": "Հռոմ",
                             "yi": "רוים",
                             "cv": "Рим",
                             "ie": "Roma",
                             "he": "רומא",
                             "nn": "Roma",
                             "yo": "Rómù",
                             "br": "Roma",
                             "ru": "Рим",
                             "be": "Рым",
                             "su": "Roma",
                             "da": "Rom",
                             "af": "Rome",
                             "rm": "Roma",
                             "co": "Roma",
                             "uk": "Рим",
                             "vo": "Roma",
                             "ba": "Рим",
                             "nl": "Rome",
                             "bn": "রোম",
                             "hr": "Rim",
                             "ug": "رىم",
                             "so": "Roma",
                             "am": "ሮማ",
                             "ty": "Roma",
                             "hi": "रोम",
                             "te": "రోమ్",
                             "it": "Roma",
                             "na": "Roma",
                             "ca": "Roma",
                             "kn": "ರೋಮ",
                             "uz": "Rim",
                             "sl": "Rim",
                             "ur": "روم",
                             "sw": "Roma",
                             "id": "Roma",
                             "fo": "Róm",
                             "sc": "Roma",
                             "fa": "رم",
                             "mg": "Roma",
                             "ja": "ローマ",
                             "bi": "Rome",
                             "gn": "Róma",
                             "si": "රෝමය",
                             "io": "Roma",
                             "rw": "Roma",
                             "ee": "Rome",
                             "sq": "Roma",
                             "cr": "ᖌᒪ",
                             "lb": "Roum",
                             "sa": "रोमा",
                             "jv": "Roma",
                             "et": "Rooma",
                             "eu": "Erroma",
                             "lv": "Roma",
                             "fr": "Rome",
                             "es": "Roma",
                             "ht": "Ròm",
                             "sv": "Rom",
                             "pl": "Rzym",
                             "no": "Roma",
                             "ia": "Roma",
                             "sg": "Rome",
                             "bg": "Рим",
                             "zh": "羅馬/罗马"
                         },
                         "lat": 41.8933203,
                         "lon": 12.4829321,
                         "country": "IT",
                         "state": "Lazio"
                     },
                     {
                         "name": "Rém",
                         "local_names": {
                             "hr": "Rim",
                             "ru": "Рем",
                             "hu": "Rém"
                         },
                         "lat": 46.2446558,
                         "lon": 19.1448334,
                         "country": "HU"
                     },
                     {
                         "name": "Rim",
                         "lat": 45.3899775,
                         "lon": 14.0401779,
                         "country": "HR"
                     },
                     {
                         "name": "Rim",
                         "lat": 45.4208501,
                         "lon": 15.2088117,
                         "country": "HR"
                     },
                     {
                         "name": "Rim",
                         "lat": 45.5404582,
                         "lon": 15.2934408,
                         "country": "SI"
                     }
                 ]
                """;
        URI uri = OpenWeatherApiService.buildUriForGeocodingRequest("Rome");
        HttpClient httpClient = this.createHttpClient(uri, bodyResponse, 200);

        OpenWeatherApiService openWeatherApiService = new OpenWeatherApiService(httpClient);

        //when
        List<LocationApiDto> locations = openWeatherApiService.findLocationsBy("Rome");

        //then
        assertEquals(5, locations.size());
        assertEquals(locations.get(0).getName(), "Rome");
    }

    @Test
    void itShouldProvideWeatherByCoordinates() throws IOException, InterruptedException {
        //given
        String bodyResponse = """
                {
                    "coord": {
                        "lon": 12.4829,
                        "lat": 41.8933
                    },
                    "weather": [
                        {
                            "id": 800,
                            "main": "Clear",
                            "description": "clear sky",
                            "icon": "01d"
                        }
                    ],
                    "base": "stations",
                    "main": {
                        "temp": 306.01,
                        "feels_like": 306.71,
                        "temp_min": 303.46,
                        "temp_max": 307.58,
                        "pressure": 1009,
                        "humidity": 40,
                        "sea_level": 1009,
                        "grnd_level": 1001
                    },
                    "visibility": 10000,
                    "wind": {
                        "speed": 5.66,
                        "deg": 230
                    },
                    "clouds": {
                        "all": 0
                    },
                    "dt": 1721917272,
                    "sys": {
                        "type": 1,
                        "id": 6796,
                        "country": "IT",
                        "sunrise": 1721879823,
                        "sunset": 1721932549
                    },
                    "timezone": 7200,
                    "id": 3169070,
                    "name": "Rome",
                    "cod": 200
                }
                """;
        URI uri = OpenWeatherApiService.buildUriForWeatherRequest(
                new BigDecimal("41.8933203"), new BigDecimal("12.4829321")
        );
        int statusCode = 200;
        HttpClient httpClient = this.createHttpClient(uri, bodyResponse, statusCode);
        OpenWeatherApiService openWeatherApiService = new OpenWeatherApiService(httpClient);

        //when
        WeatherApiResponse weather = openWeatherApiService.getWeatherBy(
                new BigDecimal("41.8933203"), new BigDecimal("12.4829321")
        );

        //then
        assertNotNull(weather);
        assertEquals(weather.getMain().getTemperature(), 306.01);
    }

    @Test
    void itShouldThrowExceptionWhenBadQueryLocation() throws IOException, InterruptedException {
        //given
        String badQueryLocation = "=";
        URI uri = OpenWeatherApiService.buildUriForGeocodingRequest("=");
        String bodyResponse = """
            {
               "cod": "400",
               "message": "bad query"
            }
        """;
        int statusCode = 400;
        HttpClient httpClient = this.createHttpClient(uri, bodyResponse, statusCode);
        OpenWeatherApiService openWeatherApiService = new OpenWeatherApiService(httpClient);

        //when and then
        OpenWeatherApiException exception = assertThrows(OpenWeatherApiException.class,
                () -> openWeatherApiService.findLocationsBy(badQueryLocation));
        assertEquals(exception.getStatusCode(), statusCode);
        assertEquals(exception.getMessage(), "bad query");
    }

    @Test
    void itShouldThrowExceptionWhenInternalServerFailure() throws IOException, InterruptedException {
        //given
        URI uri = OpenWeatherApiService.buildUriForGeocodingRequest("Rome");
        String bodyResponse = """
            {
               "cod": "500",
               "message": ""
            }
        """;
        int statusCode = 500;
        HttpClient httpClient = this.createHttpClient(uri, bodyResponse, statusCode);
        OpenWeatherApiService openWeatherApiService = new OpenWeatherApiService(httpClient);

        //when and then
        OpenWeatherApiException exception = assertThrows(OpenWeatherApiException.class,
                () -> openWeatherApiService.findLocationsBy("Rome"));
        assertEquals(exception.getStatusCode(), statusCode);
    }

    private HttpClient createHttpClient(URI uri, String bodyResponse, int statusCode) throws IOException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);

        HttpRequest httpRequest = HttpRequest.newBuilder(uri).build();
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(statusCode);
        when(mockResponse.body()).thenReturn(bodyResponse);
        when(httpClient.send(httpRequest,  HttpResponse.BodyHandlers.ofString())).thenReturn(mockResponse);
        return httpClient;
    }
}
