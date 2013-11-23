package com.lckymn.kevin.gitlab.api.impl;

import static com.lckymn.kevin.gitlab.api.GitLabApiUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.elixirian.kommonlee.util.collect.Lists.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;

import org.assertj.core.api.Condition;
import org.elixirian.jsonstatham.core.JsonStatham;
import org.elixirian.jsonstatham.core.reflect.ReflectionJsonStathams;
import org.elixirian.kommonlee.util.collect.Sets;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.kevinsawicki.http.HttpRequest;
import com.lckymn.kevin.gitlab.api.GitLabUserService;
import com.lckymn.kevin.gitlab.json.GitLabUser;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

public class GitLabUserServiceImplTest
{
  private JsonStatham jsonStatham;

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
    this.jsonStatham = ReflectionJsonStathams.newReflectionJsonStathamInAction();
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public final void testGitLabUserServiceImpl() throws Exception
  {
    /* given */
    final HttpRequestForJsonSource expectedHttpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final JsonStatham expectedJsonStatham = mock(JsonStatham.class);
    final String url = "test-value";
    final String expectedUrl = buildApiUrl(url);

    /* when */
    final GitLabUserServiceImpl actual =
      new GitLabUserServiceImpl(expectedHttpRequestForJsonSource, expectedJsonStatham, url);

    /* then */
    assertThat(actual.getHttpRequestForJsonSource()).isEqualTo(expectedHttpRequestForJsonSource);
    assertThat(actual.getJsonStatham()).isEqualTo(expectedJsonStatham);
    assertThat(actual.getUrl()).isEqualTo(expectedUrl);
  }

  @Test
  public final void testGetAllGitLabUsers() throws Exception
  {
    /* given */
    final String url = "http://localhost/gitlab";
    final String privateToken = "testPrivateToken";

    @SuppressWarnings("boxing")
    final Integer id1 = 1;
    final String username1 = "kevinlee";
    final String email1 = "kevinlee@some.email.address";
    final String name1 = "Kevin Lee";
    final String bio1 = null;
    final String skype1 = "";
    final String linkedin1 = "";
    final String twitter1 = "";
    @SuppressWarnings("boxing")
    final Integer themeId1 = 1;
    @SuppressWarnings("boxing")
    final Integer colorSchemeId1 = 1;
    final String state1 = "active";
    final String createdAt1 = "2013-08-25T07:47:19Z";
    final String externUid1 = null;
    final String provider1 = null;

    @SuppressWarnings("boxing")
    final Integer id2 = 2;
    final String username2 = "anotheruser";
    final String email2 = "another.user@some.email";
    final String name2 = "Another Person";
    final String bio2 = null;
    final String skype2 = "";
    final String linkedin2 = "";
    final String twitter2 = "";
    @SuppressWarnings("boxing")
    final Integer themeId2 = 1;
    @SuppressWarnings("boxing")
    final Integer colorSchemeId2 = 1;
    final String state2 = "active";
    final String createdAt2 = "2013-09-01T15:10:55Z";
    final String externUid2 = null;
    final String provider2 = null;

    final List<GitLabUser> expected = newArrayList();
    expected.add(new GitLabUser(id1, username1, email1, name1, bio1, skype1, linkedin1, twitter1, themeId1,
        colorSchemeId1, state1, createdAt1, externUid1, provider1));
    expected.add(new GitLabUser(id2, username2, email2, name2, bio2, skype2, linkedin2, twitter2, themeId2,
        colorSchemeId2, state2, createdAt2, externUid2, provider2));

    final String json = jsonStatham.convertIntoJson(expected);

    final HttpRequestForJsonSource expectedHttpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.body()).thenReturn(json);

    when(expectedHttpRequestForJsonSource.get(prepareUrl(buildApiUrl(url), privateToken))).thenReturn(httpRequest);

    final GitLabUserService gitLabUserService =
      new GitLabUserServiceImpl(expectedHttpRequestForJsonSource, jsonStatham, url);

