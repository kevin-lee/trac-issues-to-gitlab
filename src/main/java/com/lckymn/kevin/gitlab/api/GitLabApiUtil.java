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

import static com.lckymn.kevin.gitlab.api.GitLabApiConstants.*;

import org.elixirian.jsonstatham.core.JsonStatham;
import org.elixirian.jsonstatham.core.convertible.JsonConvertible;
import org.elixirian.jsonstatham.core.convertible.JsonObject;
import org.elixirian.kommonlee.util.CommonConstants;

import com.lckymn.kevin.gitlab.api.exception.GitLabMessageException;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-31)
 */
public final class GitLabApiUtil
{
  private GitLabApiUtil() throws IllegalAccessException
  {
    throw new IllegalAccessException(getClass().getName() + CommonConstants.CANNOT_BE_INSTANTIATED);
  }

  public static String prepareUrl(final String url, final String privateToken)
  {
    return new StringBuilder(url).append("?")
        .append(PRIVATE_TOKEN)
        .append("=")
        .append(privateToken)
        .toString();
  }

  public static String prepareUrl(final String url, final String privateToken, final Object... parameters)
  {
    final StringBuilder stringBuilder = new StringBuilder(url);
    for (final Object parameter : parameters)
    {
      stringBuilder.append("/")
          .append(parameter);
    }
    return stringBuilder.append("?")
        .append(PRIVATE_TOKEN)
        .append("=")
        .append(privateToken)
        .toString();
  }

  public static String prepareUrlForMilestones(final String url, final String privateToken, final Long projectId)
  {
    return prepareUrl(url, privateToken, projectId, MILESTONES);
  }

  public static String buildApiUrl(final String url)
  {
    return url + (url.endsWith("/") ? API_V3 : _API_V3);
  }

  public static String buildApiUrlForProjects(final String url)
  {
    return url + (url.endsWith("/") ? API_V3 : _API_V3) + _PROJECTS;
  }

  public static <T> T getResultOrThrowException(final JsonStatham jsonStatham, final Class<T> theClass,
      final String json)
  {
    final JsonConvertible jsonConvertible = jsonStatham.convertJsonStringIntoJsonConvertible(json);
    if (jsonConvertible.isJsonObject())
    {
      final JsonObject jsonObject = (JsonObject) jsonConvertible;
      if (!jsonObject.isNull() && jsonObject.containsName("message"))
      {
        final String message = jsonObject.get("message");
        throw GitLabMessageException.newInstanceByMessage(message);
      }
    }
    return jsonStatham.convertFromJsonConvertible(theClass, jsonConvertible);
  }
}
