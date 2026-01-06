
package com.ecommerce.cliente.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CarritoItemRequest(@NotNull Integer idProducto, @NotNull @Min(1) Integer cantidad) {
}
