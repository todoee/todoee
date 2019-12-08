package io.todoee.httpclient.test.rdi;

import io.todoee.httpclient.annotation.GET;
import io.todoee.httpclient.annotation.Host;
import io.todoee.httpclient.annotation.Path;
import io.todoee.httpclient.annotation.PathParam;
import io.todoee.httpclient.annotation.Port;
import io.todoee.httpclient.annotation.QueryParam;
import io.todoee.httpclient.test.rdi.dto.User;
import io.vertx.core.Future;

@Host("0.0.0.0")
@Port(8080)
public interface TestRdi {
	
	@GET
	@Path("/getuser")
	Future<User> get(@QueryParam("id") String id);
	
	@GET
	@Path("/getuser2/:id")
	Future<Void> get2(@PathParam("id") String id);

}
