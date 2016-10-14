package com.bjxrgz.startup.domain;

/**
 * Created by JiangZhiGuo on 2016/7/27.
 * <p>
 * describe 首页大图
 */
public class Article {

    private String mainImage;
    private String title;
    private String intro;

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Article{" +
                "intro='" + intro + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}
