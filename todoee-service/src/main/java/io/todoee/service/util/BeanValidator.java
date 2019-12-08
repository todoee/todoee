package io.todoee.service.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import io.todoee.base.utils.ObjUtil;
import io.vertx.serviceproxy.ServiceException;

/**
 * 对象验证器
 * 
 */
public class BeanValidator {

	/**
	 * 验证某个bean的参数
	 * 
	 * @param object
	 *            被校验的参数
	 * @throws WsgException
	 *             如果参数校验不成功则抛出此异常
	 */
	public static <T> void validate(T object) {
		// 获得验证器
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		// 执行验证
		Set<ConstraintViolation<T>> constraintViolations = validator
				.validate(object);

		if (ObjUtil.isEmpty(constraintViolations)
				|| constraintViolations.size() == 0) {
			return;
		}

		// 如果有验证信息，则将第一个取出来包装成异常返回
		ConstraintViolation<T> constraintViolation = constraintViolations
				.iterator().next();
		throw new ServiceException(10000,
				constraintViolation.getPropertyPath() + ": "
						+ constraintViolation.getMessage());
	}

}
