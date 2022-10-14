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
import lombok.Singular;

@Getter
@Setter
@NoArgsConstructor
public class UpdateDisplayProductWithProductsRequestDto {

    @Min(value = 1, message = "전시용 상품의 id를 입력하지 않았습니다.")
    @NotNull(message = "전시용 상품의 id를 입력하지 않았습니다.")
    private Long id;
    @NotBlank(message = "전시용 상품의 이름을 입력하지 않았습니다.")
    @Size(min = 1, max = 100, message = "상품의 이름은 1자 이상 100자 이하로 입력해주세요.")
    private String name;
    @NotBlank(message = "저장된 메인 이미지의 이름을 입력하지 않았습니다.")
    private String mainImageName;
    @NotBlank(message = "저장된 상세 설명 이미지의 이름을 입력하지 않았습니다.")
    private String descriptionImageName;
    @Min(value = 1, message = "상품 카테고리를 입력하지 않았습니다.")
    private int productCategoryId;
    @Valid
    private List<UpdateProductRequestDto> products;


    @Builder
    public UpdateDisplayProductWithProductsRequestDto(Long id, String name, String mainImageUrl,
            String descriptionImageUrl, int productCategoryId,
            @Singular List<UpdateProductRequestDto> products) {
        this.id = id;
        this.name = name;
        this.mainImageName = mainImageName;
        this.descriptionImageName = descriptionImageName;
        this.productCategoryId = productCategoryId;
        this.products = products;
    }

    public DisplayProduct toDisplayProduct() {
        return DisplayProduct.builder()
                .id(id)
                .mainImageName(mainImageName)
                .descriptionImageName(descriptionImageName)
                .name(name)
                .category(
                        (productCategoryId != 0) ?
                                ProductCategory.builder().id(productCategoryId).build() : null)
                .build();
    }

    public List<Product> toProducts(DisplayProduct displayProduct) {
        return products
                .stream()
                .map(productDto ->
                        Product.builder()
                                .id(productDto.getId())
                                .name(productDto.getName())
                                .price(productDto.getPrice())
                                .stock(productDto.getStock())
                                .isDeleted(false)
                                .displayProduct(displayProduct)
                                .build())
                .collect(Collectors.toList());
        }
}
