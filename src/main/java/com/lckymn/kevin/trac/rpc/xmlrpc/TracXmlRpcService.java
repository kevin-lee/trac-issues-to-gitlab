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
import java.util.TimeZone;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac.json.TracIssueComment;
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

  public TracXmlRpcService(final XmlRpcClient xmlRpcClient)
  {
    this.xmlRpcClient = xmlRpcClient;
  }

  XmlRpcClient getXmlRpcClient()
  {
    return xmlRpcClient;
  }

  @Override
  public TracIssue get(final Integer id)
  {
    final Object[] results;
    final Object[] changeLogs;
    try
    {
      results = (Object[]) xmlRpcClient.execute("ticket.get", Arrays.asList(id));
      changeLogs = (Object[]) xmlRpcClient.execute("ticket.changeLog", Arrays.asList(id, 0));
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

      return TracIssue.newInstance(idFromServer, map, changeLogs);
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

  public static TracXmlRpcService newInstance(final TracRpcConfig tracRpcConfig)
  {
    final XmlRpcClientConfigImpl xmlRpcClientConfig = new XmlRpcClientConfigImpl();
    xmlRpcClientConfig.setServerURL(tracRpcConfig.getServerUrl());
    xmlRpcClientConfig.setBasicUserName(tracRpcConfig.getBasicUserName());
    xmlRpcClientConfig.setBasicPassword(tracRpcConfig.getBasicPassword());
    xmlRpcClientConfig.setTimeZone(TimeZone.getTimeZone("UTC"));
    final XmlRpcClient xmlRpcClient = new XmlRpcClient();
    xmlRpcClient.setConfig(xmlRpcClientConfig);
    return new TracXmlRpcService(xmlRpcClient);
  }
}
