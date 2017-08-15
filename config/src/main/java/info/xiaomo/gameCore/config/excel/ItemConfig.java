package info.xiaomo.gameCore.config.excel;

import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;

@Data
public class ItemConfig {
	/**
	 * 道具id，所有道具的唯一编号，不可重复。（可能存在同名、相同效果的道具，但是绝对不能存在相同id的道具。）
	 * 装备：1+职业（1战2法3道4通用）+11+cls（2位）+等级（2位） 道具：（绑定2，不绑定则没有）+2+cls（2位）+编号（3位）
	 */
	private int itemid;
	/**
	 * 道具名(最多不多于7个汉字)
	 */
	private String name;
	/**
	 * 道具的品质(1，白色；2，绿色；3，蓝色；4，紫色；5，橙色；6，红色；7唯一道具专属品质。)
	 */
	private int quality;
	/**
	 * 道具的说明，只有itemtype为2的道具才会有，内容位于下图图示位置：支持html代码改变颜色。
	 */
	private String tips1;
	/**
	 * 道具产出途径说明，内容位于下图图示位置：支持html代码改变颜色。
	 */
	private String tips2;
	/**
     *
     */
	private String icon;
	/**
    *
    */
	private String flash;
	/**
    *
    */
	private String model;
	/**
    *
    */
	private int dropModel;
	/**
	 * 掉落时间，道具掉落到地上之后持续的时间，超过时间后会被系统自动删除掉。（时间单位为毫秒，一般道具默认为2分钟，配置最好不要超过5分钟）
	 */
	private int droptime;
	/**
	 * 掉落最低(现在已无效)
	 */
	private int dropMinLev;
	/**
	 * 最高等级（现在已无效）
	 */
	private int dropMaxLev;
	/**
	 * 道具类型，0是货币；1是装备；2是道具
	 */
	private int itemtype;
	/**
     *
     */
	private int level;
	/**
	 * 无效字段
	 */
	private int cslevel;
	/**
	 * 若itemtype为1，则cls代表装备部位；若itemtype为2，则cls代表道具类型
	 */
	private int cls;
	/**
	 * 道具特殊效果类型
	 */
	private int buffertype;
	/**
	 * 道具特殊效果参数，具体需要对应特殊效果类型
	 */
	private int[] bufferparam;
	/**
	 * 使用等级（玩家等级超过一转后需要配合reinlv使用）
	 */
	private int uselv;
	/**
	 * 道具使用间隔（是否有使用间隔）1为有，0为没有
	 */
	private int interval;
	/**
	 * 无效字段
	 */
	private int timeout;
	/**
	 * 是否可出售 1 为 是 0为否
	 */
	private int sell;
	/**
	 * 出售价格，默认出售为绑定金币，显示在下图位置
	 */
	private int price;
	/**
	 * 掉落价值（无效）
	 */
	private int dropvalue;
	/**
	 * 是否唯一，1为是 0为否
	 */
	private int onlyone;
	/**
	 * 是否可丢弃，1为是 0为否
	 */
	private int isdelete;
	/**
	 * 绑定（包含掉落、丢弃、玩家间交易、摆摊4个部分）：1为是 0为否
	 */
	private int bind;
	/**
	 * 背包一个位置的堆叠数
	 */
	private int overlying;
	/**
	 * itemtype为1时代表 耐久； itemtype为2，cls为7时，代表是否随机；1为随机，0为不随机
	 */
	private int durable;
	/**
	 * 职业 ，0为通用，1为战士，2为法师，3为道士
	 */
	private int career;
	/**
	 * 性别，0为女，1为男，2为通用
	 */
	private int sex;
	/**
	 * 暴击伤害，单位为1%，特殊属性
	 */
	private int critRate;
	/**
	 * 暴击点数
	 */
	private int critical;
	/**
	 * 物攻最小值
	 */
	private int phyAtt;
	/**
	 * 物攻最大值
	 */
	private int phyAttMax;
	/**
	 * 魔攻最小值
	 */
	private int magicAtt;
	/**
	 * 魔攻最大值
	 */
	private int magicAttMax;
	/**
	 * 道攻最小值
	 */
	private int taoAtt;
	/**
	 * 道攻最大值
	 */
	private int taoAttMax;
	/**
	 * 物防最小值
	 */
	private int phyDef;
	/**
	 * 物防最大值
	 */
	private int phyDefMax;
	/**
	 * 魔防最小值
	 */
	private int magicDef;
	/**
	 * 魔防最大值
	 */
	private int magicDefMax;
	/**
	 * 准确，特殊属性
	 */
	private int accurate;
	/**
	 * 攻速，特殊属性
	 */
	private int attackSpeed;
	/**
	 * 闪避，特殊属性
	 */
	private int dodge;
	/**
	 * 生命值
	 */
	private int hp;
	/**
	 * 魔法值
	 */
	private int mp;
	/**
	 * 内力
	 */
	private int neili;
	/**
	 * 神圣攻击
	 */
	private int holyAtt;
	/**
	 * 生命恢复，特殊属性
	 */
	private int heathRecover;
	/**
	 * 魔法恢复，特殊属性
	 */
	private int magicRecover;
	/**
	 * 中毒恢复（无效）
	 */
	private int poisonRecover;
	/**
	 * 使用间隔时间 单位为秒
	 */
	private int usecd;
	/**
	 * 洗练价值（无效
	 */
	private int xlvalue;
	/**
	 * 转生等级 （现在只做了药品类的转生等级限制）
	 */
	private int reinlv;
	/**
	 * 特效类型
	 */
	private int effect;
	/**
	 * 幸运 ，特殊属性
	 */
	private int luck;
	/**
	 * 特效类型
	 */
	private int efftype;
	/**
	 * 宝石类型 1， 物攻；2物防；3，魔攻；4，魔防；5，道攻；6，生命；7，魔法；8，特殊宝石
	 */
	private int bstype;
	/**
	 * 掉落时是否公告 1为有，2为没有
	 */
	private int cast;
	/**
	 * 战力
	 */
	private int nbvalue;
	/**
	 * 传说神器等级
	 */
	private int shenqi;
	/**
	 * 是否不自动打开（宝箱类专用）
	 */
	private int autonopen;
	/**
	 * 套装所属ID 相同id的套装直接继承套装属性。
	 */
	private int suitid;
	/**
	 * 是否可批量使用
	 */
	private int ispl;
	/**
	 * 混乱伤害，只跟混乱防御相加减的额外伤害。不受所有系统加成
	 */
	private int pureatk;
	/**
	 * 混乱防御，只跟混乱伤害相加减的绝对免伤。不受所有系统加成
	 */
	private int puredef;
	/**
	 * 掉落公告
	 */
	private int diaoluo;
	/**
	 * 无效
	 */
	private int flag;
	/**
	 * 面板链接
	 */
	private String link;
	/**
    *
    */
	private String light;
	/**
	 * 套装等级，同suitid的套装suitlevel高的套装效果覆盖低的
	 */
	private int suitlevel;
	/**
	 * 任务类型
	 */
	private int task;
	/**
	 * 道具增加熟练度
	 */
	private HashMap<Integer, Integer> skilladd;

