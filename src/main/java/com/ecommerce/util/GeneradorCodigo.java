package com.ecommerce.util;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.*;

@Component
public class GeneradorCodigo {

	public String siguiente(String prefijo, Stream<String> existentes) {

		List<Integer> nums = existentes.filter(Objects::nonNull).filter(c -> c.startsWith(prefijo))
				.map(c -> c.substring(prefijo.length())).filter(s -> s.matches("\\d+")).map(Integer::parseInt).sorted()
				.toList();

		int esperado = 1;
		for (int n : nums) {
			if (n == esperado)
				esperado++;
			else if (n > esperado)
				break;
		}
		return prefijo + String.format("%06d", esperado);
	}
}
