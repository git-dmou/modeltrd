package fr.solunea.thaleia.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>CastUtils class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public class CastUtils {

	/**
	 * <p>castList.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param collection a {@link java.util.Collection} object.
	 * @return Caste chacun des éléments de la collection passée en paramètre,
	 *         en objet de classe clazz.
	 * @throws java.lang.Exception
	 *             si le cast a échoué pour un objet.
	 * @param <T> a T object.
	 */
	public static <T> List<T> castList(Collection<?> collection,
			Class<? extends T> clazz) throws Exception {
		List<T> result = new ArrayList<>(collection.size());
		for (Object o : collection) {
			try {
				result.add(clazz.cast(o));
			} catch (Exception e) {
				throw new Exception(
						"Impossible de caster l'objet vers la classe '"
								+ clazz.getName() + "' : " + e.toString());
			}
		}
		return result;
	}

	/**
	 * <p>safeLongToInt.</p>
	 *
	 * @param l a long.
	 * @return a int.
	 */
	public static int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		}
		if (l > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		return (int) l;
	}

}
