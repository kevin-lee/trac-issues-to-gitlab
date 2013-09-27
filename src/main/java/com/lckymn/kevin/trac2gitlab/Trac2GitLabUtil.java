/**
 *
 */
package com.lckymn.kevin.trac2gitlab;

import static org.elixirian.kommonlee.util.collect.Maps.*;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.elixirian.kommonlee.util.CommonConstants;

/**
 * <pre>
 *     ___  _____                                _____
 *    /   \/    /_________  ___ ____ __ ______  /    /   ______  ______
 *   /        / /  ___ \  \/  //___// //     / /    /   /  ___ \/  ___ \
 *  /        \ /  _____/\    //   //   __   / /    /___/  _____/  _____/
 * /____/\____\\_____/   \__//___//___/ /__/ /________/\_____/ \_____/
 * </pre>
 *
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-25)
 */
public final class Trac2GitLabUtil
{
  private static final Pattern TRAC_WIKI_PATTERN_WITH_APPLICATION = Pattern.compile("\\{\\{\\{[\\s]*#!application/");
  private static final Pattern TRAC_WIKI_PATTERN_WITH_TEXT = Pattern.compile("\\{\\{\\{[\\s]*#!text/");
  private static final Pattern TRAC_WIKI_PATTERN_WITH_MIME_TYPE = Pattern.compile("\\{\\{\\{#!");
  private static final Pattern TRAC_WIKI_PATTERN = Pattern.compile("\\{\\{\\{");
  private static final Pattern TRAC_WIKI_PATTERN_CLOSE = Pattern.compile("[\\n\\r]\\}\\}\\}");

  private static final String GIT_LAB_CODE_BLOCK = "```";

  private static final Map<Pattern, String> PATTERN_TO_NEW_STRING_MAP;

  static
  {
    final Map<Pattern, String> map = newLinkedHashMap();
    map.put(TRAC_WIKI_PATTERN_WITH_APPLICATION, GIT_LAB_CODE_BLOCK);
    map.put(TRAC_WIKI_PATTERN_WITH_TEXT, GIT_LAB_CODE_BLOCK);
    map.put(TRAC_WIKI_PATTERN_WITH_MIME_TYPE, GIT_LAB_CODE_BLOCK);
    map.put(TRAC_WIKI_PATTERN, GIT_LAB_CODE_BLOCK);
    map.put(TRAC_WIKI_PATTERN_CLOSE, "\n" + GIT_LAB_CODE_BLOCK);
    PATTERN_TO_NEW_STRING_MAP = Collections.unmodifiableMap(map);
  }

  private Trac2GitLabUtil() throws IllegalAccessException
  {
    throw new IllegalAccessException(getClass().getName() + CommonConstants.CANNOT_BE_INSTANTIATED);
  }

  public static String convertCodeBlockForGitLabMarkDown(final String tracWiki)
  {
    String result = tracWiki;
    for (final Entry<Pattern, String> entry : PATTERN_TO_NEW_STRING_MAP.entrySet())
    {
      result = entry.getKey()
          .matcher(result)
          .replaceAll(entry.getValue());
    }
    return result;
  }
}
