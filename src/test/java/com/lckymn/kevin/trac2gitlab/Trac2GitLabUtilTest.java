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
  public final void testConvertCodeBlockForGitLabMarkDownWithNoTracWikiCodeBlock()
  {
    /* given */
    final String tracWikiText =
      "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    final String expected = new String(tracWikiText);

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDownWithOneLineCodeBlock()
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
  public final void testConvertCodeBlockForGitLabMarkDownWithOneLineCodeBlock2()
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
  public final void testConvertCodeBlockForGitLabMarkDownWithOneLineCodeBlock3()
  {
    /* given */
    final String tracWikiText = "{{{  String  }}}";
    final String expected = "```  String  ```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDownWithOneLineCodeBlock4()
  {
    /* given */
    final String tracWikiText = "{{{ \tString \t}}}";
    final String expected = "``` \tString \t```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown()
  {
    /* given */
    final String tracWikiText = "{{{\t  \n  #!application/javascript\n{\n  \"name\":\"Kevin\"\n}  \n  \t}}}";
    final String expected = "```javascript\n{\n  \"name\":\"Kevin\"\n}  \n  \t```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown2()
  {
    /* given */
    final String tracWikiText = "{{{  \n#!application/javascript\n{\n  \"name\":\"Kevin\"\n}\n  }}}";
    final String expected = "```javascript\n{\n  \"name\":\"Kevin\"\n}\n  ```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown3()
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
  public final void testConvertCodeBlockForGitLabMarkDown4()
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
  public final void testConvertCodeBlockForGitLabMarkDown5()
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
  public final void testConvertCodeBlockForGitLabMarkDown6()
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
  public final void testConvertCodeBlockForGitLabMarkDown7()
  {
    /* given */
    final String tracWikiText =
      "{{{  #!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}";
    final String expected =
      "```javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n```\nblah blah blah blah\n```javascript\n{\n  \"id\": 1\n}\n```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public final void testConvertCodeBlockForGitLabMarkDown8()
  {
    /* given */
    final String tracWikiText =
      "{{{  \n  #!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}";
    final String expected =
      "```javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n```\nblah blah blah blah\n```javascript\n{\n  \"id\": 1\n}\n```";

    /* when */
    final String actual = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracWikiText);

    /* then */
    assertThat(actual, is(equalTo(expected)));
  }
}
