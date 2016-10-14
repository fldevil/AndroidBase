package com.bjxrgz.startup.domain;

import java.util.List;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe
 */
public class Home {


    private List<Article> topData; // Top data，最多给10条
    private List<Tag> celebrityBioList; // tag
    private List<Tag> favoriteList; // tag
    private List<User> recommendList; // 热门
    private List<User> choiceBioList; // B精选

    public List<Tag> getCelebrityBioList() {
        return celebrityBioList;
    }

    public void setCelebrityBioList(List<Tag> celebrityBioList) {
        this.celebrityBioList = celebrityBioList;
    }

    public List<User> getChoiceBioList() {
        return choiceBioList;
    }

    public void setChoiceBioList(List<User> choiceBioList) {
        this.choiceBioList = choiceBioList;
    }

    public List<Tag> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(List<Tag> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public List<User> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<User> recommendList) {
        this.recommendList = recommendList;
    }

    public List<Article> getTopData() {
        return topData;
    }

    public void setTopData(List<Article> topData) {
        this.topData = topData;
    }

    @Override
    public String toString() {
        return "Home{" +
                "topData=" + topData +
                ", celebrityBioList=" + celebrityBioList +
                ", favoriteList=" + favoriteList +
                ", recommendList=" + recommendList +
                ", choiceBioList=" + choiceBioList +
                '}';
    }
}
