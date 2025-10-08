package lol.roxxane.roxxys_survival_core.util.fuck_off_exceptions;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("CallToPrintStackTrace")
public class FuckOffExceptions {
	public static <T> T trycrash(CrashSupplier<T> supplier) {
		return supplier.get();
	}
	public static void trycrash(CrashRunnable runnable) {
		runnable.run();
	}
	public static <T> CrashConsumer<T> trycrash(CrashConsumer<T> consumer) {
		return consumer;
	}
	public static void trylog(LogRunnable runnable) {
		runnable.run();
	}
	public static <T> LogConsumer<T> trylog(LogConsumer<T> consumer) {
		return consumer;
	}
	public static boolean doesRun(CrashRunnable runnable) {
		try {
			runnable.run();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean doesCrash(CrashRunnable runnable) {
		try {
			runnable.run();
			return false;
		} catch (Exception e) {
			return true;
		}
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
	@FunctionalInterface
	public interface CrashConsumer<T> extends Consumer<T> {
		void $(T t) throws Exception;
		default void accept(T t) {
			try {
				$(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	@FunctionalInterface
	public interface LogRunnable extends Runnable {
		void $() throws Exception;
		default void run() {
			try {
				$();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@FunctionalInterface
	public interface LogConsumer<T> extends Consumer<T> {
		void $(T t) throws Exception;
		default void accept(T t) {
			try {
				$(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}