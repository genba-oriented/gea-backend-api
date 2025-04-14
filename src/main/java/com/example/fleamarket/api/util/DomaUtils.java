package com.example.fleamarket.api.util;

import lombok.extern.slf4j.Slf4j;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.springframework.data.domain.Sort;

import java.util.function.Consumer;

@Slf4j
public class DomaUtils {

	public static Consumer<OrderByNameDeclaration> toOrderBy(Sort sort, EntityMetamodel<?> entityMetamodel) {
		if (sort.isUnsorted()) {
			return cond -> {};
		}
		return cond -> {
			for (Sort.Order order : sort) {
				PropertyMetamodel<?> propertyMetamodel = toPropertyMetamodel(order.getProperty(), entityMetamodel);
				if (propertyMetamodel != null) {
					if (order.isAscending()) {
						cond.asc(propertyMetamodel);
					} else {
						cond.desc(propertyMetamodel);
					}
				} else {
					log.warn("ソート条件で指定したプロパティが見つかりません。property=" + order.getProperty());
				}
			}
		};
	}

	static PropertyMetamodel<?> toPropertyMetamodel(String property, EntityMetamodel<?> entityMetamodel) {
		for (PropertyMetamodel<?> propertyMetamodel : entityMetamodel.allPropertyMetamodels()) {
			if (propertyMetamodel.getName().equals(property)) {
				return propertyMetamodel;
			}
		}
		return null;
	}


}
