package framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import launcher.Launcher;

public class ObjectFactory {
	public static Class createClass(String name) {
		try {
			return Class.forName(name, true, Launcher.instance().getFramework()
					.getClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object createObject(String name, Class[] params,
			Object[] objects) {
		Object o = null;
		try {
			Class c = Class.forName(name, true, Launcher.instance()
					.getFramework().getClassLoader());

			Constructor constructor = c.getConstructor(params);
			o = constructor.newInstance(objects);
			if (o == null) {
				throw new NullPointerException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * Creates a class of the given name
	 *
	 * @param name -
	 *            Name of the class
	 * @return An instance of the class <I>name</I>
	 */
	public static Object createObject(String name) {
		Object o = null;
		try {
			Class c = Class.forName(name, true, Launcher.instance()
					.getFramework().getClassLoader());

			Class[] parameters = new Class[] {};

			Constructor constructor = c.getConstructor(parameters);
			o = constructor.newInstance();
			if (o == null) {
				throw new NullPointerException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;

	}

	/**
	 * Creates the singleton of the given class The function to generate the
	 * singleton must be called "instance"
	 *
	 * @param classname -
	 *            Name of the class
	 * @return An instance of the class
	 */
	public static Object getInstance(String classname) {
		Object o = null;
		try {
			Class c = Class.forName(classname, true, Launcher.instance()
					.getFramework().getClassLoader());

			Method instance_call = c.getMethod("instance", new Class[] {});

			o = instance_call.invoke(null, new Object[] {});

			if (o == null) {
				throw new NullPointerException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
}
