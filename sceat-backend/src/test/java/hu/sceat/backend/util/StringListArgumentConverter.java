package hu.sceat.backend.util;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import java.util.Arrays;
import java.util.List;

public class StringListArgumentConverter extends TypedArgumentConverter<String, List<String>> {
	
	public StringListArgumentConverter() {
		//noinspection unchecked
		super(String.class, (Class<List<String>>) (Class<?>) List.class);
	}
	
	@Override
	protected List<String> convert(String source) throws ArgumentConversionException {
		// "" -> [""]
		// "," -> []
		return Arrays.asList(source.split(", *"));
	}
}