	/**
	 * 帮会捐献获得帮贡和资金比例
	 */
	private String uniondonate;
	/**
	 * PK伤害
	 */
	private int pkatt;
	/**
	 * pk防御
	 */
	private int pkdef;

	private int pinfen;
	/**
	 * 1 ， 2 ，3 打捆药
	 */
	private int boxid;

	private int fly;

	private int log;
	/**
	 * 道具使用次数限制 类型#次数（1，每天，2永久）
	 */
	private int[] timelimit;

	/**
	 * buffid用来处理buff叠加替换规则
	 */
	private int buffid;
	/**
	 * 最大强化等级
	 */
	private int qhmax;
	/**
	 * 仓库背包排序字段 
	 */
	private int bagorder;

	@Override
	public String toString() {
		return "ItemConfig{" + "itemid=" + itemid + ", name='" + name + '\'' + ", quality=" + quality + ", tips1='" + tips1 + '\'' + ", tips2='" + tips2 + '\'' + ", icon='" + icon + '\'' + ", flash='" + flash + '\'' + ", model='" + model + '\'' + ", dropModel=" + dropModel + ", droptime=" + droptime + ", dropMinLev=" + dropMinLev + ", dropMaxLev=" + dropMaxLev + ", itemtype=" + itemtype + ", level=" + level + ", cslevel=" + cslevel + ", cls=" + cls + ", buffertype=" + buffertype + ", bufferparam=" + Arrays.toString(bufferparam) + ", uselv=" + uselv + ", interval=" + interval + ", timeout=" + timeout + ", sell=" + sell + ", price=" + price + ", dropvalue=" + dropvalue + ", onlyone=" + onlyone + ", isdelete=" + isdelete + ", bind=" + bind + ", overlying=" + overlying + ", durable=" + durable + ", career=" + career + ", sex=" + sex + ", critRate=" + critRate + ", critical=" + critical + ", phyAtt=" + phyAtt + ", phyAttMax=" + phyAttMax + ", magicAtt=" + magicAtt + ", magicAttMax=" + magicAttMax + ", taoAtt=" + taoAtt + ", taoAttMax=" + taoAttMax + ", phyDef=" + phyDef + ", phyDefMax=" + phyDefMax + ", magicDef=" + magicDef + ", magicDefMax=" + magicDefMax + ", accurate=" + accurate + ", attackSpeed=" + attackSpeed + ", dodge=" + dodge + ", hp=" + hp + ", mp=" + mp + ", neili=" + neili + ", holyAtt=" + holyAtt + ", heathRecover=" + heathRecover + ", magicRecover=" + magicRecover + ", poisonRecover=" + poisonRecover + ", usecd=" + usecd + ", xlvalue=" + xlvalue + ", reinlv=" + reinlv + ", effect=" + effect + ", luck=" + luck + ", efftype=" + efftype + ", bstype=" + bstype + ", cast=" + cast + ", nbvalue=" + nbvalue + ", shenqi=" + shenqi + ", autonopen=" + autonopen + ", suitid=" + suitid + ", ispl=" + ispl + ", pureatk=" + pureatk + ", puredef=" + puredef + ", diaoluo=" + diaoluo + ", flag=" + flag + ", link='" + link + '\'' + ", light='" + light + '\'' + ", suitlevel=" + suitlevel + ", task=" + task + ", skilladd=" + skilladd + ", uniondonate='" + uniondonate + '\'' + ", pkatt=" + pkatt + ", pkdef=" + pkdef + ", pinfen=" + pinfen + ", boxid=" + boxid + ", fly=" + fly + ", log=" + log + ", timelimit=" + Arrays.toString(timelimit) + ", buffid=" + buffid + ", qhmax=" + qhmax + ", bagorder=" + bagorder + '}';
	}
}