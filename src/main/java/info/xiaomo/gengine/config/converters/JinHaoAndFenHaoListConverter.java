package info.xiaomo.gengine.config.converters;


import com.alibaba.druid.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import info.xiaomo.gengine.config.IConverter;
import info.xiaomo.gengine.utils.StringUtil;
import info.xiaomo.gengine.utils.SymbolUtil;

/**
 * 转换成数组List
 *
 * @author xiaomo
 */
public class JinHaoAndFenHaoListConverter implements IConverter {

    @Override
    public List<int[]> convert(Object source) {
        String strVlaue = (String) source;
        List<int[]> list = new ArrayList<>();
        if (StringUtils.isEmpty(strVlaue)) {
            return new ArrayList<>();
        } else {
            String[] allArray = strVlaue.split(SymbolUtil.FENHAO);
            for (String s : allArray) {
                String[] typeArray = s.split(SymbolUtil.JINHAO);
                list.add(StringUtil.strArrToIntArr(typeArray));
            }
        }
        return list;
    }
}