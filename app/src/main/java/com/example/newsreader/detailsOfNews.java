package com.example.newsreader;

public class detailsOfNews{
    String news;
    String url;
    public detailsOfNews(String news, String url){
        this.url = url;
        this.news = news;
    }
    public String getNews() {
        return news;
    }

    public String getUrl() {
        return url;
    }
}
