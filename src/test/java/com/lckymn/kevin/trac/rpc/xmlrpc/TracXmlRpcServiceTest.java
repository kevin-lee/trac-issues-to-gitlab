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

import static org.elixirian.kommonlee.util.collect.Maps.*;
import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfig;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac.rpc.TracRpcConfig;
import com.lckymn.kevin.util.DateAndTimeFormatUtil;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-01)
 */
public class TracXmlRpcServiceTest
{

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public final void testTracXmlRpcService() throws Exception
  {
    /* given */
    final XmlRpcClient expected = mock(XmlRpcClient.class);

    /* when */
    final TracXmlRpcService tracXmlRpcService = new TracXmlRpcService(expected);
    final XmlRpcClient actual = tracXmlRpcService.getXmlRpcClient();

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGet() throws Exception
  {
    /* given */
    final XmlRpcClient xmlRpcClient = mock(XmlRpcClient.class);
    final Integer id = 1;

    final Map<String, Object> map = newHashMap();
    final String summary = "Some test ticket";
    map.put("summary", summary);
    final String keywords = "test, ticket, blah blah";
    map.put("keywords", keywords);
    final String status = "new";
    map.put("status", status);
    final String resolution = "";
    map.put("resolution", resolution);
    final String type = "Task";
    map.put("type", type);
    final String version = "0.0.1";
    map.put("version", version);
    final String milestone = "M1";
    map.put("milestone", milestone);
    final String reporter = "kevinlee";
    map.put("reporter", reporter);

    final Date time = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-01T15:00:00Z");
    map.put("time", time);
    final String component = "test-component";
    map.put("component", component);
    final String description = "This is test description";
    map.put("description", description);
    final String priority = "highest";
    map.put("priority", priority);
    final String severity = "critical";
    map.put("severity", severity);
    final String owner = "kevinlee";
    map.put("owner", owner);

    final Date changetime = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-05T10:30:22Z");
    map.put("changetime", changetime);
    final String cc = "user1, user2";
    map.put("cc", cc);

    when(xmlRpcClient.execute("ticket.get", Arrays.asList(id))).thenReturn(new Object[] { id, null, null, map });

    final TracIssue expected = TracIssue.newInstance(id, map);
    final TracXmlRpcService tracXmlRpcService = new TracXmlRpcService(xmlRpcClient);

    /* when */
    final TracIssue actual = tracXmlRpcService.get(id);

    /* then */
    assertThat(actual).isEqualTo(expected);
    verify(xmlRpcClient, times(1)).execute("ticket.get", Arrays.asList(id));
  }

  @Test
  public final void testGetAll() throws Exception
  {
    /* given */
    final XmlRpcClient xmlRpcClient = mock(XmlRpcClient.class);
    final Integer id = 1;

    final Map<String, Object> map = newHashMap();
    final String summary = "Some test ticket";
    map.put("summary", summary);
    final String keywords = "test, ticket, blah blah";
    map.put("keywords", keywords);
    final String status = "new";
    map.put("status", status);
    final String resolution = "";
    map.put("resolution", resolution);
    final String type = "Task";
    map.put("type", type);
    final String version = "0.0.1";
    map.put("version", version);
    final String milestone = "M1";
    map.put("milestone", milestone);
    final String reporter = "kevinlee";
    map.put("reporter", reporter);

    final Date time = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-01T15:00:00Z");
    map.put("time", time);
    final String component = "test-component";
    map.put("component", component);
    final String description = "This is test description";
    map.put("description", description);
    final String priority = "highest";
    map.put("priority", priority);
    final String severity = "critical";
    map.put("severity", severity);
    final String owner = "kevinlee";
    map.put("owner", owner);

    final Date changetime = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-05T10:30:22Z");
    map.put("changetime", changetime);
    final String cc = "user1, user2";
    map.put("cc", cc);

    when(xmlRpcClient.execute("ticket.get", Arrays.asList(id))).thenReturn(new Object[] { id, null, null, map });
    when(xmlRpcClient.execute("ticket.query", Arrays.asList("max=0"))).thenReturn(new Object[] { id });

    final List<TracIssue> expected = Arrays.asList(TracIssue.newInstance(id, map));
    final TracXmlRpcService tracXmlRpcService = new TracXmlRpcService(xmlRpcClient);

    /* when */
    final List<TracIssue> actual = tracXmlRpcService.getAll();

    /* then */
    assertThat(actual).isEqualTo(expected);
    verify(xmlRpcClient, times(1)).execute("ticket.query", Arrays.asList("max=0"));
  }

  @Test
  public final void testNewInstance() throws Exception
  {
    /* given */
    final TracRpcConfig tracRpcConfig = mock(TracRpcConfig.class);
    when(tracRpcConfig.getServerUrl()).thenReturn(new URL("http://test.domain"));
    when(tracRpcConfig.getBasicUserName()).thenReturn("testUser");
    when(tracRpcConfig.getBasicPassword()).thenReturn("testPassword");

    final XmlRpcClientConfigImpl expected = new XmlRpcClientConfigImpl();
    expected.setServerURL(tracRpcConfig.getServerUrl());
    expected.setBasicUserName(tracRpcConfig.getBasicUserName());
    expected.setBasicPassword(tracRpcConfig.getBasicPassword());
    expected.setTimeZone(TimeZone.getTimeZone("UTC"));

    final TracXmlRpcService tracXmlRpcService = TracXmlRpcService.newInstance(tracRpcConfig);

    /* when */
    final XmlRpcClient xmlRpcClient = tracXmlRpcService.getXmlRpcClient();
    final XmlRpcClientConfig xmlRpcClientConfig = xmlRpcClient.getClientConfig();
    final XmlRpcClientConfigImpl actual = (XmlRpcClientConfigImpl) xmlRpcClientConfig;

    /* then */
    assertThat(actual.getServerURL()).isEqualTo(expected.getServerURL());
    assertThat(actual.getBasicUserName()).isEqualTo(expected.getBasicUserName());
    assertThat(actual.getBasicPassword()).isEqualTo(expected.getBasicPassword());
    assertThat(actual.getTimeZone()).isEqualTo(expected.getTimeZone());
  }

}
