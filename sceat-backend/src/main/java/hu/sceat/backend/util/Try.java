package hu.sceat.backend.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Much like the {@link Optional} class, this is a monadic container.
 * It holds either a success value or an error value.
 *
 * <p>
 * A typical use case of this class is to handle some input, partially processing it in small steps
 * and defining different errors for each step.
 * Then finally the fully processed value or the first error can be retrieved.
 * </p>
 *
 * @param <T> type of the real, non-error (success) value
 * @param <E> type of the error
 */
public abstract class Try<T, E> {
	
	public static <T, E> Try<T, E> success(T value) {
		return new Success<>(value);
	}
	
	public static <T, E> Try<T, E> error(E error) {
		return new Error<>(error);
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static <T, E> Try<T, E> from(Optional<? extends T> value, E error) {
		return value.<Try<T, E>>map(Try::success).orElseGet(() -> error(error));
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static <T, E> Try<T, E> fromGet(Optional<? extends T> value, Supplier<? extends E> error) {
		return value.<Try<T, E>>map(Try::success).orElseGet(() -> error(error.get()));
	}
	
	private Try() {} //prevent subclassing outside of this file
	
	public final boolean isSuccess() {return value().isPresent();}
	
	public final boolean isError() {return !isSuccess();}
	
	public abstract <R> R get(Function<? super T, ? extends R> ft, Function<? super E, ? extends R> fe);
	
	public abstract Optional<T> value();
	
	public abstract Optional<E> error();
	
	public final <NT> Try<NT, E> map(Function<? super T, ? extends NT> f) {
		return get(t -> Try.success(f.apply(t)), Try::error);
	}
	
	public final <NT> Try<NT, E> flatMap(Function<? super T, ? extends Try<NT, E>> f) {
		return get(f, Try::error);
	}
	
	public final <NT> Try<NT, E> flatMap(Function<? super T, Optional<? extends NT>> f, E error) {
		return flatMapGet(f, () -> error);
	}
	
	public final <NT> Try<NT, E> flatMapGet(Function<? super T, Optional<? extends NT>> f,
			Supplier<? extends E> error) {
		return flatMap(t -> f.apply(t)
				.<Try<NT, E>>map(Try::success)
				.orElseGet(() -> Try.error(error.get())));
	}
	
	public final T orElse(T other) {
		return value().orElse(other);
	}
	
	public final T orElseGet(Supplier<? extends T> f) {
		return value().orElseGet(f);
	}
	
	public final T orElseGet(Function<? super E, ? extends T> f) {
		return get(Function.identity(), f);
	}
	
	public final T orElseThrow() {
		return get(Function.identity(), e -> {
			throw new NoSuchElementException(getClass().getSimpleName() + " contains error: " + e);
		});
	}
	
	public final <X extends Throwable> T orElseThrow(Supplier<? extends X> f) throws X {
		return value().orElseThrow(f);
	}
	
	public final <X extends Throwable> T orElseThrow(Function<? super E, ? extends X> f) throws X {
		//noinspection OptionalGetWithoutIsPresent
		return value().orElseThrow(() -> f.apply(error().get()));
	}
	
	public final <NE> Try<T, NE> mapError(Function<? super E, ? extends NE> f) {
		return get(Try::success, e -> Try.error(f.apply(e)));
	}
	
	public final Try<T, E> filter(Predicate<? super T> f, E error) {
		return flatMap(t -> f.test(t) ? Try.success(t) : Try.error(error));
	}
	
	public final Try<T, E> filterGet(Predicate<? super T> f, Supplier<? extends E> error) {
		return flatMap(t -> f.test(t) ? Try.success(t) : Try.error(error.get()));
	}
	
	public final Try<T, E> peek(Consumer<? super T> f) {
		value().ifPresent(f);
		return this;
	}
	
	public final Try<T, E> peekError(Consumer<? super E> f) {
		error().ifPresent(f);
		return this;
	}
	
	private static final class Success<T, E> extends Try<T, E> {
		private final T value;
		
		private Success(T value) {
			Objects.requireNonNull(value);
			this.value = value;
		}
		
		@Override
		public boolean equals(Object o) {
			return o instanceof Try.Success<?, ?> other && Objects.equals(value, other.value);
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return "TrySuccess{" + value + '}';
		}
		
		@Override
		public <R> R get(Function<? super T, ? extends R> ft, Function<? super E, ? extends R> fe) {
			return ft.apply(value);
		}
		
		@Override
		public Optional<T> value() {
			return Optional.of(value);
		}
		
		@Override
		public Optional<E> error() {
			return Optional.empty();
		}
	}
	
	private static final class Error<T, E> extends Try<T, E> {
		private final E error;
		
		private Error(E error) {
			Objects.requireNonNull(error);
			this.error = error;
		}
		
		@Override
		public boolean equals(Object o) {
			return o instanceof Try.Error<?, ?> other && Objects.equals(error, other.error);
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(error);
		}
		
		@Override
		public String toString() {
			return "TryError{" + error + '}';
		}
		
		@Override
		public <R> R get(Function<? super T, ? extends R> ft, Function<? super E, ? extends R> fe) {
			return fe.apply(error);
		}
		
		@Override
		public Optional<T> value() {
			return Optional.empty();
		}
		
		@Override
		public Optional<E> error() {
			return Optional.of(error);
		}
	}
}
