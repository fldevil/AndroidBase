package com.user.project.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Fan on 2016/11/7.
 * describe 数据库实体类 demo
 */
@Entity(nameInDb = "dao_test")
public class DaoTest {

    @Property(nameInDb = "id")
    private Long id;
    @Property(nameInDb = "name")
    private String name;

    @Generated(hash = 1217481414)
    public DaoTest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 513667828)
    public DaoTest() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
