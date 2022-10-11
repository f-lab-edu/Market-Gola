package com.flab.marketgola.product.dto.request;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.domain.ProductCategory;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateDisplayProductRequestDto {

    @NotBlank(message = "전시용 상품의 이름을 입력하지 않았습니다.")
    @Size(min = 1, max = 100, message = "전시용 상품의 이름은 1자 이상 100자 이하로 입력해주세요.")
    private String name;
    @NotBlank(message = "저장된 메인 이미지의 이름을 입력하지 않았습니다.")
    private String mainImageName;
    @NotBlank(message = "저장된 상세 설명 이미지의 이름을 입력하지 않았습니다.")
    private String descriptionImageName;
    @Min(value = 1, message = "상품 카테고리를 입력하지 않았습니다.")
    private int productCategoryId;
    @Size(min = 1, message = "최소 1개의 관련된 실제 상품을 등록해야 합니다.")
    @NotNull(message = "최소 1개의 관련된 실제 상품을 등록해야 합니다.")
    @Valid
    private List<CreateProductRequestDto> products;

    @Builder
    public CreateDisplayProductRequestDto(String name, String mainImageName,
            String descriptionImageName,
            int productCategoryId, List<CreateProductRequestDto> products) {
        this.name = name;
        this.mainImageName = mainImageName;
        this.descriptionImageName = descriptionImageName;
        this.productCategoryId = productCategoryId;
        this.products = products;
    }

    public DisplayProduct toDisplayProduct() {
        List<Product> productList =
                this.products
                        .stream()
                        .map(productDto ->
                                Product.builder()
                                        .name(productDto.getName())
                                        .price(productDto.getPrice())
                                        .stock(productDto.getStock())
                                        .build())
                        .collect(Collectors.toList());

        return DisplayProduct.builder()
                .name(name)
                .mainImageName(mainImageName)
                .descriptionImageName(descriptionImageName)
                .category(ProductCategory.builder().id(productCategoryId).build())
                .products(productList)
                .build();
    }

    public List<Product> toProductsWithDisplayProduct(DisplayProduct displayProduct) {
        return products
                .stream()
                .map(productDto ->
                        Product.builder()
                                .name(productDto.getName())
                                .price(productDto.getPrice())
                                .stock(productDto.getStock())
                                .displayProduct(displayProduct)
                                .build())
                .collect(Collectors.toList());
    }
}