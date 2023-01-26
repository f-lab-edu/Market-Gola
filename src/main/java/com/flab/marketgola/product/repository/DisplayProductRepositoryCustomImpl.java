package com.flab.marketgola.product.repository;

import static com.flab.marketgola.product.domain.QDisplayProduct.displayProduct;
import static com.flab.marketgola.product.domain.QProduct.product;
import static com.flab.marketgola.product.domain.QProductCategory.productCategory;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class DisplayProductRepositoryCustomImpl implements DisplayProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private JPAQueryFactory queryFactory;

    public DisplayProductRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<DisplayProduct> findByIdWithAll(Long id) {
        DisplayProduct result = queryFactory
                .selectFrom(displayProduct)
                .join(displayProduct.products, product).fetchJoin()
                .join(displayProduct.category, productCategory).fetchJoin()
                .where(displayProduct.id.eq(id), product.isDeleted.eq(false))
                .fetchOne();

        return Optional.of(result);
    }

    @Override
    public Page<DisplayProduct> findByCategory(int categoryId, Pageable pageable) {
        List<DisplayProduct> result = queryFactory
                .selectFrom(displayProduct)
                .join(displayProduct.category, productCategory)
                .join(displayProduct.products, product)
                .where(productCategory.id.eq(categoryId), product.isDeleted.eq(false))
                .orderBy(order(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(displayProduct.count())
                .from(displayProduct)
                .join(displayProduct.category, productCategory)
                .join(displayProduct.products, product)
                .where(productCategory.id.eq(categoryId), product.isDeleted.eq(false))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    private OrderSpecifier<? extends Comparable>[] order(Sort sort) {
        List<OrderSpecifier<?>> list = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            Path<? extends Comparable> path = getPathByProperty(order.getProperty());
            list.add(new OrderSpecifier<>(direction, path));
        });

        return list.toArray(OrderSpecifier[]::new);
    }

    private Path<? extends Comparable> getPathByProperty(String property) {
        if (property.equals("price")) {
            return displayProduct.price;
        }

        return displayProduct.createdAt;
    }
}