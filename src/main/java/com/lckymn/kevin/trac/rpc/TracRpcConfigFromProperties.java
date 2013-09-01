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
package com.lckymn.kevin.trac.rpc;

import static org.elixirian.kommonlee.util.Objects.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.elixirian.kommonlee.io.exception.RuntimeMalformedURLException;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-29)
 */
public class TracRpcConfigFromProperties implements TracRpcConfig
{
  private final URL serverUrl;

  private final String basicUserName;

  private final char[] basicPassword;

  public TracRpcConfigFromProperties(final URL serverUrl, final String basicUserName, final char[] basicPassword)
  {
    this.serverUrl = serverUrl;
    this.basicUserName = basicUserName;
    this.basicPassword = basicPassword;
  }

  @Override
  public URL getServerUrl()
  {
    return serverUrl;
  }

  @Override
  public String getBasicUserName()
  {
    return basicUserName;
  }

  @Override
  public String getBasicPassword()
  {
    return new String(basicPassword);
  }

  public static TracRpcConfigFromProperties newInstance(final Properties properties)
      throws RuntimeMalformedURLException
  {
    final String serverUrl = properties.getProperty("server.url");
    final String basicUserName = properties.getProperty("basic.username");
    final String basicPassword = properties.getProperty("basic.password");
    try
    {
      return new TracRpcConfigFromProperties(new URL(serverUrl), basicUserName, basicPassword.toCharArray());
    }
    catch (final MalformedURLException e)
    {
      throw new RuntimeMalformedURLException(e);
    }
  }

  @Override
  public String toString()
  {
    /* @formatter:off */
    return toStringBuilder(this)
            .add("serverUrl", serverUrl)
            .add("basicUserName", basicUserName)
            .add("basicPassword", "## PROTECTED ##")
          .toString();
    /* @formatter:on */
  }
}
