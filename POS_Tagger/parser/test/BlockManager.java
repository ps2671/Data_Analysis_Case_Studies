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

import java.util.Map;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class BlockManager implements TreeProcessor {
  private final TreeProcessor adapter_;
  private TreeProcessor defaultBlock_;
  private final Map<String, TreeProcessor> blocks_;

  public BlockManager(TreeProcessor defaultBlock) {
    adapter_ = new TreeProcessorAdapter(this);
    defaultBlock_ = defaultBlock;
    blocks_ = new TreeMap();
  }

  public void setDefaultBlock(TreeProcessor defaultBlock) {
    defaultBlock_ = defaultBlock;
  }

  public TreeProcessor getDefaultBlock() {
    return defaultBlock_;
  }

  public void setBlock(String name, TreeProcessor block) {
    blocks_.put(name, block);
  }

  public TreeProcessor getBlock(String name) {
    if (blocks_.containsKey(name)) {
      return blocks_.get(name);
    }
    return defaultBlock_;
  }

  @Override
  public Object process(JSONObject dictionary) throws BEMException {
    if (dictionary.has(DataTreeProcessor.BLOCK_ATTRIBUTE) &&
        dictionary.get(DataTreeProcessor.BLOCK_ATTRIBUTE) instanceof String) {
      String name = dictionary.getString(DataTreeProcessor.BLOCK_ATTRIBUTE);
      return getBlock(name).process(dictionary);
    }
    throw new BEMException("Dictionary has no " +
                           DataTreeProcessor.BLOCK_ATTRIBUTE +
                           " attribute: " + dictionary.toString());
  }

  @Override
  public Object process(JSONArray array) throws BEMException {
    throw new BEMException("Dictionary expected, but array was given: " +
                           array.toString());
  }

  @Override
  public Object process(String string) throws BEMException {
    throw new BEMException("Dictionary expected, but string was given: " +
                           string);
  }

  @Override
  public Object process(Number number) throws BEMException {
    throw new BEMException("Dictionary expected, but number was given: " +
                           number.toString());
  }

  @Override
  public Object process(Boolean bool) throws BEMException {
    throw new BEMException("Dictionary expected, but boolean was given: " +
                           bool.toString());
  }

  @Override
  public Object process(Object object) throws BEMException {
    if (object == JSONObject.NULL) {
      throw new BEMException("Dictionary expected, but null was given.");
    }
    return adapter_.process(object);
  }
}
