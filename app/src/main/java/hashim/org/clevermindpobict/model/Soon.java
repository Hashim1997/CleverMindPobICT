package hashim.org.clevermindpobict.model;

public class Soon {
    private String idSoon;
    private String Title;

    public Soon() {}

    public Soon(String id, String title) {
        this.idSoon = id;
        Title = title;
    }

    public String getIdSoon() {
        return idSoon;
    }

    public void setIdSoon(String idSoon) {
        this.idSoon = idSoon;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
