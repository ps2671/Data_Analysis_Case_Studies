/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.bem;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alex
 */
public class TreeProcessorAdapter implements TreeProcessor {

  private final TreeProcessor processor_;

  public TreeProcessor getProcessor() {
    return processor_;
  }

  public TreeProcessorAdapter(TreeProcessor processor) {
    processor_ = processor;
  }

  @Override
  public Object process(JSONObject dictionary) throws BEMException {
    return processor_.process(dictionary);
  }

  @Override
  public Object process(JSONArray array) throws BEMException {
    return processor_.process(array);
  }

  @Override
  public Object process(String string) throws BEMException {
    return processor_.process(string);
  }

  @Override
  public Object process(Number number) throws BEMException {
    return processor_.process(number);
  }

  @Override
  public Object process(Boolean bool) throws BEMException {
    return processor_.process(bool);
  }

  @Override
  public Object process(Object object) throws BEMException {
    if (object == JSONObject.NULL) {
      return processor_.process(object);
    }
    if (object instanceof JSONObject) {
      return processor_.process((JSONObject) object);
    }
    if (object instanceof JSONArray) {
      return processor_.process((JSONArray) object);
    }
    if (object instanceof String) {
      return processor_.process((String) object);
    }
    if (object instanceof Number) {
      return processor_.process((Number) object);
    }
    if (object instanceof Boolean) {
      return processor_.process((Boolean) object);
    }
    throw new BEMException("Unknown type of object!");
  }
}
