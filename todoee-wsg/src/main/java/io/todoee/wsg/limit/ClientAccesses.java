package io.todoee.wsg.limit;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author James.zhang
 *
 */
public class ClientAccesses {

	private List<Long> history;

	public ClientAccesses() {
		history = new ArrayList<>();
	}

	public void newAccess() {
		history.add(System.currentTimeMillis());
	}

	public boolean isOverLimit(RateLimit limit) {
		long minBound = System.currentTimeMillis()
				- limit.getTimeUnit().toMillis(limit.getValue());
		long count = history.stream().filter(access -> access > minBound)
				.count();
		return count > limit.getCount();
	}

	public void clearHistory(Long keepAfter) {
		history = history.stream().filter(access -> access > keepAfter)
				.collect(Collectors.toList());
	}

	public boolean noAccess() {
		return history.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public static ClientAccesses fromJsonObject(JsonObject json) {
		ClientAccesses accesses = new ClientAccesses();
		JsonArray array = json.getJsonArray("history");
		if (array != null) {
			accesses.history = array.getList();
		}
		return accesses;
	}

	public JsonObject toJsonObject() {
		return new JsonObject().put("history", new JsonArray(history));
	}
}
