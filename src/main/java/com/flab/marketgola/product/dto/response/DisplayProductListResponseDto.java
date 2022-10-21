package com.flab.marketgola.product.dto.response;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.SortType;
import com.flab.marketgola.product.dto.request.GetDisplayProductsCondition;
import com.flab.marketgola.product.mapper.dto.DisplayProductListDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static DisplayProductListResponseDto of(DisplayProductListDto displayProductListDto,
            GetDisplayProductsCondition condition) {

        List<DisplayProductListData> data = displayProductListDto.getDisplayProducts().stream()
                .map(DisplayProductListData::of)
                .collect(Collectors.toList());

        Pagination meta = Pagination.of(displayProductListDto.getTotal(), condition);

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

        private SortType sortType;
        private int currentPage;
        private long total;
        private int totalPages;

        @Builder
        public Pagination(SortType sortType, long total, int currentPage, int totalPages) {
            this.sortType = sortType;
            this.total = total;
            this.currentPage = currentPage;
            this.totalPages = totalPages;
        }

        public static Pagination of(long total, GetDisplayProductsCondition condition) {
            return Pagination.builder()
                    .sortType(condition.getSortType())
                    .total(total)
                    .currentPage(condition.getPage())
                    .totalPages(calculateTotalPages(total, condition))
                    .build();
        }

        private static int calculateTotalPages(long total, GetDisplayProductsCondition condition) {
            return (int) Math.ceil((double) total / condition.getPerPage());
        }
    }
}
