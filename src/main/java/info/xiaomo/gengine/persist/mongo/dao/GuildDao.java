package info.xiaomo.gengine.persist.mongo.dao;

import info.xiaomo.gengine.entity.BaseGuild;
import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * 公会
 *
 *   2017年9月22日 上午10:38:16
 */
public class GuildDao extends BasicDAO<BaseGuild, Long> {
  private static volatile GuildDao guildDao;

  public GuildDao(AbsMongoManager mongoManager) {
    super(
        BaseGuild.class,
        mongoManager.getMongoClient(),
        mongoManager.getMorphia(),
        mongoManager.getMongoConfig().getDbName());
  }

  public static GuildDao getDB(AbsMongoManager mongoManager) {
    if (guildDao == null) {
      synchronized (GuildDao.class) {
        if (guildDao == null) {
          guildDao = new GuildDao(mongoManager);
        }
      }
    }
    return guildDao;
  }

  /**
   * 存储
   *
   *   2017年9月22日 上午10:45:52
   * @param baseGuild
   */
  public static void saveGuild(BaseGuild baseGuild) {
    guildDao.save(baseGuild);
  }
}
