/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.platform7.extension.graphql.auth;

public class AuthenticationResult {

  protected String authenticatedUser;
  protected boolean isAuthenticated;

  public AuthenticationResult(String authenticatedUser, boolean isAuthenticated) {
    this.authenticatedUser = authenticatedUser;
    this.isAuthenticated = isAuthenticated;
  }

  public String getAuthenticatedUser() {
    return authenticatedUser;
  }
  public void setAuthenticatedUser(String authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }
  public boolean isAuthenticated() {
    return isAuthenticated;
  }
  public void setAuthenticated(boolean isAuthenticated) {
    this.isAuthenticated = isAuthenticated;
  }

  public static AuthenticationResult successful(String userId) {
    return new AuthenticationResult(userId, true);
  }

  public static AuthenticationResult unsuccessful() {
    return new AuthenticationResult(null, false);
  }

  public static AuthenticationResult unsuccessful(String userId) {
    return new AuthenticationResult(userId, false);
  }
}
