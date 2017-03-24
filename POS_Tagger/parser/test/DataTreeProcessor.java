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

import org.json.JSONArray;
import org.json.JSONObject;

public class DataTreeProcessor implements TreeProcessor {
  private final TreeProcessor adapter_;
  private TreeProcessor blockProcessor_;
  public static final String BLOCK_ATTRIBUTE = "block";

  public TreeProcessor getBlockProcessor() {
    return blockProcessor_;
  }

  public void setBlockProcessor(TreeProcessor blockProcessor) {
    this.blockProcessor_ = blockProcessor;
  }

  public DataTreeProcessor(TreeProcessor blockProcessor) {
    adapter_ = new TreeProcessorAdapter(this);
    blockProcessor_ = blockProcessor;
  }

  @Override
  public Object process(JSONObject dictionary) throws BEMException {
    JSONObject result = new JSONObject();
    for (String key : dictionary.keySet()) {
      result.put(key, process(dictionary.get(key)));
    }
    if (result.has(BLOCK_ATTRIBUTE) &&
        result.get(BLOCK_ATTRIBUTE) instanceof String) {
      if (blockProcessor_ == null) {
        throw new BEMException(
                "Block found, but there is no block processor set!");
      }
      return blockProcessor_.process(result);
    }
    return result;
  }

  @Override
  public Object process(JSONArray array) throws BEMException {
    JSONArray result = new JSONArray();
    for (int i = 0; i < array.length(); ++i) {
      result.put(process(array.get(i)));
    }
    return result;
  }

  @Override
  public Object process(String string) throws BEMException {
    return string;
  }

  @Override
  public Object process(Number number) throws BEMException {
    return number;
  }

  @Override
  public Object process(Boolean bool) throws BEMException {
    return bool;
  }

  @Override
  public Object process(Object object) throws BEMException {
    if (object == JSONObject.NULL) {
      return object;
    }
    return adapter_.process(object);
  }

}
