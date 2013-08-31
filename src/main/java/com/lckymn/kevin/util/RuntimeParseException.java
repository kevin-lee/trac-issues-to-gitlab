/**
 *
 */
package com.lckymn.kevin.util;

import java.text.ParseException;

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
 * @version 0.0.1 (2013-08-31)
 */
public class RuntimeParseException extends RuntimeException
{
  private static final long serialVersionUID = -4983557347192166811L;

  private int errorOffset;

  public RuntimeParseException(final String message, final int errorOffset)
  {
    this(message, errorOffset, null);
  }

  public RuntimeParseException(final String message, final int errorOffset, final ParseException cause)
  {
    super(message, cause);
    this.errorOffset = errorOffset;
  }

  public int getErrorOffset()
  {
    return errorOffset;
  }
}
