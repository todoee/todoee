package io.todoee.web.auth;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.AuthProvider;

/**
 *
 * @author James.zhang
 */
public interface SkyAuth extends AuthProvider {

  /**
   * Create a Sky auth provider
   * @param vertx  the Vert.x instance
   * @param realm  the AuthRealm instance
   * @return  the auth provider
   */
  static SkyAuth create(Vertx vertx, AuthRealm realm) {
    return new SkyAuthProvider(vertx, realm);
  }
}
