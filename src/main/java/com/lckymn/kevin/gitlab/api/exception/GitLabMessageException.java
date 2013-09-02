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
public class GitLabMessageException extends GitLabException
{
  private static final long serialVersionUID = 6094328692344008154L;

  public GitLabMessageException(final String message, final Throwable cause, final boolean enableSuppression,
      final boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public GitLabMessageException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  public GitLabMessageException(final String message)
  {
    super(message);
  }

  public GitLabMessageException(final Throwable cause)
  {
    super(cause);
  }

  public static GitLabMessageException newInstanceByMessage(final String message)
  {
    final String[] values = message.split("[\\s]+");
    if (0 != values.length)
    {
      final int code;
      try
      {
        code = Integer.parseInt(values[0]);
      }
      catch (final NumberFormatException e)
      {
        return new GitLabMessageException(message);
      }
      final GitLabMessageException gitLabMessageException;
      switch (code)
      {
        case 400:
          gitLabMessageException = new GitLab400BadRequestMessageException(message);
          break;
        case 401:
          gitLabMessageException = new GitLab401UnauthorizedMessageException(message);
          break;
        case 403:
          gitLabMessageException = new GitLab403ForbiddenMessageException(message);
          break;
        case 404:
          gitLabMessageException = new GitLab404NotFoundMessageException(message);
          break;
        case 405:
          gitLabMessageException = new GitLab405MethodNotAllowedMessageException(message);
          break;
        case 409:
          gitLabMessageException = new GitLab409ConflictMessageException(message);
          break;
        case 500:
          gitLabMessageException = new GitLab500ServerErrorMessageException(message);
          break;
        default:
          gitLabMessageException = new GitLabMessageException(message);
      }
      return gitLabMessageException;
    }
    return new GitLabMessageException(message);
  }
}
