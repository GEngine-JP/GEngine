package info.xiaomo.gengine.config.converters;


import com.alibaba.druid.util.StringUtils;
import info.xiaomo.gengine.config.IConverter;
import info.xiaomo.gengine.utils.Cast;
import info.xiaomo.gengine.utils.SymbolUtil;

/**
 * 转换成二维数组
 * a#b;c#d;e#f
 *
 * @author 杨 强
 */
public class JinHaoAndFenHaoArrayConverter implements IConverter {

	@Override
	public int[][] convert(Object source) {
		String strVlaue = (String) source;
		if (StringUtils.isEmpty(strVlaue)) {
			return new int[0][0];
		} else {
			String[] allArray = strVlaue.split(SymbolUtil.FENHAO);
			int[][] request = new int[allArray.length][];
			for (int i = 0; i < allArray.length; i++) {
				String[] itemArray = allArray[i].split(SymbolUtil.JINHAO);
				int[] req = new int[itemArray.length];
				for (int j = 0; j < itemArray.length; j++) {
					req[j] = Cast.toInteger(itemArray[j]);
				}
				request[i] = req;
			}
			return request;
		}

	}

}
