/**
 *
 */
package com.lckymn.kevin.gitlab.json;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonField;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-01)
 */
@Json
public class GitLabMessage
{
  @JsonField
  public final String message;

  public GitLabMessage(final String message)
  {
    this.message = message;
  }
}
