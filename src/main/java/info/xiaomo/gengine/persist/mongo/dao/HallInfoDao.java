/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.gengine.persist.mongo.dao;

import info.xiaomo.gengine.entity.BaseHallInfo;
import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

/** 大厅信息 */
public class HallInfoDao extends BasicDAO<BaseHallInfo, Integer> {

  private static volatile HallInfoDao hallInfoDao;

  private HallInfoDao(AbsMongoManager mongoManager) {
    super(
        BaseHallInfo.class,
        mongoManager.getMongoClient(),
        mongoManager.getMorphia(),
        mongoManager.getMongoConfig().getDbName());
  }

  public static HallInfoDao getDB(AbsMongoManager mongoManager) {
    if (hallInfoDao == null) {
      synchronized (HallInfoDao.class) {
        if (hallInfoDao == null) {
          hallInfoDao = new HallInfoDao(mongoManager);
        }
      }
    }
    return hallInfoDao;
  }

  /**
   * 用户ID
   *
   * @return
   */
  public static long getUserId() {
    BaseHallInfo baseHallInfo = incFieldNum(1, "userIdNum");
    return baseHallInfo.getUserIdNum();
  }

  /**
   * 角色ID
   *
   * @return
   */
  public static long getRoleId() {
    BaseHallInfo baseHallInfo = incFieldNum(1, "roleIdNum");
    return baseHallInfo.getRoleIdNum();
  }

  /**
   * 增加属性的值
   *
   * @param num 增加量
   * @param fieldName 属性名称
   * @return
   */
  public static BaseHallInfo incFieldNum(int num, String fieldName) {
    Query<BaseHallInfo> query = hallInfoDao.createQuery();
    UpdateOperations<BaseHallInfo> updateOperations =
        hallInfoDao.createUpdateOperations().inc(fieldName, num);
    BaseHallInfo baseHallInfo = hallInfoDao.getDs().findAndModify(query, updateOperations);
    if (baseHallInfo == null) {
      baseHallInfo = new BaseHallInfo();
      hallInfoDao.save(baseHallInfo);
      baseHallInfo = hallInfoDao.getDs().findAndModify(query, updateOperations);
    }
    return baseHallInfo;
  }
}
