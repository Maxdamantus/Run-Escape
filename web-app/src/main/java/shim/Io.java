package shim;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Io<T> {
	void run(Consumer<T> onResolve, Consumer<Throwable> onReject);

	default void run() {
		run(v -> {}, e -> e.printStackTrace());
	}

	static <T> Io<T> resolve(T v) {
		return (onResolve, onReject) -> onResolve.accept(v);
	}

	static <T> Io<T> reject(Throwable e) {
		return (onResolve, onReject) -> onReject.accept(e);
	}

	static <T> Io<T> defer(Supplier<Io<T>> s) {
		return resolve(null).flatMap(v -> s.get());
	}

	static Io<Void> par0(Consumer<Par0> c) {
		return (onResolve, onReject) -> {
			var par = new Par0(onResolve, onReject);
			try {
				c.accept(par);
			} catch (Throwable t) {
				onReject.accept(t);
			}
			par.doneInit();
		};
	}

	default <U> Io<U> map(Function<T, U> fn) {
		return (onResolve, onReject) -> {
			run(v -> {
				U u;
				try {
					u = fn.apply(v);
				} catch (Throwable t) {
					onReject.accept(t);
					return;
				}
				onResolve.accept(u);
			}, onReject);
		};
	}

	default <U> Io<U> flatMap(Function<T, Io<U>> fn) {
		return (onResolve, onReject) -> {
			run(v -> {
				Io<U> io;
				try {
					io = fn.apply(v);
				} catch (Throwable t) {
					onReject.accept(t);
					return;
				}
				io.run(onResolve, onReject);
			}, onReject);
		};
	}

	static class Par0 {
		private final Consumer<Void> onResolve;
		private final Consumer<Throwable> onReject;
		private Throwable reject;
		private int counter = -1;

		private Par0(Consumer<Void> onResolve, Consumer<Throwable> onReject) {
			this.onResolve = onResolve;
			this.onReject = onReject;
		}

		public void run(Io<Void> io) {
			if (counter >= 0)
				throw new IllegalStateException();
			counter--;
			io.run(doneResolve, doneReject);
		}

		private void done() {
			if (counter > 0)
				counter--;
			else if (counter < 0)
				counter++;
			else
				throw new IllegalStateException();
			if (counter == 0)
				if (reject != null)
					onReject.accept(reject);
				else
					onResolve.accept(null);
		}

		private final Consumer<Void> doneResolve = v -> {
			done();
		};

		private final Consumer<Throwable> doneReject = e -> {
			if (reject == null)
				reject = e != null? reject : new RuntimeException();
			done();
		};

		private void doneInit() {
			counter = -counter;
			done();
		}
	}
}
