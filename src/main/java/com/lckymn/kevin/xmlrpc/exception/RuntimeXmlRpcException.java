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
package com.lckymn.kevin.xmlrpc.exception;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-29)
 */
public class RuntimeXmlRpcException extends RuntimeException
{
  private static final long serialVersionUID = 3970307268626578895L;

  public final int code;

  public RuntimeXmlRpcException(final int code, final String message, final Throwable cause)
  {
    super(message, cause);
    this.code = code;
  }

  public RuntimeXmlRpcException(final int code, final String message)
  {
    super(message);
    this.code = code;
  }

  public RuntimeXmlRpcException(final int code, final Throwable cause)
  {
    super(cause);
    this.code = code;
  }

  public RuntimeXmlRpcException(final String message, final Throwable cause)
  {
    super(message, cause);
    this.code = 0;
  }

  public RuntimeXmlRpcException(final String message)
  {
    super(message);
    this.code = 0;
  }

  public RuntimeXmlRpcException(final Throwable cause)
  {
    super(cause);
    this.code = 0;
  }

}
