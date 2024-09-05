public class Game {
    private String title;
    private String genre;
    private String playType;

    public Game(String title, String genre, String playType) {
        this.title = title;
        this.genre = genre;
        this.playType = playType;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlayType() {
        return playType;
    }
    public void setPlayType(String playType) {
        this.playType = playType;
    }
}
