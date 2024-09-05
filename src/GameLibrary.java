import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameLibrary {
    private Map<String, List<Game>> gamesByGenre;
    private List<String> displayedRandomGames;
    private Map<String, String> genreAliases;
    private Map<String, String> playTypeAliases;

    public GameLibrary() {
        gamesByGenre = new HashMap<>();
        displayedRandomGames = new ArrayList<>();
        genreAliases = new HashMap<>();
        playTypeAliases = new HashMap<>();
        initializeGenreAliases();
        initializePlayTypeAliases();
    }

    private void initializeGenreAliases() {
        genreAliases.put("Action", "Экшн");
        genreAliases.put("RPG", "РПГ");
        genreAliases.put("Strategy", "Стратегия");
        genreAliases.put("Adventure", "Приключения");
        genreAliases.put("Simulator", "Симулятор");
        genreAliases.put("Racing", "Гонки");
        genreAliases.put("Sports", "Спорт");
        genreAliases.put("Quest", "Квест");
        genreAliases.put("Platformer", "Платформер");
        genreAliases.put("Horror", "Ужас");
        genreAliases.put("Fighting", "Файтинг");
        genreAliases.put("Beat 'em up", "Драки");
        genreAliases.put("Shooter", "Шутер");
        genreAliases.put("MMO", "ММО");
    }

    private void initializePlayTypeAliases() {
        playTypeAliases.put("Single", "Один");
        playTypeAliases.put("Multi", "Много");
    }

    public void addGame(String genre, String title, String playType) {
        Game game = new Game(title, genre, playType);
        String actualGenre = genreAliases.getOrDefault(genre, genre);
        String actualPlayType = playTypeAliases.getOrDefault(playType, playType);
        game.setPlayType(actualPlayType);
        gamesByGenre.computeIfAbsent(actualGenre, k -> new ArrayList<>()).add(game);
    }

    public Game getRandomGameByGenre(String genre, String playType) {
        String actualGenre = genreAliases.getOrDefault(genre, genre);
        String actualPlayType = playTypeAliases.getOrDefault(playType, playType);
        List<Game> games = getGamesByParameters(actualGenre, actualPlayType);
        if (games != null && !games.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(games.size());
            return games.get(randomIndex);
        }
        return null;
    }

    public void printGamesByParameters(String genre, String playType) {
        String actualGenre = genreAliases.getOrDefault(genre, genre);
        String actualPlayType = playTypeAliases.getOrDefault(playType, playType);
        List<Game> games = getGamesByParameters(actualGenre, actualPlayType);
        if (!games.isEmpty()) {
            System.out.println("Игры в жанре " + actualGenre + ", тип игры " + actualPlayType + ":");
            Game randomGame = getRandomGameByGenre(actualGenre, actualPlayType);
            if (randomGame != null) {
                System.out.println("Название игры: " + randomGame.getTitle());
                System.out.println("Жанр: " + randomGame.getGenre());
                System.out.println("Тип игры: " + randomGame.getPlayType());
                GameReviewSearch.searchAndPrintGameReviews(randomGame.getTitle());
                displayedRandomGames.add(genre);
            }
        } else {
            System.out.println("Нет игр в жанре " + actualGenre + ", тип игры " + actualPlayType);
        }
    }


    public List<Game> getGamesByParameters(String genre, String playType) {
        List<Game> result = new ArrayList<>();
        List<Game> games = gamesByGenre.get(genre);
        if (games != null) {
            for (Game game : games) {
                if (game.getPlayType().equalsIgnoreCase(playType)) {
                    result.add(game);
                }
            }
        }
        return result;
    }
}
