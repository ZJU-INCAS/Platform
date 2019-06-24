package team.educoin.transaction.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 实体类和map互相转化工具类
 * @author: PandaClark
 * @create: 2019-06-24
 */
public class MyBeanMapUtil {

    public static Map<String, Object> BeanToMap(Object obj) throws Exception {
        if (obj == null) return null;

        Map<String, Object> map = new HashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property:propertyDescriptors) {
            String  key = property.getName();
            if (key.compareToIgnoreCase("class") == 0){
                // 如果对象中有另外一个对象的引用，这个引用对象直接跳过，若也要封装到map中，递归调用BeanToMap即可
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, value);
        }
        return map;
    }

}
