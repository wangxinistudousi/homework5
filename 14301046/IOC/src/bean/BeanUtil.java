package bean;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import annotation.Component;
import util.ReflectionUtils;

public class BeanUtil {
	
	private static final Map<String, String> beanClassMap = new HashMap<>();
	public static final String ANNOATE_CLS_PREFIX = "test.";
	
	public static void invokeSetterMethod( Object obj, String propertyName, Object propertyValue)
	{
		char [] tmp = propertyName.toCharArray();
		if(tmp[0] >= 'a' && tmp[0] <= 'z')
		{
			tmp[0] -= 32;
		}
		String setMethodName = String.format("set%s", String.valueOf(tmp));
		Field field;
		Class<?> cls = obj.getClass();
		try {
			field = cls.getDeclaredField(propertyName);
			Class<?> type = field.getType();
			Method method = ReflectionUtils.findMethod(cls, setMethodName, type);

			method.invoke(obj, propertyValue);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static{

		Reflections reflections = new Reflections("test");
		Set<Class<?>> annotateClasses = 
				reflections.getTypesAnnotatedWith(annotation.Component.class);
		
		for(Class<?> cls : annotateClasses){			
			Component annotateCls = (Component) cls.getAnnotation(Component.class);
			if(annotateCls != null){
				String param = ANNOATE_CLS_PREFIX + annotateCls.value();
				beanClassMap.put(param, cls.getName());

			}	
		}
	}
	
	public static String getClassName(String ref) {
		return beanClassMap.get(ANNOATE_CLS_PREFIX + ref);
	}
	
	public static void getPropertyvalue(Object obj, String propertyName, Object propertyValue){
		Field field;
		Class<?> cls = obj.getClass();
		try {
			field = cls.getDeclaredField(propertyName);
			field.setAccessible(true);
			field.set(obj, propertyValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
