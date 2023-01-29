package com.flab.marketgola.product.dto.response;

import com.flab.marketgola.product.domain.DisplayProduct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class DisplayProductListResponseDto {

    private List<DisplayProductListData> data = new ArrayList<>();
    private Pagination meta;

    @Builder
    public DisplayProductListResponseDto(List<DisplayProductListData> data, Pagination meta) {
        this.data = data;
        this.meta = meta;
    }

    public static DisplayProductListResponseDto of(Page<DisplayProduct> page) {

        List<DisplayProductListData> data = page.getContent().stream()
                .map(DisplayProductListData::of)
                .collect(Collectors.toList());

        Pagination meta = new Pagination(page.getNumber() + 1, page.getTotalElements(),
                page.getTotalPages());

        return DisplayProductListResponseDto.builder()
                .data(data)
                .meta(meta)
                .build();
    }

    @Getter
    public static class DisplayProductListData {

        private long id;
        private String name;
        private String mainImageUrl;
        private int price;

        @Builder
        public DisplayProductListData(long id, String name, String mainImageUrl, int price) {
            this.id = id;
            this.name = name;
            this.mainImageUrl = mainImageUrl;
            this.price = price;
        }

        public static DisplayProductListData of(DisplayProduct displayProduct) {
            return DisplayProductListData.builder()
                    .id(displayProduct.getId())
                    .name(displayProduct.getName())
                    .mainImageUrl(displayProduct.getMainImageUrl())
                    .price(displayProduct.getPrice())
                    .build();
        }
    }

    @Getter
    public static class Pagination {

        private int currentPage;
        private long total;
        private int totalPages;

        public Pagination(int currentPage, long total, int totalPages) {
            this.currentPage = currentPage;
            this.total = total;
            this.totalPages = totalPages;
        }
    }
}
