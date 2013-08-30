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
package com.lckymn.kevin.trac.rpc.xmlrpc;

import static org.elixirian.kommonlee.util.collect.Lists.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac.rpc.TracRpcConfig;
import com.lckymn.kevin.trac.rpc.TracRpcService;
import com.lckymn.kevin.xmlrpc.exception.RuntimeXmlRpcException;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-29)
 */
public class TracXmlRpcService implements TracRpcService
{
  private final XmlRpcClient xmlRpcClient;

  public TracXmlRpcService(final TracRpcConfig tracRpcConfig)
  {
    final XmlRpcClientConfigImpl xmlRpcClientConfig = new XmlRpcClientConfigImpl();
    xmlRpcClientConfig.setServerURL(tracRpcConfig.getServerUrl());
    xmlRpcClientConfig.setBasicUserName(tracRpcConfig.getBasicUserName());
    xmlRpcClientConfig.setBasicPassword(tracRpcConfig.getBasicPassword());
    final XmlRpcClient xmlRpcClient = new XmlRpcClient();
    xmlRpcClient.setConfig(xmlRpcClientConfig);
    this.xmlRpcClient = xmlRpcClient;
  }

  @Override
  public TracIssue get(final Integer id)
  {
    final Object[] results;
    try
    {
      results = (Object[]) xmlRpcClient.execute("ticket.get", Arrays.asList(id));
    }
    catch (final XmlRpcException e)
    {
      throw new RuntimeXmlRpcException(e.code, e.getMessage(), e);
    }

    final Integer idFromServer = (Integer) results[0];
    final Object each = results[3];
    if (each instanceof Map)
    {
      @SuppressWarnings("unchecked")
      final Map<String, Object> map = ((Map<String, Object>) each);
      return TracIssue.newInstance(idFromServer, map);
    }
    throw new RuntimeXmlRpcException(
        "Invalid result. The element at the index 3 is not a Map containing the issue details. [type: "
            + each.getClass() + "][object: " + each + "]");
  }

  @Override
  public List<TracIssue> getAll() throws RuntimeXmlRpcException
  {
    final List<TracIssue> resultList = newArrayList();
    try
    {
      for (final Object id : (Object[]) xmlRpcClient.execute("ticket.query", Arrays.asList("max=0")))
      {
        resultList.add(get((Integer) id));
      }
    }
    catch (final XmlRpcException e)
    {
      throw new RuntimeXmlRpcException(e);
    }
    return resultList;
  }
}
