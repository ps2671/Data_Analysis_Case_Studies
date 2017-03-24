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
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class TemplateTreeProcessor {
  private TreeProcessor blockProcessor_;

  public TreeProcessor getBlockProcessor() {
    return blockProcessor_;
  }

  public void setBlockProcessor(TreeProcessor blockProcessor) {
    this.blockProcessor_ = blockProcessor;
  }

  public TemplateTreeProcessor(TreeProcessor blockProcessor) {
    blockProcessor_ = blockProcessor;
  }

  public TreeProcessor process(JSONObject dictionary) throws BEMException {
    if (dictionary.has(DataTreeProcessor.BLOCK_ATTRIBUTE) &&
        dictionary.get(DataTreeProcessor.BLOCK_ATTRIBUTE) instanceof String) {
      return blockProcessor_;
    }
    Map<String, TreeProcessor> processors = new TreeMap();
    for (String key : dictionary.keySet()) {
      processors.put(key, (TreeProcessor) process(dictionary.get(key)));
    }
    return new TreeProcessorDictionary(processors);
  }

  public TreeProcessor process(JSONArray array) throws BEMException {
    List<TreeProcessor> processors = new LinkedList();
    for (int i = 0; i < array.length(); ++i) {
      processors.add((TreeProcessor) process(array.get(i)));
    }
    return new TreeProcessorArray(processors);
  }

  public TreeProcessor process(String string) throws BEMException {
    return new StringTemplateTreeProcessor(string);
  }

  public TreeProcessor process(Number number) throws BEMException {
    return new ConstantTemplateTreeProcessor(number);
  }

  public TreeProcessor process(Boolean bool) throws BEMException {
    return new ConstantTemplateTreeProcessor(bool);
  }

  public TreeProcessor process(Object object) throws BEMException {
    if (object == JSONObject.NULL) {
      return new ConstantTemplateTreeProcessor();
    }
    if (object instanceof JSONObject) {
      return process((JSONObject) object);
    }
    if (object instanceof JSONArray) {
      return process((JSONArray) object);
    }
    if (object instanceof String) {
      return process((String) object);
    }
    if (object instanceof Number) {
      return process((Number) object);
    }
    if (object instanceof Boolean) {
      return process((Boolean) object);
    }
    throw new BEMException("Unknown type of object");
  }

}
