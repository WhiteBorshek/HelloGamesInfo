import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GameReviewSearch {
    private static final int MAX_LINKS_TO_DISPLAY = 2; // Максимальное число ссылок для вывода

    public static void searchAndPrintGameReviews(String gameTitle) {
        List<String> sites = new ArrayList<>();
        sites.add("site:stopgame.ru");
        sites.add("site:igromania.ru");
        sites.add("site:kanobu.ru");
        sites.add("site:ixbt.games");
        sites.add("site:shazoo.ru");

        String apiKey = "AIzaSyA_HzsUDh3zBZQ0BSvanitz95wSJwmStD4";
        String searchEngineId = "b44eb0b8f943c4d17";
        String keywords = "\"обзор\" OR \"рецензия\" OR \"отзыв\" OR \"обзоры\" OR \"оценки\"";

        try {
            for (String site : sites) {
                JSONObject searchResults = searchGameReviews(gameTitle, apiKey, searchEngineId, site, keywords);
                printLimitedSearchResults(gameTitle, site, searchResults, MAX_LINKS_TO_DISPLAY);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printLimitedSearchResults(String gameTitle, String site, JSONObject searchResults, int limit) {
        if (searchResults.has("items")) {
            JSONArray items = searchResults.getJSONArray("items");
            System.out.println("Результаты для игры '" + gameTitle + "' на сайте " + site + ":");
            int count = 0;
            for (int i = 0; i < items.length() && count < limit; i++) {
                JSONObject item = items.getJSONObject(i);
                String title = item.getString("title");
                String link = item.getString("link");
                if (title.toLowerCase().contains(gameTitle.toLowerCase()) && isExactGameTitle(title, gameTitle)) {
                    System.out.println("Ссылка: " + link);
                    System.out.println("Заголовок: " + title);
                    System.out.println();
                    count++;
                }
            }
            System.out.println("----------------------------------------");
        } else {
            System.out.println("Результаты для игры '" + gameTitle + "' на сайте " + site + " не найдены.");
            System.out.println("----------------------------------------");
        }
    }

    private static boolean isExactGameTitle(String title, String gameTitle) {
        // Проверяем, содержит ли заголовок полное название игры без учета регистра
        return title.toLowerCase().contains(gameTitle.toLowerCase());
    }

    private static JSONObject searchGameReviews(String gameTitle, String apiKey, String searchEngineId, String site, String keywords) throws IOException {
        String cacheKey = gameTitle + "_" + site; // Создаем уникальный ключ для кэша
        if (SearchCache.containsKey(cacheKey)) {
            return SearchCache.get(cacheKey); // Возвращаем результат из кэша, если он есть
        } else {
            // Если результат отсутствует в кэше, делаем запрос к Google API
            String encodedGameTitle = URLEncoder.encode(gameTitle, "UTF-8");
            String encodedKeywords = URLEncoder.encode(keywords, "UTF-8");
            String apiUrl = "https://www.googleapis.com/customsearch/v1?key=" + apiKey +
                    "&cx=" + searchEngineId +
                    "&q=" + site + "%20" + encodedGameTitle + "%20" + encodedKeywords +
                    "&fields=items(link,title)";


            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject searchResults = new JSONObject(response.toString());

            // Сохраняем результат в кэше
            SearchCache.put(cacheKey, searchResults);

            return searchResults;
        }
    }
    }
