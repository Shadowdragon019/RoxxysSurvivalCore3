package lol.roxxane.roxxys_survival_core.util.fuck_off_exceptions;

import java.util.function.Supplier;

public class FuckOffExceptions {
	public static <T> T trycrash(CrashSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void trycrash(CrashRunnable runnable) {
		runnable.run();
	}
	@FunctionalInterface
	public interface CrashSupplier<T> extends Supplier<T> {
		T $() throws Exception;
		default T get() {
			try {
				return $();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	@FunctionalInterface
	public interface CrashRunnable extends Runnable {
		void $() throws Exception;
		default void run() {
			try {
				$();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}