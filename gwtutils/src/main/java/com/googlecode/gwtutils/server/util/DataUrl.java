/**
 * <pre>
 * Copyright (c) 1995-2013 levigo holding gmbh. All Rights Reserved.
 *
 * This software is the proprietary information of levigo holding gmbh.
 * Use is subject to license terms.
 * </pre>
 */
package com.googlecode.gwtutils.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class for converting a DataUrl
 */
public class DataUrl {
  private String rawdata = null;
  private String mineType = null;
  private String charset = null;
  private boolean base64 = false;

  public DataUrl(String data) {
    if (!data.startsWith("data:"))
      throw new RuntimeException("Illegal Format");

    String format = data.substring(5, data.indexOf(","));
    rawdata = data.substring(format.length() + 6);

    String[] formats = format.split(";");

    mineType = formats[0];

    if (formats.length == 2) {
      if (formats[1].startsWith("charset="))
        charset = formats[1].substring(9, formats[1].length() - 10);
      else
        base64 = true;
    } else if (formats.length == 3) {
      if (!formats[1].startsWith("charset="))
        throw new RuntimeException("Illegal Format");
      charset = formats[1].substring(9, formats[1].length() - 10);
      if (!formats[1].equals("base64"))
        throw new RuntimeException("Illegal Format");
      base64 = true;
      if (!formats[2].equals("base64"))
        throw new RuntimeException("Illegal Format");

    } else if (formats.length > 3) {
      throw new RuntimeException("Illegal Format");
    }
  }

  public String getMineType() {
    return mineType;
  }

  public String getCharset() {
    return charset;
  }

  public boolean isBase64() {
    return base64;
  }

  public String getRawdata() {
    return rawdata;
  }


  public InputStream getInputStream() {
    return new ByteArrayInputStream(Base64.decode(rawdata));
  }


  public static String toDataUrl(String mineType, InputStream in) throws IOException {
    return toDataUrl(mineType, inputStreamToString(in));
  }

  private static byte[] inputStreamToString(InputStream in) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    int next = in.read();
    while (next > -1) {
      bos.write(next);
      next = in.read();
    }
    bos.flush();
    return bos.toByteArray();
  }

  public static String toDataUrl(String mineType, byte[] binaryData) {
    return toDataUrl(mineType, null, binaryData);
  }

  public static String toDataUrl(String mineType, String charset, byte[] binaryData) {
    String rawData = Base64.encode(binaryData);
    return "data:" + mineType + (charset != null ? ";charset=" + charset : "") + ";base64," + rawData;
  }
}
