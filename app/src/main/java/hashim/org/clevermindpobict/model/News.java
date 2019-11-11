package hashim.org.clevermindpobict.model;

public class News {

    private String newsTitle;
    private String newsSubTitle;
    private String newsDescription;
    private String newsImage;

    public News(){}

    public News(String newsTitle, String newsSubTitle, String newsDescription, String newsImage) {
        this.newsTitle = newsTitle;
        this.newsSubTitle = newsSubTitle;
        this.newsDescription = newsDescription;
        this.newsImage = newsImage;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsSubTitle() {
        return newsSubTitle;
    }

    public void setNewsSubTitle(String newsSubTitle) {
        this.newsSubTitle = newsSubTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }
}
