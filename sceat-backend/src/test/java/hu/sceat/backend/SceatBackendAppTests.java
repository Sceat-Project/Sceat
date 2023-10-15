package hu.sceat.backend;

import hu.sceat.backend.util.ConfigureSpringTest;
import hu.sceat.backend.util.StringListArgumentConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@ConfigureSpringTest
class SceatBackendAppTests { // TODO delete
	
	@ParameterizedTest
	@CsvSource({
			"'test, elek, 123'"
	})
	void placeholder(@ConvertWith(StringListArgumentConverter.class) List<String> list) {
		Assertions.assertEquals(List.of("test", "elek", "123"), list);
	}
}
