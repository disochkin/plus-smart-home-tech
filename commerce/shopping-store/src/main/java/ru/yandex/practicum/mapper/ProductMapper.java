package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.product.NewProductDto;
import ru.yandex.practicum.dto.product.ProductShortDto;
import ru.yandex.practicum.dto.product.UpdateProductDto;
import ru.yandex.practicum.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageSrc", source = "imageSrc")
    //@Mapping(target = "quantityState", source = "quantity",  qualifiedByName = "mapQuantity")
    @Mapping(target = "quantityState", source = "quantityState")
    @Mapping(target = "productState", source = "productState")
    @Mapping(target = "price", source = "price")
    ProductShortDto toShortDto(Product product);

//    @Named("mapQuantity")
//    default QuantityState mapQuantity(Integer quantity) {
//        if (quantity == 0) return QuantityState.ENDED;
//        if (quantity < 10) return QuantityState.FEW;
//        if (quantity < 100) return QuantityState.MANY;
//        return QuantityState.ENOUGN;
//    }

    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageSrc", source = "imageSrc")
    @Mapping(target = "quantityState", source = "quantityState")
    @Mapping(target = "productState", source = "productState")
    @Mapping(target = "price", source = "price")
    Product toProduct(NewProductDto newProductDto);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName", conditionExpression = "java(updateProductDto.hasProductName())")
    @Mapping(target = "description", source = "description", conditionExpression = "java(updateProductDto.hasDescription())")
    @Mapping(target = "imageSrc", source = "imageSrc", conditionExpression = "java(updateProductDto.hasImageSrc())")
    @Mapping(target = "quantityState", source = "quantityState", conditionExpression = "java(updateProductDto.hasProductState())")
    @Mapping(target = "price", source = "price", conditionExpression = "java(updateProductDto.hasPrice())")
    Product updateProductToProduct(UpdateProductDto updateProductDto);
}





