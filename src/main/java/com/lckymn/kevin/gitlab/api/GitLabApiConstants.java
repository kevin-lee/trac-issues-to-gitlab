/**
 * Copyright 2013 Lee, Seong Hyun (Kevin)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lckymn.kevin.gitlab.api;

import org.elixirian.kommonlee.util.CommonConstants;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-01)
 */
public class GitLabApiConstants
{
  public static final String SLASH = "/";

  /**
   * "api/v3"
   */
  public static final String API_V3 = "api/v3";

  /**
   * "/api/v3"
   */
  public static final String _API_V3 = SLASH + API_V3;

  /**
   * "/projects"
   */
  public static final String _PROJECTS = "/projects";

  /**
   * "milestones"
   */
  public static final String MILESTONES = "milestones";

  /**
   * "/milestones"
   */
  public static final String _MILESTONES = SLASH + MILESTONES;

  /**
   * "private_token"
   */
  public static final String PRIVATE_TOKEN = "private_token";

  private GitLabApiConstants() throws IllegalAccessException
  {
    throw new IllegalAccessException(getClass().getName() + CommonConstants.CANNOT_BE_INSTANTIATED);
  }
}
