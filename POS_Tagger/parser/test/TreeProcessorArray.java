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

import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alex-ac
 */
public class TreeProcessorArray implements TreeProcessor {
  private final Collection<TreeProcessor> processors_;
  private final TreeProcessor adapter_;

  public TreeProcessorArray(final Collection<TreeProcessor> processors) {
    processors_ = processors;
    adapter_ = new TreeProcessorAdapter(this);
  }

  @Override
  public Object process(JSONObject dictionary) throws BEMException {
    JSONArray result = new JSONArray();
    for (TreeProcessor processor : processors_) {
      result.put(processor.process(dictionary));
    }
    return result;
  }

  @Override
  public Object process(JSONArray array) throws BEMException {
    JSONArray result = new JSONArray();
    for (TreeProcessor processor : processors_) {
      result.put(processor.process(array));
    }
    return result;
  }

  @Override
  public Object process(String string) throws BEMException {
    JSONArray result = new JSONArray();
    for (TreeProcessor processor : processors_) {
      result.put(processor.process(string));
    }
    return result;
  }

  @Override
  public Object process(Number number) throws BEMException {
    JSONArray result = new JSONArray();
    for (TreeProcessor processor : processors_) {
      result.put(processor.process(number));
    }
    return result;
  }

  @Override
  public Object process(Boolean bool) throws BEMException {
    JSONArray result = new JSONArray();
    for (TreeProcessor processor : processors_) {
      result.put(processor.process(bool));
    }
    return result;
  }

  @Override
  public Object process(Object object) throws BEMException {
    if (object == JSONObject.NULL) {
      JSONArray result = new JSONArray();
      for (TreeProcessor processor : processors_) {
        result.put(processor.process(object));
      }
      return result;
    }
    return adapter_.process(object);
  }
}