    /* when */
    final List<GitLabUser> actual = gitLabUserService.getAllGitLabUsers(privateToken);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetGitLabUsersByUsernames() throws Exception
  {
    /* given */
    final String url = "http://localhost/gitlab";
    final String privateToken = "testPrivateToken";

    @SuppressWarnings("boxing")
    final Integer id1 = 1;
    final String username1 = "kevinlee";
    final String email1 = "kevinlee@some.email.address";
    final String name1 = "Kevin Lee";
    final String bio1 = null;
    final String skype1 = "";
    final String linkedin1 = "";
    final String twitter1 = "";
    @SuppressWarnings("boxing")
    final Integer themeId1 = 1;
    @SuppressWarnings("boxing")
    final Integer colorSchemeId1 = 1;
    final String state1 = "active";
    final String createdAt1 = "2013-08-25T07:47:19Z";
    final String externUid1 = null;
    final String provider1 = null;

    @SuppressWarnings("boxing")
    final Integer id2 = 2;
    final String username2 = "anotheruser";
    final String email2 = "another.user@some.email";
    final String name2 = "Another Person";
    final String bio2 = null;
    final String skype2 = "";
    final String linkedin2 = "";
    final String twitter2 = "";
    @SuppressWarnings("boxing")
    final Integer themeId2 = 1;
    @SuppressWarnings("boxing")
    final Integer colorSchemeId2 = 1;
    final String state2 = "active";
    final String createdAt2 = "2013-09-01T15:10:55Z";
    final String externUid2 = null;
    final String provider2 = null;

    @SuppressWarnings("boxing")
    final Integer id3 = 2;
    final String username3 = "tester";
    final String email3 = "tester.user@some.email";
    final String name3 = "Some Tester";
    final String bio3 = null;
    final String skype3 = "";
    final String linkedin3 = "";
    final String twitter3 = "";
    @SuppressWarnings("boxing")
    final Integer themeId3 = 2;
    @SuppressWarnings("boxing")
    final Integer colorSchemeId3 = 2;
    final String state3 = "active";
    final String createdAt3 = "2013-09-02T20:00:17Z";
    final String externUid3 = null;
    final String provider3 = null;

    final List<GitLabUser> expected = newArrayList();
    expected.add(new GitLabUser(id1, username1, email1, name1, bio1, skype1, linkedin1, twitter1, themeId1,
        colorSchemeId1, state1, createdAt1, externUid1, provider1));
    expected.add(new GitLabUser(id3, username3, email3, name3, bio3, skype3, linkedin3, twitter3, themeId3,
        colorSchemeId3, state3, createdAt3, externUid3, provider3));

    final String json =
      jsonStatham.convertIntoJson(newArrayList(new GitLabUser(id1, username1, email1, name1, bio1, skype1, linkedin1,
          twitter1, themeId1, colorSchemeId1, state1, createdAt1, externUid1, provider1), new GitLabUser(id2,
          username2, email2, name2, bio2, skype2, linkedin2, twitter2, themeId2, colorSchemeId2, state2, createdAt2,
          externUid2, provider2), new GitLabUser(id3, username3, email3, name3, bio3, skype3, linkedin3, twitter3,
          themeId3, colorSchemeId3, state3, createdAt3, externUid3, provider3)));

    final HttpRequestForJsonSource expectedHttpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.body()).thenReturn(json);

    when(expectedHttpRequestForJsonSource.get(prepareUrl(buildApiUrl(url), privateToken))).thenReturn(httpRequest);

    final GitLabUserService gitLabUserService =
      new GitLabUserServiceImpl(expectedHttpRequestForJsonSource, jsonStatham, url);

