package com.lckymn.kevin.trac2gitlab;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Trac2GitLabUtilTest
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
  public final void testConvertCodeBlockForGitLabMarkDown() throws Exception
  {
    /* given */
    final String tracWikiText = "{{{String}}}";
    final String expected = "```String```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown2() throws Exception
  {
    /* given */
    final String tracWikiText = "{{{GitLab}}}";
    final String expected = "```GitLab```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown3() throws Exception
  {
    /* given */
    final String tracWikiText = "{{{\n#!application/javascript\n{\n  \"name\":\"Kevin\"\n}\n}}}";
    final String expected = "```javascript\n{\n  \"name\":\"Kevin\"\n}\n```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown4() throws Exception
  {
    /* given */
    final String tracWikiText = "{{{#!text/javascript\n{\n  \"name\":\"Kevin\"\n}\n}}}";
    final String expected = "```javascript\n{\n  \"name\":\"Kevin\"\n}\n```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown5() throws Exception
  {
    /* given */
    final String tracWikiText =
      "{{{#!text/javascript\n{\n  \"name\":\"Kevin\"\n}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}";
    final String expected =
      "```javascript\n{\n  \"name\":\"Kevin\"\n}\n```\nblah blah blah blah\n```javascript\n{\n  \"id\": 1\n}\n```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown6() throws Exception
  {
    /* given */
    final String tracWikiText =
      "{{{#!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}";
    final String expected =
        "```javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n```\nblah blah blah blah\n```javascript\n{\n  \"id\": 1\n}\n```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown7() throws Exception
  {
    /* given */
    final String tracWikiText =
        "{{{#!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}";
    final String expected =
        "```javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n```\nblah blah blah blah\n```javascript\n{\n  \"id\": 1\n}\n```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }
}
