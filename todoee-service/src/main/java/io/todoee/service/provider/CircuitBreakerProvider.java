package io.todoee.service.provider;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.Vertx;

import javax.inject.Inject;

import com.google.inject.Provider;

/**
 *
 * @author James.zhang
 */
public class CircuitBreakerProvider implements Provider<CircuitBreaker> {

	private Vertx vertx;
	
	@Inject
    public CircuitBreakerProvider(Vertx vertx) {
		this.vertx = vertx;
    }

    @Override
    public CircuitBreaker get() {
        return CircuitBreaker.create("meedo-circuit-breaker",
				vertx, new CircuitBreakerOptions().setMaxFailures(5) // 最大故障次数
				.setTimeout(60000) // 超时时间
				.setFallbackOnFailure(true) // 设置是否失败回调
				.setResetTimeout(20000) // 重置状态超时
		);
    }
}
