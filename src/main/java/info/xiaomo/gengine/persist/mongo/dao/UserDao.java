/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.gengine.persist.mongo.dao;

import info.xiaomo.gengine.entity.BaseUser;
import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

/**
 * 用戶
 *
 *
 *
 */
public class UserDao extends BasicDAO<BaseUser, Long> {

    private static volatile UserDao userDao;

    private UserDao(AbsMongoManager mongoManager) {
        super(BaseUser.class, mongoManager.getMongoClient(), mongoManager.getMorphia(), mongoManager.getMongoConfig().getDbName());
    }

    public static UserDao getDB(AbsMongoManager mongoManager) {
        if (userDao == null) {
            synchronized (UserDao.class) {
                if (userDao == null) {
                    userDao = new UserDao(mongoManager);
                }
            }
        }
        return userDao;
    }

    public static BaseUser findByAccount(String accunt) {
        Query<BaseUser> query = userDao.createQuery().filter("accunt", accunt);
        return query.get();
    }
    
    public static void saveUser(BaseUser baseUser){
        userDao.save(baseUser);
    }

}
