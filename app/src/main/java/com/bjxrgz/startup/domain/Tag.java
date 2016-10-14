package com.bjxrgz.startup.domain;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by JiangZhiGuo on 2016/7/4.
 * describe 标签
 */
public class Tag implements Serializable {

    private String id; // 标签ID
    private String name; // 标签
    private String localName; // 标签 – 本地名称，如果该值为空，使用 name
    private String image; // 图片

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getShowName() {
        if (TextUtils.isEmpty(localName)) {
            return name;
        } else {
            return localName;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        if (id != null ? !id.equals(tag.id) : tag.id != null) return false;
        if (name != null ? !name.equals(tag.name) : tag.name != null) return false;
        return localName != null ? localName.equals(tag.localName) : tag.localName == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (localName != null ? localName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", localName='" + localName + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
