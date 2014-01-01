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
package com.lckymn.kevin.gitlab.api.exception;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-01)
 */
public class GitLab404NotFoundMessageException extends GitLabMessageException
{
  private static final long serialVersionUID = 6094328692344008154L;

  public GitLab404NotFoundMessageException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  public GitLab404NotFoundMessageException(final String message)
  {
    super(message);
  }

  public GitLab404NotFoundMessageException(final Throwable cause)
  {
    super(cause);
  }
}
