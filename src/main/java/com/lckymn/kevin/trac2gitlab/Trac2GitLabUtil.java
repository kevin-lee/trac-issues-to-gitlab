/**
 *
 */
package com.lckymn.kevin.trac2gitlab;

import static org.elixirian.kommonlee.util.string.RegularExpressionUtil.*;

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
  private static final String OPENING_TRIPLE_BRACES = "{{{";
  private static final int OPENING_TRIPLE_BRACES_LENGTH = OPENING_TRIPLE_BRACES.length();
  private static final String CLOSING_TRIPLE_BRACES = "}}}";
  private static final int CLOSING_TRIPLE_BRACES_LENGTH = CLOSING_TRIPLE_BRACES.length();

  private static final String APPLICATION = "#!application/";
  private static final int APPLICATION_LENGTH = APPLICATION.length();
  /**
   * {{{[\s]*#!application/
   */
  private static final Pattern TRAC_WIKI_PATTERN_WITH_APPLICATION = Pattern.compile("\\{\\{\\{[\\s]*" + APPLICATION);

  private static final String TEXT = "#!text/";
  private static final int TEXT_LENGTH = TEXT.length();
  /**
   * {{{[\s]*#!text/
   */
  private static final Pattern TRAC_WIKI_PATTERN_WITH_TEXT = Pattern.compile("\\{\\{\\{[\\s]*" + TEXT);

  private static final String MIME_TYPE_SIGNS = "#!";
  private static final int MIME_TYPE_SIGNS_LENGTH = MIME_TYPE_SIGNS.length();
  /**
   * {{{#!
   */
  private static final Pattern TRAC_WIKI_PATTERN_WITH_MIME_TYPE = Pattern.compile("\\{\\{\\{[\\s]*" + MIME_TYPE_SIGNS);
  private static final Pattern TRAC_WIKI_PATTERN_WITH_NEW_LINES = Pattern.compile("\\{\\{\\{[ \\t]*[\\n\\r]+");
  private static final Pattern TRAC_WIKI_PATTERN = Pattern.compile("\\{\\{\\{");
  private static final Pattern TRAC_WIKI_PATTERN_CLOSE = Pattern.compile("[\\n\\r][ \\t]*\\}\\}\\}");
  private static final Pattern TRAC_WIKI_PATTERN_ONE_LINE_CLOSE = Pattern.compile("[^\\n^\\r][ \\t]*\\}\\}\\}");

  private static final String GIT_LAB_CODE_BLOCK = "```";

  private Trac2GitLabUtil() throws IllegalAccessException
  {
    throw new IllegalAccessException(getClass().getName() + CommonConstants.CANNOT_BE_INSTANTIATED);
  }

  public static String convertCodeBlockForGitLabMarkDown(final String tracWiki)
  {
    final int length = tracWiki.length();
    final String result = tracWiki;

    final StringBuilder stringBuilder = new StringBuilder();

    int startIndex = 0;
    int closeIndex = 0;

    while (startIndex < length)
    {
      int indexFound = indexOf(result, TRAC_WIKI_PATTERN_WITH_APPLICATION, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = result.indexOf(APPLICATION, indexFound) + APPLICATION_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }

      indexFound = indexOf(result, TRAC_WIKI_PATTERN_WITH_TEXT, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = result.indexOf(TEXT, indexFound) + TEXT_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }

      indexFound = indexOf(result, TRAC_WIKI_PATTERN_WITH_MIME_TYPE, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = result.indexOf(MIME_TYPE_SIGNS, indexFound) + MIME_TYPE_SIGNS_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }

      indexFound = indexOf(result, TRAC_WIKI_PATTERN_WITH_NEW_LINES, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = indexFound + OPENING_TRIPLE_BRACES_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }

      indexFound = indexOf(result, TRAC_WIKI_PATTERN, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = indexFound + OPENING_TRIPLE_BRACES_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_ONE_LINE_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }
      stringBuilder.append(result.substring(startIndex));
      break;
    }
    return stringBuilder.toString();
  }

  private static int replaceCodeBlock(final String result, final StringBuilder stringBuilder, final int startIndex,
      final int closeIndex, final int indexFound, final int contentStartIndex)
  {
    final int closingTripleBracesIndex = result.indexOf(CLOSING_TRIPLE_BRACES, closeIndex);
    stringBuilder.append(result.substring(startIndex, indexFound))
        .append(GIT_LAB_CODE_BLOCK)
        .append(result.substring(contentStartIndex, closingTripleBracesIndex))
        .append(GIT_LAB_CODE_BLOCK);
    return closingTripleBracesIndex + CLOSING_TRIPLE_BRACES_LENGTH;
  }
}
