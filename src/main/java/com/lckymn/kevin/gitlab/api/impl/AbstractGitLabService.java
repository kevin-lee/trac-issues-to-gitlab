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
package com.lckymn.kevin.gitlab.api.impl;

import static com.lckymn.kevin.gitlab.api.GitLabApiUtil.*;

import org.elixirian.jsonstatham.core.JsonStatham;

import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-06)
 */
public class AbstractGitLabService
{
  protected final HttpRequestForJsonSource httpRequestForJsonSource;
  protected final JsonStatham jsonStatham;
  protected final String url;

  public AbstractGitLabService(final HttpRequestForJsonSource httpRequestForJsonSource, final JsonStatham jsonStatham,
      final String url)
  {
    this.httpRequestForJsonSource = httpRequestForJsonSource;
    this.jsonStatham = jsonStatham;
    this.url = buildApiUrl(url);
  }

  HttpRequestForJsonSource getHttpRequestForJsonSource()
  {
    return httpRequestForJsonSource;
  }

  JsonStatham getJsonStatham()
  {
    return jsonStatham;
  }

  String getUrl()
  {
    return url;
  }

}
