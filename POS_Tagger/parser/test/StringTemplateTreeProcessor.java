/*
 * Copyright (c) 2014, alex-ac
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package info.bem;

import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alex-ac
 */
class StringTemplateTreeProcessor implements TreeProcessor {
  private final String string_;
  private final List<TreeMatcher> matchers_;
  private static final String TEMPLATE_START = "${context";
  private static final String TEMPLATE_END = "}";

  public StringTemplateTreeProcessor(String string) throws BEMException {
    if (string.startsWith(TEMPLATE_START) &&
        string.endsWith(TEMPLATE_END)) {
      string_ = null;
      matchers_ = new LinkedList();
      string = string.substring(TEMPLATE_START.length(),
                                string.length() - TEMPLATE_END.length());
      if (string.isEmpty()) {
        return;
      }
      int offset = 0;
      do {
        int splitIndex = string.indexOf('.', offset + 1);
        if (splitIndex == -1) {
          splitIndex = string.indexOf('[', offset + 1);
        }
        if (splitIndex != -1) {
          matchers_.add(TreeMatcher.Create(string.substring(offset,
                                                            splitIndex)));
          offset = splitIndex;
          continue;
        }
        matchers_.add(TreeMatcher.Create(string.substring(offset)));
        break;
      } while (true);
    } else {
      string_ = string;
      matchers_ = null;
    }
  }

  @Override
  public Object process(JSONObject dictionary) throws BEMException {
    return process((Object) dictionary);
  }

  @Override
  public Object process(JSONArray array) throws BEMException {
    return process((Object) array);
  }

  @Override
  public Object process(String string) throws BEMException {
    return process((Object) string);
  }

  @Override
  public Object process(Number number) throws BEMException {
    return process((Object) number);
  }

  @Override
  public Object process(Boolean bool) throws BEMException {
    return process((Object) bool);
  }

  @Override
  public Object process(Object object) throws BEMException {
    if (string_ != null) {
      return string_;
    }
    for (TreeMatcher matcher : matchers_) {
      object = matcher.process(object);
    }
    return object;
  }

}