    /* when */
    final List<GitLabUser> actual =
      gitLabUserService.getGitLabUsersByUsernames(privateToken, Sets.newHashSet(username1, username3));

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetGitLabUsersByUsernamesStringArray() throws Exception
  {
    /* given */
    final String url = "http://localhost/gitlab";
    final String privateToken = "testPrivateToken";

    @SuppressWarnings("boxing")
    final Integer id1 = 1;
    final String username1 = "kevinlee";
    final String email1 = "kevinlee@some.email.address";
    final String name1 = "Kevin Lee";
    final String bio1 = null;
    final String skype1 = "";
    final String linkedin1 = "";
    final String twitter1 = "";
    @SuppressWarnings("boxing")
    final Integer themeId1 = 1;
    @SuppressWarnings("boxing")
    final Integer colorSchemeId1 = 1;
    final String state1 = "active";
    final String createdAt1 = "2013-08-25T07:47:19Z";
    final String externUid1 = null;
    final String provider1 = null;

    @SuppressWarnings("boxing")
    final Integer id2 = 2;
    final String username2 = "anotheruser";
    final String email2 = "another.user@some.email";
    final String name2 = "Another Person";
    final String bio2 = null;
    final String skype2 = "";
    final String linkedin2 = "";
    final String twitter2 = "";
    @SuppressWarnings("boxing")
    final Integer themeId2 = 1;
    @SuppressWarnings("boxing")
    final Integer colorSchemeId2 = 1;
    final String state2 = "active";
    final String createdAt2 = "2013-09-01T15:10:55Z";
    final String externUid2 = null;
    final String provider2 = null;

    @SuppressWarnings("boxing")
    final Integer id3 = 2;
    final String username3 = "tester";
    final String email3 = "tester.user@some.email";
    final String name3 = "Some Tester";
    final String bio3 = null;
    final String skype3 = "";
    final String linkedin3 = "";
    final String twitter3 = "";
    @SuppressWarnings("boxing")
    final Integer themeId3 = 2;
    @SuppressWarnings("boxing")
    final Integer colorSchemeId3 = 2;
    final String state3 = "active";
    final String createdAt3 = "2013-09-02T20:00:17Z";
    final String externUid3 = null;
    final String provider3 = null;

    final List<GitLabUser> expected = newArrayList();
    expected.add(new GitLabUser(id1, username1, email1, name1, bio1, skype1, linkedin1, twitter1, themeId1,
        colorSchemeId1, state1, createdAt1, externUid1, provider1));
    expected.add(new GitLabUser(id3, username3, email3, name3, bio3, skype3, linkedin3, twitter3, themeId3,
        colorSchemeId3, state3, createdAt3, externUid3, provider3));

    final String json =
      jsonStatham.convertIntoJson(newArrayList(new GitLabUser(id1, username1, email1, name1, bio1, skype1, linkedin1,
          twitter1, themeId1, colorSchemeId1, state1, createdAt1, externUid1, provider1), new GitLabUser(id2,
          username2, email2, name2, bio2, skype2, linkedin2, twitter2, themeId2, colorSchemeId2, state2, createdAt2,
          externUid2, provider2), new GitLabUser(id3, username3, email3, name3, bio3, skype3, linkedin3, twitter3,
          themeId3, colorSchemeId3, state3, createdAt3, externUid3, provider3)));

    final HttpRequestForJsonSource expectedHttpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.body()).thenReturn(json);

    when(expectedHttpRequestForJsonSource.get(prepareUrl(buildApiUrl(url), privateToken))).thenReturn(httpRequest);

    final GitLabUserService gitLabUserService =
      new GitLabUserServiceImpl(expectedHttpRequestForJsonSource, jsonStatham, url);

    /* when */
    final List<GitLabUser> actual = gitLabUserService.getGitLabUsersByUsernames(privateToken, username1, username3);

    /* then */
    assertThat(actual).hasSameSizeAs(expected)
        .containsAll(expected)
        .isEqualTo(expected);

    final Iterator<GitLabUser> actualIterator = actual.iterator();
    final Iterator<GitLabUser> expectedIterator = expected.iterator();
    while (actualIterator.hasNext())
    {
      final GitLabUser actualEach = actualIterator.next();
      final GitLabUser expectedEach = expectedIterator.next();
      assertThat(actualEach).is(new Condition<GitLabUser>() {
        @Override
        public boolean matches(final GitLabUser value)
        {
          return value.hasSameDataAs(expectedEach);
        }
      });
    }
  }

}
