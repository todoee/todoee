package io.todoee.core.boot;

import com.beust.jcommander.Parameter;

import lombok.Data;

/**
 * 
 * @author James.zhang
 *
 */
@Data
public class CommandParam {
	@Parameter(names = { "-sid", "-serviceId" })
	private String serviceId;
}
