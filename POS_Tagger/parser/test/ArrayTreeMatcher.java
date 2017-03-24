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

public class ArrayTreeMatcher extends TreeMatcher {
  private final int index_;
  private final TreeProcessor adapter_;

  public ArrayTreeMatcher(int index) {
    index_ = index;
    adapter_ = new TreeProcessorAdapter(this);
  }

  @Override
  public Object process(JSONObject dictionary) throws BEMException {
    throw new BEMException("Array expected, but dictionary was given: " +
                           dictionary.toString());
  }

  @Override
  public Object process(JSONArray array) throws BEMException {
    if (array.length() <= index_) {
      throw new BEMException("Array: " + array.toString() +
                             " has no item with index: " +
                             Integer.toString(index_));
    }
    return array.get(index_);
  }

  @Override
  public Object process(String string) throws BEMException {
    throw new BEMException("Array expected, but string was given: " + string);
  }

  @Override
  public Object process(Number number) throws BEMException {
    throw new BEMException("Array expected, but number was given: " +
                           number.toString());
  }

  @Override
  public Object process(Boolean bool) throws BEMException {
    throw new BEMException("Array expected, but boolean was given: " +
                           bool.toString());
  }

  @Override
  public Object process(Object object) throws BEMException {
    if (object == JSONObject.NULL) {
      throw new BEMException("Array expected, but null was given.");
    }
    return adapter_.process(object);
  }

}
