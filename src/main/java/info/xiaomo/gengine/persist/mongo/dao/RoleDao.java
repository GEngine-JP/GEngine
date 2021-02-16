/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.gengine.persist.mongo.dao;

import info.xiaomo.gengine.common.struct.Role;
import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * 角色
 *
 *
 *
 */
public class RoleDao extends BasicDAO<Role, Long> {

	private static volatile RoleDao roleDao;

	private RoleDao(AbsMongoManager mongoManager) {
		super(Role.class, mongoManager.getMongoClient(), mongoManager.getMorphia(),
				mongoManager.getMongoConfig().getDbName());
	}

	public static RoleDao getDB(AbsMongoManager mongoManager) {
		if (roleDao == null) {
			synchronized (RoleDao.class) {
				if (roleDao == null) {
					roleDao = new RoleDao(mongoManager);
				}
			}
		}
		return roleDao;
	}

	public static Role getRoleByUserId(long userId) {
		Role role = roleDao.createQuery().filter("userId", userId).get();
		return role;
	}

	public static void saveRole(Role role) {
		roleDao.save(role);
	}

}
